package simulation.simulatable.listeners;

/**
 * Interface for listening to simulatable's events.
 * 
 * @author Alex Maskovyak
 *
 */
public interface ISimulatableListener<E extends ISimulatableEvent> {

	/**
	 * Simulatable has experienced some sort of event, state change, etc.
	 * @param e event of which to be informed.
	 */
	public void update(E e);
}
