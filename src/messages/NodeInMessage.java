package messages;

import computation.IData;

import network.routing.IAddress;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;

/**
 * Messages designed for Node.
 * @author Alex Maskovyak
 *
 */
public class NodeInMessage implements IMessage {
	
	/** event to handle. */
	protected IMessage _message;
	/** address to send to. */
	protected String _protocol;
	
	/**
	 * Default constructor.
	 * @param packet for ConnectionAdaptorManager to handle.
	 */
	public NodeInMessage( IMessage message, String protocol ) {
		_message = message;
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
	 * Get the protocol.
	 * @return destination for the data.
	 */
	public String getProtocol() {
		return _protocol;
	}
}
