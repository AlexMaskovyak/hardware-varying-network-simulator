package simulation.simulator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import simulation.simulatable.ISimulatable;
import simulation.simulator.listeners.ISimulatorEvent;
import simulation.simulator.listeners.ISimulatorListener;
import simulation.simulator.listeners.SimulatableSimulatorListener;
import simulation.simulator.listeners.SimulatorEvent;

/**
 * Abstract implementation of Simulator.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractSimulator 
		implements ISimulator {

/// State 
	
	/**
	 * States of operation a simulator can enter.
	 * @author Alex Maskovyak
	 */
	protected enum State { 
		REGISTERED_SIMULATABLE, 
		UNREGISTERED_SIMULATABLE, 
		INITIALIZED, 
		STARTED, 
		PAUSED, 
		RESUMED, 
		STEPPED, 
		STOPPED,  
		SIMULATED };
		
	
/// Fields
	
	/** listeners of state change. */
	protected Set<ISimulatorListener> _listeners;
	/** simulatable objects to receive tick events. */
	protected Set<ISimulatable> _simulatables;

	/** provides protection for multi-threaded operation. */
	protected final Lock _lock = new ReentrantLock();
	/** occurs when the simulator has been "started" again */
	protected final Condition _startedCondition = _lock.newCondition();

	/** latest time that has occurred. */
	protected double _currentTime;

	/** current state of the simulator. */
	protected State _state;
		
	
	
/// Construction
	
	/**
	 * Default constructor.
	 */
	protected AbstractSimulator() {
		init();
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		_listeners = new HashSet<ISimulatorListener>();
		_simulatables = new HashSet<ISimulatable>();
		_state = State.PAUSED;
	}

	
/// Listener management
		
	@Override
	public void addListener(ISimulatorListener simulatorListener) {
		_lock.lock();
		try {
			_listeners.add(simulatorListener);
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public void removeListener(ISimulatorListener simulatorListener) {
		_lock.lock();
		try { _listeners.remove(simulatorListener);} 
		finally { _lock.unlock(); }
	}

	/**
	 * Short-cut to notifying listeners and simulatables about events after we
	 * undergo a state-change.
	 */
	public void fireEvent() {
		notifyListeners(new SimulatorEvent(this, getTime()));
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulator#notifyListeners(simulation.ISimulatorEvent)
	 */
	@Override
	public void notifyListeners(ISimulatorEvent o) {
		_lock.lock();
		try {
			for(ISimulatorListener listener : _listeners) {
				switch(_state) {
					case PAUSED: listener.pauseUpdate(o); break;
					case RESUMED: listener.resumeUpdate(o); break;
					case STARTED: listener.startUpdate(o); break;
					case STOPPED: listener.stopUpdate(o); break;
					case SIMULATED: listener.simulatedUpdate(o); break;
					case REGISTERED_SIMULATABLE: listener.simulatableRegisteredUpdate(o); break;
					case UNREGISTERED_SIMULATABLE: listener.simulatableUnregisteredUpdate(o); break;
				}
			}
		} finally {
			_lock.unlock();
		}
	}

	
/// simulatable management

	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulator#registerSimulatable(simulation.ISimulatable)
	 */
	@Override
	public void registerSimulatable(ISimulatable simulatable) {
		_lock.lock();
		try {
			_simulatables.add(simulatable);
			simulatable.setSimulator( this );
			_listeners.add(new SimulatableSimulatorListener(simulatable));
			
			State _temp = _state;
			_state = State.REGISTERED_SIMULATABLE;
			fireEvent();
			_state = _temp;
			
		} finally {
			_lock.unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulator#unregisterSimulatable(simulation.ISimulatable)
	 */
	@Override
	public void unregisterSimulatable(ISimulatable simulatable) {
		_lock.lock();
		try {
			_simulatables.remove(simulatable);
			simulatable.setSimulator( null );
			_listeners.remove(new SimulatableSimulatorListener(simulatable));
			
			State _temp = _state;
			_state = State.UNREGISTERED_SIMULATABLE;
			fireEvent();
			_state = _temp;
			
		} finally {
			_lock.unlock();
		}
	}
	
	
/// Methods
	
	@Override
	public double getTime() {
		_lock.lock();
		try { return _currentTime; } 
		finally { _lock.unlock(); }
	}
	
	/**
	 * Sets the time of the simulator.  Protects against motion in the anti-time
	 * direction of duration.
	 * @param newTime to which to update current time.
	 */
	protected void setTime(double newTime) {
		_lock.lock();
		try { _currentTime = (newTime > _currentTime) ? newTime : _currentTime;}
		finally { _lock.unlock(); }
	}
	

/// ISimulator
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulator#step()
	 */
	@Override
	public void step(double interval) {
		_lock.lock();
		try {
			_state = State.STEPPED;
			fireEvent();
			simulate( getTime() + interval );
		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			_lock.unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#start()
	 */
	@Override
	public void start() {
		_lock.lock();
		try {
			_state = State.STARTED;
			_currentTime = 0;
			fireEvent();
			_startedCondition.signalAll();
		} finally {
			_lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#stop()
	 */
	@Override
	public void stop() {
		_lock.lock();
		try {
			_state = State.STOPPED;
			Thread.currentThread().interrupt();
			fireEvent();
		} finally {
			_lock.unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#pause()
	 */
	@Override
	public void pause() {
		_lock.lock();
		try {
			_state = State.PAUSED;
			fireEvent();
		} finally {
			_lock.unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#resume()
	 */
	@Override
	public void resume() {
		_lock.lock();
		try {
			_state = State.RESUMED;
			fireEvent();
			_state = State.STARTED;
			fireEvent();
			_startedCondition.signalAll();
		} finally {
			_lock.unlock();
		}
	}
}
