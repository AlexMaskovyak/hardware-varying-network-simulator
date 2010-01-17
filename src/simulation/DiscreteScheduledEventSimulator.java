package simulation;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;

public class DiscreteScheduledEventSimulator extends Simulator implements ISimulator, IDiscreteScheduledEventSimulator {

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
	
	/* (non-Javadoc)
	 * @see simulation.IDiscretedScheduledEventSimulator#schedule(simulation.IDiscreteScheduledEvent)
	 */
	public void schedule(IDiscreteScheduledEvent event) {
		_lock.lock();
		try {
			if ( event.getTime() >= getTime() ) {
				_queue.offer(event);
				_eventsAddedCondition.signalAll();
			}
		} finally {
			_lock.unlock();
		}
	}
	
	/* (non-Javadoc)
	 * @see simulation.IDiscretedScheduledEventSimulator#run()
	 */
	@Override
	public void run() {
		_lock.lock();
		try {
			while( _state != State.STOPPED ) {
				while( _queue.isEmpty() ) { _eventsAddedCondition.await(); }
				while( !_queue.isEmpty() ) {
					while( _state == State.PAUSED ) { _startedCondition.await(); }
					IDiscreteScheduledEvent event = _queue.poll();
					_currentTime = event.getTime();
					event.execute();
					_state = State.TICK;
					fireEvent();
				}
			}
		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			_lock.unlock();
		}
	}
}
