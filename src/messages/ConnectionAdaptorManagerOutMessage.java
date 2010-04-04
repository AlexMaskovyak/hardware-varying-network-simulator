package messages;

import network.IData;
import routing.IAddress;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages designed for ConnectionAdaptorManager.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptorManagerOutMessage implements IMessage {
	
	/** data to handle. */
	protected Object _data;
	/** destination for this data. */
	protected IAddress _destination;
	
	/**
	 * Default constructor.
	 * @param data for ConnectionAdaptorManager to handle.
	 * @param destination to which to send this data.
	 */
	public ConnectionAdaptorManagerOutMessage( Object data, IAddress destination ) {
		_data = data;
		_destination = destination;
	}
	
	/**
	 * Get the packet.
	 * @return packet to handle.
	 */
	public Object getData() {
		return _data;
	}
	
	/**
	 * 
	 * @return
	 */
	public IAddress getDestination() {
		return _destination;
	}
}
