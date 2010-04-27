package messages;

import network.communication.IPacket;
import simulation.event.IDiscreteScheduledEvent.IMessage;

/**
 * Messages for ConnectionMedium.
 * @author Alex Maskovyak
 *
 */
public class ConnectionMediumMessage 
		extends ConnectionAdaptorMessage
		implements IMessage {

	/**
	 * Default constructor.
	 * @param packet
	 */
	public ConnectionMediumMessage(IPacket packet) {
		super(packet);
	}

	/*
	 * (non-Javadoc)
	 * @see messages.ConnectionAdaptorMessage#toString()
	 */
	@Override
	public String toString() {
		return String.format("MediumMessage %s", getPacket());
	}
}
