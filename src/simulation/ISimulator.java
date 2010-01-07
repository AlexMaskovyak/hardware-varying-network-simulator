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
	
	/**
	 * Fires the provided event for all listeners to receive.
	 * @param o event that has occurred.
	 */
	public void notify(ISimulatorEvent o);
	
	/**
	 * Simulate the specified number of steps for all simulatables.
	 * @param time steps to simulate.
	 */
	public void simulate(int time);
	
	/**
	 * Place the Simulator into the start state, necessary to begin simulation.
	 */
	public void start();
	
	/**
	 * Place the Simulator into the pause state to halt simulation until resumed.
	 */
	public void pause();
	
	/**
	 * Places the Simulator into the resumed state, allowing the simulator to continue simulating from the point at which it was paused.
	 */
	public void resume();
	
	/**
	 * Permanently halts the current simulation.
	 */
	public void stop();

	/**
	 * Proceeds with the next simulation time event.  Updates time.
	 */
	public void step();
}
