import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import simulation.ISimulatable;
import simulation.ISimulator;
import simulation.ISimulatorEvent;
import simulation.ISimulatorListener;
import simulation.SimulatorEvent;



public class SimulatorOld implements ISimulator, Runnable {

	protected List<ISimulatable> _simulatables;
	protected List<ISimulatorListener> _listeners;
	
	protected Integer _simulatablesDone;
	protected final Lock lock = new ReentrantLock();
	protected final Condition _allSimulatableActivityDone = lock.newCondition();
	protected final Condition _pauseRemitted = lock.newCondition();
	
	protected int _timeSteps;
	
	protected boolean _pause;
	protected boolean _stopped;
	protected boolean _allSimulatablesDone;
	protected Thread _thread;
	
	public SimulatorOld(int timeSteps) {
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
	public void addListener(ISimulatorListener simulatorListener) {
		_listeners.add(simulatorListener);
	}

	public void removeListener(ISimulatorListener simulatorListener) {
		_listeners.remove(simulatorListener);
	}
	
	@Override
	public void registerSimulatable(ISimulatable simulatable) {
		_simulatables.remove(simulatable);
	}
	
	public void unregisterSimulatable(ISimulatable simulatable) {
		_simulatables.add(simulatable);
	}
	
	public void fireEvent() {
		ISimulatorEvent o = new SimulatorEvent(this);
		fireEvent(o);
	}
	
	@Override
	public void fireEvent(ISimulatorEvent o) {
		for(ISimulatable simulatable : _simulatables) {
			System.out.println("fire" + _simulatables.size());
			simulatable.handleTickEvent(o);
		}
	}


	
	@Override
	public void step() {
		lock.lock();		
		try { 
			System.out.println(_allSimulatablesDone);
			// create a tick
			fireEvent();
			// wait for everyone to signal completion
			while(!_allSimulatablesDone) { System.out.println("all sims not done"); _allSimulatableActivityDone.await(); }
			_allSimulatablesDone = false;
		} catch( Exception e ) { e.printStackTrace(); }
		finally {
			lock.unlock();
		}
	}

	@Override
	public void simulate(int time) {
		System.out.println("a");
		lock.lock();
		try {
			for( int i = 0; i < time && !_stopped; ++i ) {
				step();
				while(_pause) { _pauseRemitted.await(); }
				_pause = false;
				
			}
		} catch( Exception e ) { e.printStackTrace(); } 
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
				System.out.println("signal!");
				_allSimulatablesDone = true;
				_simulatablesDone = 0;
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
