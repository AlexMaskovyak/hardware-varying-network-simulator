package simulation;
import java.util.EventListener;

/**
 * Simulatable object for a simulator.
 * @author Alex Maskovyak
 *
 */
public interface ISimulatable extends EventListener {

	/**
	 * Adds an eventlistener.
	 * @param listener to add.
	 */
	public void addListener(ISimulatableListener listener);
	
	/**
	 * Removes an eventlistener.
	 * @param listener to remove.
	 */
	public void removeListener(ISimulatableListener listener);
	
	/**
	 * Signals for operations to occur.
	 * @param o ISimulatorEvent describing the tick event that occurred.
	 */
	public void handleTickEvent(ISimulatorEvent o);
}
