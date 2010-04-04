package messages;

import network.IData;
import routing.IAddress;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages designed for Node.
 * @author Alex Maskovyak
 *
 */
public class NodeOutMessage implements IMessage {
	
	/** data to handle. */
	protected IData _data;
	/** address to send to. */
	protected IAddress _address;
	
	/**
	 * Default constructor.
	 * @param packet for ConnectionAdaptorManager to handle.
	 */
	public NodeOutMessage( IData data, IAddress address ) {
		_data = data;
		_address = address;
	}
	
	/**
	 * Get the packet.
	 * @return data to handle.
	 */
	public IData getData() {
		return _data;
	}
	
	/**
	 * Get the destination.
	 * @return destination for this data.
	 */
	public IAddress getAddress() {
		return _address;
	}
}
