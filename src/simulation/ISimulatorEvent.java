package simulation;

/**
 * ISimulator generated event object.
 * 
 * @author Alex Maskovyak
 *
 */
public interface ISimulatorEvent {

	/**
	 * Obtains the ISimulator that caused this event.
	 * @return ISimulator which caused the event.
	 */
	public ISimulator getSimulator();
	
	/**
	 * Obtains the numeric designation for this time event.
	 * @return the discrete time which identifies this event.
	 */
	public int getTime();
}
