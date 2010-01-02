package simulation;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementatin of ISimulator.  Provides hookup for simulatable objects and creates time events.
 * 
 * @author Alex Maskovyak
 *
 */
public class Simulator implements ISimulator {

	/**
	 * States of operation a simulator can enter.
	 * @author Alex Maskovyak
	 */
	protected enum State { INITIALIZED, STARTED, PAUSED, STOPPED, TICK, SIMULATED };

	/** provides protection for multi-threaded operation. */
	protected final Lock _lock = new ReentrantLock();

	
	/** listeners of state change. */
	protected List<ISimulatorListener> _listeners;
	/** simulatable objects to receive tick events. */
	protected List<ISimulatable> _simulatables;
	/** current state of the simulator. */
	protected State _state;
	
	/** number of simulatables done. */
	protected int _simulatablesDone;
	
	/**
	 * Default constructor.
	 */
	public Simulator() {
		init();
	}
	
	/**
	 * Wraps all object instantiation code for the constructor.
	 */
	public void init() {
		_listeners = new ArrayList<ISimulatorListener>();
		_simulatables = new ArrayList<ISimulatable>();
		_state = State.INITIALIZED;
		_simulatablesDone = 0;
	}
	
/// listener management
	
	@Override
	public void addListener(ISimulatorListener simulatorListener) {
		_listeners.add(simulatorListener);
	}

	@Override
	public void removeListener(ISimulatorListener simulatorListener) {
		_listeners.remove(simulatorListener);
	}
	

/// simulatable management

	@Override
	public void registerSimulatable(ISimulatable simulatable) {
		_simulatables.add(simulatable);
		_listeners.add(new SimulatableSimulatorListener(simulatable));
		simulatable.addListener(new SimulatorSimulatableListener(this));
	}
	
	@Override
	public void unregisterSimulatable(ISimulatable simulatable) {
		_simulatables.remove(simulatable);
		_listeners.remove(new SimulatableSimulatorListener(simulatable));
		simulatable.removeListener(new SimulatorSimulatableListener(this));
	}
	
	@Override
	public void signalDone(ISimulatable simulatable) {
		++_simulatablesDone;
	}
	
	public void fireEvent() {
		fireEvent(new SimulatorEvent(this));
	}
	
	@Override
	public void fireEvent(ISimulatorEvent o) {
		for(ISimulatorListener listeners : _listeners) {
			switch(_state) {
				case PAUSED: listeners.pauseUpdate(o); break;
				case STARTED: listeners.startUpdate(o); break;
				case STOPPED: listeners.stopUpdate(o); break;
				case TICK: listeners.tickUpdate(o); break;
			}
		}
	}

/// simulator operation control
	
	@Override
	public void pause() {
		_state = State.PAUSED;
		fireEvent();
	}

	@Override
	public void start() {
		_state = State.STARTED;
		fireEvent();
	}

	@Override
	public void stop() {
		_state = State.STOPPED;
		fireEvent();
	}
	
/// simulator primary operation
	
	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	@Override
	public void simulate(int time) {
		// TODO Auto-generated method stub

	}



}
