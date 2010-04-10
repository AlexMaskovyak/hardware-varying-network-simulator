package simulation.simulatable;

import messages.SimulatableRefreshMessage;
import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;

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
	
	
/// Constants
	
	/** defines an unimpeded performance simulatable, that, nonetheless, 
	 * uses the FSM present here. */
	public static final int INFINITY = Integer.MAX_VALUE;
	
/// State Info

	/** states of this device. */
	protected enum State {
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
	private State _state;
	/** time it takes us to send a message. */
	protected double _transitTime;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 */
	public PerformanceRestrictedSimulatable() {
		this(INFINITY, 0, 0);
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
		_state = State.FULLY_AWAKE;
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
	

/// Protected Accessors/Mutators
	
	/**
	 * Gets the number of operations remaining until the next refresh event 
	 * occurs.
	 * @return number of operations remaining.
	 */
	protected int getRemainingOperations() {
		return _remainingOperations;
	}
	
	/**
	 * Sets the remaining number of operations remaining until the next refresh.
	 * @param remainingOperations left during this refresh period.
	 */
	protected void setRemainingOperations( int remainingOperations ) {
		_remainingOperations = remainingOperations;
	}
	
	/**
	 * Gets the last time a refresh event was sent.
	 * @return time the last refresh event was sent.
	 */
	protected double getLastRefreshTime() {
		return _lastRefreshTime;
	}
	
	/**
	 * Sets the last time a refresh event was sent.
	 * @param lastRefreshTime time the last refresh event was sent.
	 */
	protected void setLastRefreshTime( double lastRefreshTime ) {
		_lastRefreshTime = lastRefreshTime;
	}
	

/// Helpers

	/**
	 * Resets this items state to the default start state.
	 */
	protected void reset() {
		resetRemainingOperations();
		setState( State.FULLY_AWAKE );
	}
	
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
		if( getMaxAllowedOperations() != INFINITY ) {
			setRemainingOperations( getRemainingOperations() - 1 );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#canPerformOperation()
	 */
	public boolean canPerformOperation() {
		return ( getRemainingOperations() > 0 ) 
				|| getMaxAllowedOperations() == INFINITY;
	}
	
	/**
	 * Obtains the state of this item.
	 * @return state the Simulatable is in.
	 */
	protected State getState() {
		synchronized( _state ) {
			return _state;
		}
	}
	
	/**
	 * Sets the value of the simulatable's state.
	 * @param newState state to place the simulatable in.
	 */
	protected void setState( State newState ) {
		synchronized( _state ) {
			_state = newState;
		}
	}
	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public synchronized void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
	
		// do this for all states.
		if( message instanceof SimulatableRefreshMessage ) {
			_state = State.FULLY_AWAKE;
			setLastRefreshTime( e.getEventTime() );
			resetRemainingOperations();
			return;
		} 
		
		switch( getState() ) {
			case FULLY_AWAKE:
				// handle cost and state
				spendOperation();						// cost us
				System.out.println(getRemainingOperations());
				setState( State.PARTIALLY_AWAKE );		// set next
				
				// handle refresh
				setLastRefreshTime( e.getEventTime() );	
				sendRefreshMessage( e.getEventTime() );
				
				// handle event
				handleEventDelegate( e );
				break;
			case PARTIALLY_AWAKE:
				// handle cost and state
				spendOperation();						// spend operation first
				System.out.println(getRemainingOperations());
				setState( ( canPerformOperation() ) 	// determine next state
					? State.PARTIALLY_AWAKE
					: State.BLOCKED );
				
				// handle event
				handleEventDelegate( e );
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
	 * for the time being to allow this class to be a bit more flexible.  This 
	 * is a hook into the FSM that controls its own actions!  Use this to 
	 * maintain the performance restricted functionality of this class.
	 * @param e the event to handle.
	 */
	protected void handleEventDelegate( IDiscreteScheduledEvent e ) {}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#notifyListeners()
	 */
	public void notifyListeners() {
		switch( getState() ) {
			case FULLY_AWAKE: break;
			case PARTIALLY_AWAKE: break;
			case BLOCKED: break;
		}
	}
	
	/**
	 * Sends the refresh message to ourself.
	 * @param currentEventTime time baseline from which we are responding.
	 */
	protected void sendRefreshMessage( double currentEventTime ) {
		if( getMaxAllowedOperations() != INFINITY ) {
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<SimulatableRefreshMessage>(
					this, 
					this, 
					currentEventTime + getRefreshInterval(), 
					getSimulator(), 
					new SimulatableRefreshMessage(),
					DefaultDiscreteScheduledEvent.INTERNAL));
		}
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
