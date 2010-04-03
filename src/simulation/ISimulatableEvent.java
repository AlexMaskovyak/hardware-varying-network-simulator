package simulation;

/**
 * Describes a Simulatable's event.
 * @author Alex Maskovyak
 *
 */
public interface ISimulatableEvent {

	/**
	 * Obtain the ISimulatable which generated this event.
	 * @return ISimulatable which generated this event.
	 */
	public ISimulatable getSource();
	
	/**
	 * Obtain the ISimulator time which generated this response event.
	 * @return numeric time which generated this event.
	 */
	public double getEventTime();
}
