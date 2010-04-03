package simulation;

/**
 * Event created by a Simulator.
 * @author Alex Maskovyak
 *
 */
public class SimulatorEvent 
		implements ISimulatorEvent {

	/** source of this event. */
	protected ISimulator _source;
	/** time of this event. */
	protected double _time;
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 */
	public SimulatorEvent(ISimulator source, double time) {
		_source = source;
		_time = time;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatorEvent#getSimulator()
	 */
	@Override
	public ISimulator getSimulator() {
		return _source;
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatorEvent#getTime()
	 */
	@Override
	public double getEventTime() {
		return _time;
	}
}