package simulation.simulator;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;

import simulation.event.DiscreteScheduleEventComparator;
import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.ISimulatable;
import simulation.simulator.listeners.DESimulatorEvent;
import simulation.simulator.listeners.IDESimulatorListener;
import simulation.simulator.listeners.ISimulatorEvent;
import simulation.simulator.listeners.ISimulatorListener;

/**
 * The work-horse of the Discrete Event Simulator.
 * @author Alex Maskovyak
 *
 */
public class DESimulator 
		extends AbstractSimulator 
		implements ISimulator, IDESimulator {
	
/// Constants
	
	/** run without limit value. */
	protected static final Double INFINITY = Double.NaN;
	
/// Fields
	
	/** queue of events. */
	protected PriorityQueue<IDiscreteScheduledEvent> _queue;
	/** lock protecting the addition, subtraction, retrieval of events. */
	protected final Condition _eventsAddedCondition = _lock.newCondition();
	
/// Construction
	
	/** Default constructor */
	public DESimulator() {
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

/// IDESimulator
	
	/* (non-Javadoc)
	 * @see IDESimulator#schedule(simulation.IDiscreteScheduledEvent)
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
	 * @see simulation.IDESimulator#run()
	 */
	@Override
	public void run() {
		simulate( INFINITY );
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulator.Simulator#simulate(double)
	 */
	@Override
	public void simulate(double timeLimit) {
		_lock.lock();
		try {
			_state = State.SIMULATED;
			fireEvent();
			boolean runForever = new Double( timeLimit ).equals( INFINITY );
			while( _state != State.STOPPED 
					&& ( runForever || getTime() <= timeLimit ) ) {
				try { // await on events being added
					while( _queue.isEmpty() ) { _eventsAddedCondition.await(); }
					while( _state == State.PAUSED ) { _startedCondition.await(); }
					
					IDiscreteScheduledEvent event = _queue.poll();	// get event
					setTime( event.getEventTime() );				// update time
					notifyListeners( new DESimulatorEvent( this, getTime(), event ) );
					event.getDestination().handleEvent(event);		// get destination and deliver

				} catch( Exception e ) { /* interrupt exceptions kick us here and then to condition check. */ }
			}
		} finally {
			_lock.unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulator#notifyListeners(simulation.ISimulatorEvent)
	 */
	@Override
	public void notifyListeners(ISimulatorEvent o) {
		_lock.lock();
		try {
			for(ISimulatorListener listener : _listeners) {
				if( listener instanceof IDESimulatorListener ) {
					((IDESimulatorListener)listener).update( o );
				}
			}
		} finally {
			_lock.unlock();
		}
	}
}
