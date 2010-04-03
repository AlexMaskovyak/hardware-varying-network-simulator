package network;

import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.SimulatableEvent;

public class NodeSimulatableEvent extends SimulatableEvent implements ISimulatableEvent {

	protected String _event;
	protected IPacket _packet;
	
	/**
	 * Default constructor.
	 * @param source ISimulatable which generated this event.
	 * @param time during which this event occurred.
	 * @param event description of the event.
	 */
	public NodeSimulatableEvent(ISimulatable source, double time, String event, IPacket packet) {
		super(source, time);
		_event = event;
		_packet = packet;
	}

	/**
	 * Description of this event.
	 * @return event description.
	 */
	public String getEvent() {
		return _event;
	}
	
	/**
	 * Packet that caused this event.
	 * @return Packet that was sent or received.
	 */
	public IPacket getPacket() {
		return _packet;
	}
	
	/**
	 * Obtains the Node that caused this event to occur.
	 * @return Node associated with this event.
	 */
	public Node getNode() {
		return (Node)super.getSource();
	}
}
