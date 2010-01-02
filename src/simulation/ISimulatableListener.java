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
	public void tickUpdate(ISimulatableEvent e);
}
