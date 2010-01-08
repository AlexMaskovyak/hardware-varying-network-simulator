package simulation;

/**
 * For use with the DiscreteScheduleEventSimulator.  This will schedule events to occur at clock ticks.
 * @author Alex Maskovyak
 *
 */
public interface IDiscreteScheduledEvent {

	public void execute();
	
	public double getTime();
}
