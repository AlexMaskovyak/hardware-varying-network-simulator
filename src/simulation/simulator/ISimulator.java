package simulation.simulator;

import simulation.simulatable.ISimulatable;
import simulation.simulator.listeners.ISimulatorEvent;
import simulation.simulator.listeners.ISimulatorListener;

/**
 * Discrete event simulator which delivers time events to registered 
 * simulatables.
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

	/**
	 * Fires the provided event for all listeners to receive.
	 * @param o event that has occurred.
	 */
	public void notifyListeners(ISimulatorEvent o);
	
	
/// simulatable management
	
	/**
	 * Registers a simulatable with this simulator.
	 * @param simulatable to add to the simulator and its future simulations.
	 */
	public void registerSimulatable(ISimulatable simulatable);
	
	/**
	 * Unregisters a simulatable with this simulator.
	 * @param simulatable to remove from the simulator and to exclude from 
	 * future simulations.
	 */
	public void unregisterSimulatable(ISimulatable simulatable);

	
/// simulation operation	

	/**
	 * Simulate up to the specified time and then pause operation.
	 * @param time steps to simulate.
	 */
	public void simulate(double time);
	
	/**
	 * Place the Simulator into the start state, necessary to begin simulation.
	 */
	public void start();
	
	/**
	 * Place the Simulator into the pause state to halt simulation until resumed.
	 */
	public void pause();
	
	/**
	 * Places the Simulator into the resumed state, allowing the simulator to 
	 * continue simulating from the point at which it was paused.
	 */
	public void resume();
	
	/**
	 * Permanently halts the current simulation.
	 */
	public void stop();

	/**
	 * Proceeds with the simulation for the amount of time specified.  Updates 
	 * time.
	 * @param step amount of time to step.
	 */
	public void step(double step);
	
	/**
	 * Obtains the current simulation time.
	 * @return current simulator time.
	 */
	public double getTime();
}
