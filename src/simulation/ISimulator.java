package simulation;

/**
 * Discrete event simulator which delivers time events to registered simulatables.
 * 
 * @author Alex Maskovyak
 *
 */
public interface ISimulator {

/// listener management
	/**
	 * Adds an event listener.
	 * @param simulatorListener to add.
	 */
	public void addListener(ISimulatorListener simulatorListener);
	
	/**
	 * Removes an event listener.
	 * @param simulatorListener to remove.
	 */
	public void removeListener(ISimulatorListener simulatorListener);

	
/// simulatable management
	
	/**
	 * Registers a simulatable with this simulator.
	 * @param simulatable to add to the simulator and its future simulations.
	 */
	public void registerSimulatable(ISimulatable simulatable);
	
	/**
	 * Unregisters a simulatable with this simulator.
	 * @param simulatable to remove from the simulator and to exclude from future simulations.
	 */
	public void unregisterSimulatable(ISimulatable simulatable);

	/**
	 * Alerts the simulator that a simulatable has completed all operations possible for its time event.
	 * @param simulatable alerting the simulator.
	 */
	public void signalDone(ISimulatable simulatable);
	
	public void fireEvent(ISimulatorEvent o);
	
	public void simulate(int time);
	
	public void start();
	
	public void step();
	
	public void pause();
	
	public void stop();

}
