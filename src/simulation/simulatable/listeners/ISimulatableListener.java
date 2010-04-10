package simulation.simulatable.listeners;

/**
 * Interface for listening to simulatable's events.
 * 
 * @author Alex Maskovyak
 *
 */
public interface ISimulatableListener {

	/**
	 * Simulatable has experienced some sort of event, state change, etc.
	 * @param e event of which to be informed.
	 */
	public void update(ISimulatableEvent e);
	
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
