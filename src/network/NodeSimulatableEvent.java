package network;

import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.SimulatableEvent;

public class NodeSimulatableEvent extends SimulatableEvent implements ISimulatableEvent {

	protected String _event;
	protected IData[] _dataReceived;
	protected IData[] _dataSent;
	
	/**
	 * Default constructor.
	 * @param source ISimulatable which generated this event.
	 * @param time during which this event occurred.
	 * @param event description of the event.
	 */
	public NodeSimulatableEvent(ISimulatable source, int time, String event, IData[] dataReceived, IData[] dataSent) {
		super(source, time);
		_event = event;
	}

	/**
	 * Description of this event.
	 * @return event description.
	 */
	public String getEvent() {
		return _event;
	}
	
	/**
	 * Data received during this event, null if not applicable.
	 * @return data received.
	 */
	public IData[] getDataReceived() {
		return _dataReceived;
	}
	
	/**
	 * Data send during this event, null if not applicable.
	 * @return data sent.
	 */
	public IData[] getDataSent() {
		return _dataSent;
	}
	
	/**
	 * Obtains the Node that caused this event to occur.
	 * @return Node associated with this event.
	 */
	public Node getNode() {
		return (Node)super.getSimulatable();
	}
}
