package simulation.event;

import java.util.Comparator;

/**
 * Comparator for Discrete Scheduled Event sorting.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public class DEComparator<T extends IDEvent> 
		implements Comparator<T> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(IDEvent arg0, IDEvent arg1) {
		if( arg0.getEventTime() < arg1.getEventTime() ) { return -1; }
		if( arg0.getEventTime() > arg1.getEventTime() ) { return 1; }
		if( arg0.getEventTime() == arg1.getEventTime() ) {
			if( arg0.getPriority() < arg1.getPriority() ) { return -1; }
			if( arg0.getPriority() > arg1.getPriority() ) { return 1; }
		}
		return 0;
	}
}
