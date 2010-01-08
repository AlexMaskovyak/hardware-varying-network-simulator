package simulation;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;

public class DiscreteScheduledEventSimulator extends Simulator implements ISimulator {

	protected PriorityQueue<IDiscreteScheduledEvent> _queue;
	protected final Condition _eventsAddedCondition = _lock.newCondition();
	
	public DiscreteScheduledEventSimulator() {
		super();
	}
	
	public void init() {
		super.init();
		_queue = 
			new PriorityQueue<IDiscreteScheduledEvent>(
					10, 
					new DiscreteScheduleEventComparator<IDiscreteScheduledEvent>());
	}
	
	public void schedule(IDiscreteScheduledEvent event) {
		System.out.println("got event....getting lock");
		_lock.lock();
		try {
			_queue.offer(event);
			System.out.println("Got event!");
			_eventsAddedCondition.signalAll();
		} finally {
			_lock.unlock();
		}
	}
	
	@Override
	public void run() {
		_lock.lock();
		try {
			while(_state != State.STOPPED ) {
				System.out.println("1");
				while( _queue.isEmpty() ) { System.out.println("await"); _eventsAddedCondition.await(); } 
				System.out.println("2");
				while( !_queue.isEmpty() ) {
					IDiscreteScheduledEvent event = _queue.poll();
					_currentTime = event.getTime();
					event.execute();
					_state = State.TICK;
					fireEvent();
				}
			}
			System.out.println("3");
		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			_lock.unlock();
		}
		System.out.println("4");
	}
}
