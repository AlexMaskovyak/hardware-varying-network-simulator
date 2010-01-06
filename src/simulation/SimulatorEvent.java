package simulation;

public class SimulatorEvent implements ISimulatorEvent {

	/** source of this event. */
	protected ISimulator _source;
	/** time of this event. */
	protected int _time;
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 */
	public SimulatorEvent(ISimulator source, int time) {
		_source = source;
		_time = time;
	}
	
	@Override
	public ISimulator getSimulator() {
		return _source;
	}

	@Override
	public int getTime() {
		return _time;
	}
}
