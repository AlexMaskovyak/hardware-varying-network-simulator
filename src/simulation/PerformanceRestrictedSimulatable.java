package simulation;

import messages.SimulatableRefreshMessage;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Simulatables which can perform a restricted number of operations.  This type
 * of siimulatable has 3 states.  It is "Fully-awake", "Partially-awake", and
 * "Blocked".  Fully awake simulatables can perform up to max-operations before
 * requiring a refresh signal.  Fully-awake simulatables transition to partially
 * awake upon handling a request.  At this point, a "refresh" event is scheduled
 * for current_time + renewal_time.  A refresh_time is stored at this point.  
 * 
 * Every request reduces the number of allowed-operations by one.  When the 
 * number of allowed-operations shrinks to zero, state "Blocked" is entered.  
 * When Blocked the simulatable reschedules all requests received for 
 * current_time + original_refresh_time.  Eventually, the "refresh" event will
 * be received allowing the device to re-enter "Awake."
 *
 * @author Alex Maskovyak
 *
 */
public class PerformanceRestrictedSimulatable 
		extends AbstractSimulatable
		implements ISimulatable {

/// State Info

	/** states of this device. */
	protected static enum State {
		FULLY_AWAKE, PARTIALLY_AWAKE, BLOCKED
	};
	
	
/// Fields 
	
	/** number of allowed operations before entering a blocked state. */
	protected int _maxAllowedOperations;
	/** number of operations performed since the last refresh. */
	protected int _remainingOperations;
	/** time between refreshes. */
	protected double _refreshInterval;
	/** time when the last refresh event was scheduled. */
	protected double _lastRefreshTime;
	/** current state of operational status. */
	protected State _state;
	/** time it takes us to send a message. */
	protected double _transitTime;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 */
	public PerformanceRestrictedSimulatable() {
		this(1, 1, 1);
	}
	
	/**
	 * Constructor.
	 * @param maxAllowedOperations between refresh events.
	 * @param refreshInterval for scheduling refresh events.
	 * @param transitTime time it takes us to send a message.
	 */
	public PerformanceRestrictedSimulatable( 
			int maxAllowedOperations, 
			double refreshInterval, 
			double transitTime ) {
		super();
		_maxAllowedOperations = maxAllowedOperations;
		_refreshInterval = refreshInterval;
		_transitTime = transitTime;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#init()
	 */
	protected void init() {
		super.init();
	}


/// Accessors/Mutators
	
	/**
	 * Obtain the maximum number of allowed operations between refreshes.
	 * @return the number of allowed operations.
	 */
	public int getMaxAllowedOperations() {
		return _maxAllowedOperations;
	}
	
	/**
	 * Sets the number of allowed operations between refreshes.
	 * @param maxAllowedOperations between refreshes.
	 */
	public void setMaxAllowedOperations( int maxAllowedOperations ) {
		_maxAllowedOperations = maxAllowedOperations;
	}
	
	/**
	 * Gets the minimum time allowed between refresh events.
	 * @return minimum time allowed between refresh events.
	 */
	public double getRefreshInterval() {
		return _refreshInterval;
	}
	
	/**
	 * Sets the minimum time allowed between refresh events.
	 * @param refreshInterval time between refresh events.
	 */
	public void setRefreshInterval( double refreshInterval ) {
		_refreshInterval = refreshInterval;
	}
	
	/**
	 * Gets the number of operations remaining until the next refresh event 
	 * occurs.
	 * @return number of operations remaining.
	 */
	public int getRemainingOperations() {
		return _remainingOperations;
	}
	
	/**
	 * Sets the remaining number of operations remaining until the next refresh.
	 * @param remainingOperations left during this refresh period.
	 */
	public void setRemainingOperations( int remainingOperations ) {
		_remainingOperations = remainingOperations;
	}
	
	/**
	 * Gets the last time a refresh event was sent.
	 * @return time the last refresh event was sent.
	 */
	public double getLastRefreshTime() {
		return _lastRefreshTime;
	}
	
	/**
	 * Sets the last time a refresh event was sent.
	 * @param lastRefreshTime time the last refresh event was sent.
	 */
	public void setLastRefreshTime( double lastRefreshTime ) {
		_lastRefreshTime = lastRefreshTime;
	}
	
	/**
	 * Gets the time it takes for us to send a message.  This time is equivalent
	 * to how far in the future our messages will be scheduled from the current
	 * time.
	 */
	public double getTransitTime() {
		return _transitTime;
	}
	
	/**
	 * Sets the time it takes for us to send a message.
	 * @param transitTime is how long it takes for our message to reach a 
	 * destination.
	 */
	public void setTransitTime( double transitTime ) {
		_transitTime = transitTime;
	}
	

/// Helpers
	
	/**
	 * Sets the remaining operations to the maximum allowed.
	 */
	protected void resetRemainingOperations() {
		setRemainingOperations( getMaxAllowedOperations() );
	}
	
	/**
	 * Spends some amount of the remaining operations.  Typically this will be
	 * one operations.
	 */
	protected void spendOperation() {
		setRemainingOperations( getRemainingOperations() - 1 );
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#canPerformOperation()
	 */
	public boolean canPerformOperation() {
		return ( getRemainingOperations() > 0 );
	}
	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		
		// do this for all states.
		if( message instanceof SimulatableRefreshMessage ) {
			_state = State.FULLY_AWAKE;
			setLastRefreshTime( e.getEventTime() );
			resetRemainingOperations();
			return;
		} 
		
		switch( _state ) {
			case FULLY_AWAKE:
				// handle cost and state
				spendOperation();						// cost us
				_state = State.PARTIALLY_AWAKE;			// set next
				
				// handle refresh
				setLastRefreshTime( e.getEventTime() );	// 
				sendRefreshMessage( e.getEventTime() );
				
				// handle event
				subclassHandle( e );
				break;
			case PARTIALLY_AWAKE:
				// handle cost and state
				spendOperation();						// spend operation first
				_state = ( canPerformOperation() ) 		// determine next state
					? State.PARTIALLY_AWAKE
					: State.BLOCKED;
				
				// handle event
				subclassHandle( e );
				break;
			case BLOCKED:
				// reschedule all
				rescheduleEvent( e );
				break;
			default:
				break;
		}
	}

	/**
	 * Meant for subclasses of this one to override.  We won't make it abstract
	 * for the time being to allow this class to be a bit more flexible.
	 * @param e the event to handle.
	 */
	protected void subclassHandle( IDiscreteScheduledEvent e ) {
		
	}
	
	/**
	 * Sends the refresh message to ourself.
	 * @param currentEventTime time baseline from which we are responding.
	 */
	protected void sendRefreshMessage( double currentEventTime ) {
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent<SimulatableRefreshMessage>(
				this, 
				this, 
				currentEventTime + getRefreshInterval(), 
				getSimulator(), 
				new SimulatableRefreshMessage(),
				DefaultDiscreteScheduledEvent.INTERNAL));
	}
	
	/**
	 * Reschedules an event for the point in the future where we'll be able to
	 * receive and deal with it and other requests again.
	 * @param e event to reschedule for ourselves into the future.
	 */
	protected void rescheduleEvent( IDiscreteScheduledEvent e ) {
		IDiscreteScheduledEvent rescheduledEvent = 
			new DefaultDiscreteScheduledEvent<IMessage>(
				e.getSource(), 
				e.getDestination(), 
				getLastRefreshTime() + getRefreshInterval(), 
				e.getSimulator(), 
				e.getMessage(),
				DefaultDiscreteScheduledEvent.EXTERNAL);
		getSimulator().schedule( rescheduledEvent );
	}
}
