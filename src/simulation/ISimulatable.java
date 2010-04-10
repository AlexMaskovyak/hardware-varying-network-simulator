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
	 * Tests whether this ISimulatable can currently perform an operation or 
	 * not.
	 * @return true if an operation can be performed, false otherwise.
	 */
	public boolean canPerformOperation();
	
	/**
	 * Signals for operations to occur.
	 * @param e ISimulatorEvent describing the tick event that occurred.
	 */
	public void handleTickEvent(ISimulatorEvent e);
	
	/**
	 * Signals an event which may/may not trigger a response.
	 * @param e ISimulatableEvent which has occurred.
	 */
	public void handleEvent(IDiscreteScheduledEvent e);
	
	/**
	 * Obtains the simulator to which we are registered.
	 * @return simulator to which we are registered.
	 */
	public ISimulator getSimulator();
	
	/**
	 * Sets the simulator to which we are registered.
	 * @param simulator to which we are registered.
	 */
	public void setSimulator(ISimulator simulator);
	
	/**
	 * Notifies all listeners of the current Simulatable's state.
	 */
	public void notifyListeners();
	
	/**
	 * Notifies all listeners that an event has occurred.
	 * @param e ISimulatableEvent to disseminate.
	 */
	public void notifyListeners(ISimulatableEvent e);

	/**
	 * Gets the time it takes for us to send a message.  This time is equivalent
	 * to how far in the future our messages will be scheduled from the current
	 * time.
	 */
	public double getTransitTime();

	/**
	 * Sets the time it takes for us to send a message.
	 * @param transitTime is how long it takes for our message to reach a 
	 * destination.
	 */
	public void setTransitTime(double transitTime);
}
