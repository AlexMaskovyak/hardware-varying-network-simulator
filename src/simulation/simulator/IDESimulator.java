package simulation.simulator;

import simulation.event.IDEvent;

/**
 * Interface for a Discrete Event Simulator.  It has all of the functionality of
 * an ISimulator but also allows for events to be scheduled and delivered to
 * destinations a particular time events.
 * @author Alex Maskovyak
 *
 */
public interface IDESimulator extends ISimulator, Runnable {

	/**
	 * Adds a IDiscreteScheduledEvent to the priority queue.
	 * @param event to be scheduled.
	 */
	public abstract void schedule(IDEvent event);

	/**
	 * Runs the main scheduling and dispatch thread.
	 */
	public abstract void run();
}