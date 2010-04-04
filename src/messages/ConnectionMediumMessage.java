package messages;

import network.IPacket;
import simulation.IDiscreteScheduledEvent.IMessage;

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

}
