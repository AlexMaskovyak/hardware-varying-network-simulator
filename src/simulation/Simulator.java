package simulation;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
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
	protected enum State { INITIALIZED, STARTED, PAUSED, STEPPED, STOPPED, TICK, SIMULATED };

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
	protected boolean _simulatablesDone;
	
	
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
		fireEvent(new SimulatorEvent(this));
	}
	
	@Override
	public void fireEvent(ISimulatorEvent o) {
		_lock.lock();
		try {
			for(ISimulatorListener listeners : _listeners) {
				switch(_state) {
					case PAUSED: listeners.pauseUpdate(o); break;
					case STARTED: listeners.startUpdate(o); break;
					case STOPPED: listeners.stopUpdate(o); break;
					case TICK: listeners.tickUpdate(o); break;
					case SIMULATED: listeners.simulatedUpdate(o); break;
				}
			}
		} finally {
			_lock.unlock();
		}
	}

/// simulator operation control
	
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

	@Override
	public void start() {
		_lock.lock();
		try {
			_state = State.STARTED;
			_simulatablesDoneCount = 0;
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
	
/// simulator primary operation
	
	@Override
	public void step() {
		_lock.lock();
		try {
			// create a tick
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
}
