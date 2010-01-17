package simulation;

public interface IDiscreteScheduledEventSimulator extends ISimulator {

	/**
	 * Adds a IDiscreteScheduledEvent to the priority queue.
	 * @param event to be scheduled.
	 */
	public abstract void schedule(IDiscreteScheduledEvent event);

	/**
	 * Runs the main scheduling and dispatch thread.
	 */
	public abstract void run();

}