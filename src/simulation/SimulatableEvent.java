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
	protected int _time;
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 */
	public SimulatableEvent(ISimulatable source, int time) {
		_source = source;
		_time = time;
	}
	
	@Override
	public ISimulatable getSimulatable() {
		return _source;
	}

	@Override
	public int getEventTime() {
		return _time;
	}

}
