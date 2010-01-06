package network;

import simulation.ISimulatable;
import simulation.SimulatableEvent;

public class NodeSimulatableEvent extends SimulatableEvent {

	protected String _event;
	
	/**
	 * Default constructor.
	 * @param source ISimulatable which generated this event.
	 * @param time during which this event occurred.
	 * @param event description of the event.
	 */
	public NodeSimulatableEvent(ISimulatable source, int time, String event) {
		super(source, time);
		_event = event;
	}

	
}
