package messages;

import network.communication.IPacket;
import simulation.event.IDiscreteScheduledEvent.IMessage;

/**
 * Messages designed for ConnectionAdaptorManager.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptorManagerMessage implements IMessage {
	
	/** packet to handle. */
	protected IPacket<IPacket> _packet;
	
	/**
	 * Default constructor.
	 * @param packet for ConnectionAdaptorManager to handle.
	 */
	public ConnectionAdaptorManagerMessage( IPacket packet ) {
		_packet = packet;
	}
	
	/**
	 * Get the packet.
	 * @return packet to handle.
	 */
	public IPacket getPacket() {
		return _packet;
	}


/// Display
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format( "ConnectionAdaptorManagerIn %s", getPacket() );
	}
}
