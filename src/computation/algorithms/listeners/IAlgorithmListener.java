package computation.algorithms.listeners;


/**
 * Simulatable Listener for Algorithmic events.
 * @author Alex Maskovyak
 *
 */
public interface IAlgorithmListener {

	/**
	 * Be informed of the event.  Event includes source information.
	 * @param e event with which we are to be updated.
	 */
	public abstract void update(AlgorithmEvent e);
}
