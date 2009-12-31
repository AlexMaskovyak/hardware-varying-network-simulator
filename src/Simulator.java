import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Simulator extends Observable implements ISimulator, Runnable {

	protected List<ISimulatable> _simulatables;
	protected Integer _simulatablesDone;
	protected final Lock lock = new ReentrantLock();
	protected final Condition _allSimulatableActivityDone = lock.newCondition();
	protected final Condition _pauseRemitted = lock.newCondition();
	
	protected int _timeSteps;
	
	protected boolean _pause;
	protected boolean _stopped;
	protected boolean _allSimulatablesDone;
	protected Thread _thread;
	
	public Simulator(int timeSteps) {
		_timeSteps = timeSteps;
		init();
	}
	
	protected void init() {
		_simulatablesDone = 0;
		_simulatables = new ArrayList<ISimulatable>();
		_thread = new Thread(this);
		_allSimulatablesDone = false;
		_stopped = false;
	}
	
	@Override
	public void addListener(ISimulatable simulatable) {
		_simulatables.add(simulatable);
	}

	public void fireEvent() {
		EventObject o = new EventObject(this);
		fireEvent(o);
	}
	
	@Override
	public void fireEvent(EventObject o) {
		for(ISimulatable simulatable : _simulatables) {
			simulatable.handleTickEvent(o);
		}
	}

	@Override
	public void removeListener(ISimulatable simulatable) {
		_simulatables.remove(simulatable);
	}
	
	@Override
	public void step() {
		lock.lock();		
		try { 
			// create a tick
			fireEvent();
			// wait for everyone to signal completion
			while(!_allSimulatablesDone) { _allSimulatableActivityDone.await(); }
			_allSimulatablesDone = false;
		} catch( Exception e ) {}
		finally {
			lock.unlock();
		}
	}

	@Override
	public void simulate(int time) {
		lock.lock();
		try {
			for( int i = 0; i < time && !_stopped; ++i ) {
				step();
				while(_pause) { _pauseRemitted.await(); }
				_pause = false;
				
			}
		} catch( Exception e ) {}
		finally {
			lock.unlock();
		}
	}

	@Override
	public void start() {
		// init conditions
		_allSimulatablesDone = false;
		_stopped = false;
	}

	@Override
	public void stop() {
		_stopped = true;
	}

	@Override
	public void pause() {
		_pause = true;
	}
	
	public void resume() {
		_pause = false;
		_pauseRemitted.signal();
	}
	
	public void signalDone(ISimulatable simulatable) {
		synchronized( _simulatablesDone ) {
			++_simulatablesDone;
			if( _simulatablesDone.intValue() == _simulatables.size() ) {
				_allSimulatablesDone = true;
				_allSimulatableActivityDone.signal();
			}
		}
	}

	@Override
	public void run() {
		start();
		simulate(_timeSteps);
	}
}
