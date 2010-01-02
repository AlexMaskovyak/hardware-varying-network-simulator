package simulation;

public class SimulatorEvent implements ISimulatorEvent {

	/** source of this event. */
	protected ISimulator _source;
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 */
	public SimulatorEvent(ISimulator source) {
		_source = source;
	}
	
	@Override
	public ISimulator getSimulator() {
		return _source;
	}
}
