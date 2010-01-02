package simulation;
import java.util.EventListener;

/**
 * Simulatable object for a simulator.
 * @author Alex Maskovyak
 *
 */
public interface ISimulatable extends EventListener {

	/**
	 * 
	 * @param listener
	 */
	public void addListener(ISimulatableListener listener);
	
	/**
	 * 
	 * @param listener
	 */
	public void removeListener(ISimulatableListener listener);
	
	/**
	 * Signals for operations to occur.
	 * @param o 
	 */
	public void handleTickEvent(ISimulatorEvent o);
}
