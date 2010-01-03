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
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 */
	public SimulatableEvent(ISimulatable source) {
		_source = source;
	}
	
	@Override
	public ISimulatable getSimulatable() {
		return _source;
	}

}
