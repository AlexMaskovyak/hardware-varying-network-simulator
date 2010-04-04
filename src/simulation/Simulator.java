package simulation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementatin of ISimulator.  Provides hookup for simulatable objects and creates time events.
 * 
 * @author Alex Maskovyak
 *
 */
public class Simulator 
		extends AbstractSimulator
		implements ISimulator, Runnable {

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
		TICK, 
		SIMULATED };

	/** current state of the simulator. */
	protected State _state;
		
	/** provides protection for multi-threaded operation. */
	protected final Lock _lock = new ReentrantLock();
	/** occurs when all simulatables have reported "done" */
	protected final Condition _simulatablesDoneCondition = _lock.newCondition();
	/** occurs when the simulator has been "started" again */
	protected final Condition _startedCondition = _lock.newCondition();
	
	
	/** number of simulatables done. */
	protected int _simulatablesDoneCount;
	
	/** Default constructor. */
	public Simulator() {
		super();
	}

	/** Externalize instantiation. */
	protected void init() {
		super.init();
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
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#signalDone(simulation.ISimulatable)
	 */
	@Override
	public void signalDone(ISimulatable simulatable) {
		_lock.lock();
		try {
			++_simulatablesDoneCount;
			if( _simulatablesDoneCount == _simulatables.size() ) {
				_simulatablesDoneCount = 0;
				_state = State.STEPPED;
				_simulatablesDoneCondition.signalAll();
			}
		} finally {
			_lock.unlock();
		}
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
					case TICK: listener.tickUpdate(o); break;
					case SIMULATED: listener.simulatedUpdate(o); break;
					case REGISTERED_SIMULATABLE: listener.simulatableRegisteredUpdate(o); break;
					case UNREGISTERED_SIMULATABLE: listener.simulatableUnregisteredUpdate(o); break;
				}
			}
		} finally {
			_lock.unlock();
		}
	}

/// simulator operation control

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#start()
	 */
	@Override
	public void start() {
		_lock.lock();
		try {
			_state = State.STARTED;
			_simulatablesDoneCount = 0;
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
	

/// simulator primary operation

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#step()
	 */
	@Override
	public void step() {
		_lock.lock();
		try {
			// create a tick
			++_currentTime;
			_state = State.TICK;
			fireEvent();
			while( _state == State.TICK ) { _simulatablesDoneCondition.await(); }
			_state = State.STARTED;
			System.out.println("---");
		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			_lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulator#simulate(int)
	 */
	@Override
	public void simulate(int time) {
		_lock.lock();
		try {
			for( int i = 0; i < time && _state != State.STOPPED; ++i ) {
				step();
				while( _state != State.STARTED ) { _startedCondition.await(); }
			}
			_state = State.SIMULATED;
			fireEvent();
		} catch (Exception e) { e.printStackTrace(); }
		finally {
			_lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		start();
		simulate(10);
	}
}
