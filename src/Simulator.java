import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Simulator extends Observable implements ISimulator {

	protected List<ISimulatable> simulatables;
	protected Integer simulatablesDone;
	final Lock lock = new ReentrantLock();
	final Condition allSimulatableActivityDone = lock.newCondition();
	
	public Simulator() {
		init();
	}
	
	protected void init() {
		simulatablesDone = 0;
		simulatables = new ArrayList<ISimulatable>();
	}
	
	@Override
	public void addListener(ISimulatable simulatable) {
		simulatables.add(simulatable);
	}

	public void fireEvent() {
		EventObject o = new EventObject(this);
		fireEvent(o);
	}
	
	@Override
	public void fireEvent(EventObject o) {
		for(ISimulatable simulatable : simulatables) {
			simulatable.handleTickEvent(o);
		}
	}

	@Override
	public void removeListener(ISimulatable simulatable) {
		simulatables.remove(simulatable);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void step() {
		lock.lock();		
		try { 
			// create a tick
			fireEvent();
			// wait for everyone to signal completion
			allSimulatableActivityDone.await();
		} catch( Exception e ) {}
		finally {
			lock.unlock();
		}
	}

	@Override
	public void simulate(int time) {
		// TODO Auto-generated method stub
		for( int i = 0; i < time; ++i ) {
			step();
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	public void signalDone(ISimulatable simulatable) {
		synchronized( simulatablesDone ) {
			++simulatablesDone;
			if( simulatablesDone.intValue() == simulatables.size() ) {
				allSimulatableActivityDone.signal();
			}
		}
	}
}
