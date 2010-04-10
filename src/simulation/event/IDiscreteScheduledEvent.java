package simulation.event;

import java.util.Comparator;

import simulation.simulatable.ISimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * For use with the DiscreteScheduleEventSimulator.
 * @author Alex Maskovyak
 *
 */
public interface IDiscreteScheduledEvent<T extends IDiscreteScheduledEvent.IMessage> 
		extends ISimulatableEvent, ISimulatorEvent {

	/** destination to receive the event. */
	public ISimulatable getDestination();
	
	/** operate upon the destination...as in, give them a message. */
	public T getMessage();
	
	/** priority of this event. */
	public int getPriority();
	
	/** labels a message, every simulatable should expect this. */
	public interface IMessage {}
}