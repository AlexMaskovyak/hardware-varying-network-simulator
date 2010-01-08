package simulation;

/**
 * Interface for listening to simulatable's events.
 * 
 * @author Alex Maskovyak
 *
 */
public interface ISimulatableListener {

	/**
	 * Simulatable has received a new tick.
	 * @param e
	 */
	public void tickReceivedUpdate(ISimulatableEvent e);
	
	/**
	 * Simulatable has responded to a new tick.
	 * @param e 
	 */
	public void tickHandledUpdate(ISimulatableEvent e);
}
