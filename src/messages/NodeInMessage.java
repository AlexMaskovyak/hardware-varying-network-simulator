package messages;

import network.IData;
import routing.IAddress;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages designed for Node.
 * @author Alex Maskovyak
 *
 */
public class NodeInMessage implements IMessage {
	
	/** data to handle. */
	protected IData _data;
	/** address to send to. */
	protected String _protocol;
	
	/**
	 * Default constructor.
	 * @param packet for ConnectionAdaptorManager to handle.
	 */
	public NodeInMessage( IData data, String protocol ) {
		_data = data;
		_protocol = protocol;
	}
	
	/**
	 * Get the packet.
	 * @return data to handle.
	 */
	public IData getData() {
		return _data;
	}
	
	/**
	 * Get the protocol.
	 * @return destination for the data.
	 */
	public String getProtocol() {
		return _protocol;
	}
}
