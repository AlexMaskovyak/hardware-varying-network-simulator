package simulation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract implementation of Simulator.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractSimulator implements ISimulator {

// Fields
	
	/** listeners of state change. */
	protected Set<ISimulatorListener> _listeners;
	/** simulatable objects to receive tick events. */
	protected Set<ISimulatable> _simulatables;

	/** provides protection for multi-threaded operation. */
	protected final Lock _lock = new ReentrantLock();

	/** latest time that has occurred. */
	protected double _currentTime;

// Construction
	
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
	}

// Listener management
		
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

// Methods
	
	@Override
	public double getTime() {
		return _currentTime;
	}

	/**
	 * Sets the time of the simulator.  Protects against motion in the anti-time
	 * direction of duration.
	 * @param newTime to which to update current time.
	 */
	protected void setTime(int newTime) {
		_currentTime = (newTime > _currentTime) ? newTime : _currentTime;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void signalDone(ISimulatable simulatable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void simulate(int time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
}
