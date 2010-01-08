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
		_lock.lock();
		try {
			_queue.offer(event);
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
				while( _queue.isEmpty() ) { _eventsAddedCondition.await(); } 
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
