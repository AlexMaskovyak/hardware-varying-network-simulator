package simulation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementatin of ISimulator.  Provides hookup for simulatable objects and creates time events.
 * 
 * @author Alex Maskovyak
 *
 */
public class Simulator implements ISimulator, Runnable {

	/**
	 * States of operation a simulator can enter.
	 * @author Alex Maskovyak
	 */
	protected enum State { REGISTERED_SIMULATABLE, UNREGISTERED_SIMULATABLE, INITIALIZED, STARTED, PAUSED, RESUMED, STEPPED, STOPPED, TICK, SIMULATED };

	/** provides protection for multi-threaded operation. */
	protected final Lock _lock = new ReentrantLock();
	/** occurs when all simulatables have reported "done" */
	protected final Condition _simulatablesDoneCondition = _lock.newCondition();
	/** occurs when the simulator has been "started" again */
	protected final Condition _startedCondition = _lock.newCondition();
	
	
	/** listeners of state change. */
	protected Set<ISimulatorListener> _listeners;
	/** simulatable objects to receive tick events. */
	protected Set<ISimulatable> _simulatables;
	/** current state of the simulator. */
	protected State _state;
	
	/** number of simulatables done. */
	protected int _simulatablesDoneCount;
	
	/** latest time that has occurred. */
	protected int _currentTime;
	
	
	/**
	 * Default constructor.
	 */
	public Simulator() {
		init();
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		_listeners = new HashSet<ISimulatorListener>();
		_simulatables = new HashSet<ISimulatable>();
		_state = State.INITIALIZED;
		_simulatablesDoneCount = 0;
	}
	
/// listener management
	
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
		try {
			_listeners.remove(simulatorListener);
		} finally {
			_lock.unlock();
		}
	}
	

/// simulatable management

	@Override
	public void registerSimulatable(ISimulatable simulatable) {
		_lock.lock();
		try {
			_simulatables.add(simulatable);
			_listeners.add(new SimulatableSimulatorListener(simulatable));
			simulatable.addListener(new SimulatorSimulatableListener(this));
			
			State _temp = _state;
			_state = State.REGISTERED_SIMULATABLE;
			fireEvent();
			_state = _temp;
			
		} finally {
			_lock.unlock();
		}
	}
	
	@Override
	public void unregisterSimulatable(ISimulatable simulatable) {
		_lock.lock();
		try {
			_simulatables.remove(simulatable);
			_listeners.remove(new SimulatableSimulatorListener(simulatable));
			simulatable.removeListener(new SimulatorSimulatableListener(this));
			
			State _temp = _state;
			_state = State.UNREGISTERED_SIMULATABLE;
			fireEvent();
			_state = _temp;
			
		} finally {
			_lock.unlock();
		}
	}
	
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
	
	public void fireEvent() {
		notify(new SimulatorEvent(this, _currentTime));
	}
	
	@Override
	public void notify(ISimulatorEvent o) {
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

	@Override
	public void run() {
		start();
		simulate(10);
	}

	@Override
	public int getTime() {
		return _currentTime;
	}
}
