package simulation;

/**
 * Standard ISimulatableEvent implementation.
 * 
 * @author Alex Maskovyak
 *
 */
public class SimulatableEvent implements ISimulatableEvent {

	/** source of this event. */
	protected ISimulatable _source;
	/** Simulator time of this event. */
	protected double _time;
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 */
	public SimulatableEvent(ISimulatable source, double time) {
		_source = source;
		_time = time;
	}
	
	@Override
	public ISimulatable getSource() {
		return _source;
	}

	@Override
	public double getEventTime() {
		return _time;
	}

}
