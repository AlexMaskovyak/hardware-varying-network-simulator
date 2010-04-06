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
	protected IMessage _message;
	/** address to send to. */
	protected IAddress _address;
	/** protocol requesting the send. */
	protected String _protocol;
	
	/**
	 * Default constructor.
	 * @param packet for ConnectionAdaptorManager to handle.
	 */
	public NodeOutMessage( IMessage message, IAddress address, String protocol ) {
		_message = message;
		_address = address;
		_protocol = protocol;
	}
	
	/**
	 * Get the packet.
	 * @return data to handle.
	 */
	public IMessage getMessage() {
		return _message;
	}
	
	/**
	 * Get the destination.
	 * @return destination for this data.
	 */
	public IAddress getAddress() {
		return _address;
	}
	
	/**
	 * Get the protocol.
	 * @return protocol who is to handle this.
	 */
	public String getProtocol() {
		return _protocol;
	}
}
