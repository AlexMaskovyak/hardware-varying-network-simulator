package simulation;

import java.util.EventListener;

/**
 * Interface for listening to simulator events.
 * 
 * @author Alex Maskovyak
 *
 */
public interface ISimulatorListener extends EventListener {

	/**
	 * ISimulatable newly registered to this simulator.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void simulatableRegisteredUpdate(ISimulatableEvent e);
	
	/**
	 * ISimulatable unregistered fromt his simulator.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void simulatableUnregisteredUpdate(ISimulatableEvent e);
	
	/**
	 * Time increment event.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void tickUpdate(ISimulatorEvent e);
	
	/**
	 * Simulator completing a single step of simulation (tick event and all simulatables completing operation.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void stepUpdate(ISimulatorEvent e);
	
	/**
	 * Simulation completing all steps.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void simulatedUpdate(ISimulatorEvent e);
	
	/**
	 * Simulator was started.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void startUpdate(ISimulatorEvent e);
	
	/**
	 * Simulator was paused.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void pauseUpdate(ISimulatorEvent e);
	
	/**
	 * Simulator was resumed from a pause.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void resumeUpdate(ISimulatorEvent e);
}
