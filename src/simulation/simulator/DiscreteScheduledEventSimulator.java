package simulation.simulator;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;

import simulation.event.DiscreteScheduleEventComparator;
import simulation.event.IDiscreteScheduledEvent;

/**
 * The work-horse of the Discrete Event Simulator.
 * @author Alex Maskovyak
 *
 */
public class DiscreteScheduledEventSimulator 
		extends Simulator 
		implements ISimulator, IDiscreteScheduledEventSimulator {

	/** queue of events. */
	protected PriorityQueue<IDiscreteScheduledEvent> _queue;
	/** lock protecting the addition, subtraction, retrieval of events. */
	protected final Condition _eventsAddedCondition = _lock.newCondition();
	
	/** Default constructor */
	public DiscreteScheduledEventSimulator() {
		super();
	}
	
	/** Externalize instantiation. */
	protected void init() {
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
			// ensure that this isn't from the past
			if ( event.getEventTime() < getTime() ) { return; }
			_queue.offer(event);
			_eventsAddedCondition.signalAll();
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
				// await on events being added
				while( _queue.isEmpty() ) { _eventsAddedCondition.await(); }
				while( _state == State.PAUSED ) { _startedCondition.await(); }
				
				IDiscreteScheduledEvent event = _queue.poll();		// get event
				System.out.println( event.toString() );
				_currentTime = event.getEventTime();				// update time
				System.out.println( event.getDestination() );
				event.getDestination().handleEvent(event);			// get destination and deliver
				
				//Thread.currentThread().sleep(200);
			}
		} catch (Exception e) { e.printStackTrace(); } 
		finally {
			_lock.unlock();
		}
	}
}
