package simulation;

/**
 * Base class for DiscretesScheduledEvents.
 * @author Alex Maskovyak
 *
 */
public class DefaultDiscreteScheduledEvent<T extends IDiscreteScheduledEvent.IMessage>
		implements IDiscreteScheduledEvent<T> {

/// Constants
	
	/** normal priority */
	public static final int EXTERNAL = 0;
	/** high priority, for those things which absolutely must be done first, 
	 *  like a wake-up message, or any message a simulatable sends itself, those
	 *  absolutely should occur first but should arrive at the same 'time' as 
	 *  other events. */
	public static final int INTERNAL = 1;
	
/// Fields
	
	/** source */
	protected ISimulatable _source;
	/** destination. */
	protected ISimulatable _destination;
	/** event time */
	protected double _eventTime;
	/** simulator. */
	protected ISimulator _simulator;
	/** message to give. */
	protected T _message;
	/** priority of the event. */
	protected int _priority;
	
/// Construction
	
	/**
	 * Default constructor, create with normal priority.
	 * @param source of this event.
	 * @param destination to receive and handle this event.
	 * @param eventTime during which this event occurs.
	 * @param simulator responsible for queueing and assigning this event.
	 * @param message to deliver.
	 */
	public DefaultDiscreteScheduledEvent( 
			ISimulatable source, 
			ISimulatable destination, 
			double eventTime, 
			ISimulator simulator,
			T message ) {
		this( source, destination, eventTime, simulator, message, EXTERNAL );
	}

	/**
	 * Default constructor, create with normal priority.
	 * @param source of this event.
	 * @param destination to receive and handle this event.
	 * @param eventTime during which this event occurs.
	 * @param simulator responsible for queueing and assigning this event.
	 * @param message to deliver.
	 * @param priority how valuable is this event, normally this is set to 
	 */
	public DefaultDiscreteScheduledEvent(
			ISimulatable source, 
			ISimulatable destination, 
			double eventTime, 
			ISimulator simulator,
			T message,
			int priority ) {
		
		// assign
		_source = source;
		_destination = destination;
		_eventTime = eventTime;
		_simulator = simulator;
		_message = message;
		_priority = priority;
		// create
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() { }

	
/// IDiscreteScheduledEvent
	
	/*
	 * (non-Javadoc)
	 * @see simulation.IDiscreteScheduledEvent#execute()
	 */
	@Override
	public T getMessage() {
		return _message;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.IDiscreteScheduledEvent#getDestination()
	 */
	@Override
	public ISimulatable getDestination() { return _destination; }
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatableEvent#getEventTime()
	 */
	@Override
	public double getEventTime() { return _eventTime; }
	
	/*
	 * (non-Javadoc)
	 * @see simulation.IDiscreteScheduledEvent#getPriority()
	 */
	@Override
	public int getPriority() { return _priority; }
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatableEvent#getSource()
	 */
	@Override
	public ISimulatable getSource() { return _source; }
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatorEvent#getSimulator()
	 */
	@Override
	public ISimulator getSimulator() { return _simulator; }

	
/// Utility
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("[ %s %s %f ]", 
			getSource(), 
			getDestination(), 
			getEventTime() );
	}
}
