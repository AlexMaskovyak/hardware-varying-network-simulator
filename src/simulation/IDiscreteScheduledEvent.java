package simulation;

import java.util.Comparator;

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

/**
 * Comparator for Discrete Scheduled Event sorting.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
class DiscreteScheduleEventComparator<T extends IDiscreteScheduledEvent> 
		implements Comparator<T> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IDiscreteScheduledEvent arg0, IDiscreteScheduledEvent arg1) {
		if( arg0.getEventTime() < arg1.getEventTime() ) { return -1; }
		if( arg0.getEventTime() > arg1.getEventTime() ) { return 1; }
		if( arg0.getEventTime() == arg1.getEventTime() ) {
			if( arg0.getPriority() < arg1.getPriority() ) { return -1; }
			if( arg0.getPriority() > arg1.getPriority() ) { return 1; }
		}
		return 0;
	}
}