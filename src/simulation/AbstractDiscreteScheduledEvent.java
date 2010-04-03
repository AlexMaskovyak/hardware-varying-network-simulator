package simulation;

/**
 * Base class for DiscretesScheduledEvents.
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractDiscreteScheduledEvent<T>
		implements IDiscreteScheduledEvent<T> {

/// Fields
	
	/** source */
	protected ISimulatable _source;
	/** destination. */
	protected ISimulatable _destination;
	/** event time */
	protected double _eventTime;
	/** simulator. */
	protected ISimulator _simulator;

	
/// Construction
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 * @param destination to receive and handle this event.
	 * @param eventTime during which this event occurs.
	 * @param simulator responsible for queueing and assigning this event.
	 */
	public AbstractDiscreteScheduledEvent( 
			ISimulatable source, 
			ISimulatable destination, 
			double eventTime, 
			ISimulator simulator ) {
		// assign
		_source = source;
		_destination = destination;
		_eventTime = eventTime;
		_simulator = simulator;
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
	public abstract T getMessage();
	
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
}
