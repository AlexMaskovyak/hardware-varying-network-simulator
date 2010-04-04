package messages;

import network.IPacket;
import simulation.IDiscreteScheduledEvent.IMessage;

/**
 * Messages designed for ConnectionAdaptor.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptorMessage implements IMessage {
	
	/** packet to handle. */
	protected IPacket<IPacket> _packet;
	
	/**
	 * Default constructor.
	 * @param packet for ConnectionAdaptorManager to handle.
	 */
	public ConnectionAdaptorMessage( IPacket packet ) {
		_packet = packet;
	}
	
	/**
	 * Get the packet.
	 * @return packet to handle.
	 */
	public IPacket getPacket() {
		return _packet;
	}
}

