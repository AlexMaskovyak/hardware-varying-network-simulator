package simulation;

import java.util.Comparator;

/**
 * For use with the DiscreteScheduleEventSimulator.  This will schedule events to occur at clock ticks.
 * @author Alex Maskovyak
 *
 */
public interface IDiscreteScheduledEvent {

	public void execute();
	
	public int getTime();
}

class DiscreteScheduleEventComparator<T extends IDiscreteScheduledEvent> implements Comparator<T> {

	public int compare(IDiscreteScheduledEvent arg0, IDiscreteScheduledEvent arg1) {
		if( arg0.getTime() < arg1.getTime() ) { return -1; }
		if( arg0.getTime() > arg1.getTime() ) { return 1; }
		return 0;
	}
	
}
