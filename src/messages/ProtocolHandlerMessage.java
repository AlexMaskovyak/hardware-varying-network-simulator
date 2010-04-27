package messages;

import network.communication.IPacket;
import network.communication.IProtocolHandler;
import simulation.event.IDiscreteScheduledEvent.IMessage;

/**
 * Contains messages to be passed to ProtocolHandlers.
 * @author Alex Maskovyak
 *
 */
public class ProtocolHandlerMessage 
		implements IMessage {

/// Class variables
	
	/** 
	 * Corresponds to receive a packet from a lower protocol or a higher 
	 * protocol.
	 * @author Alex Maskovyak
	 */
	public static enum TYPE { HANDLE_LOWER, HANDLE_HIGHER };
	

/// Fields
	
	/** type of this message. */
	protected TYPE _type;
	/** packet to handle. */
	protected IPacket _packet;
	/** the one who called us. */
	protected IProtocolHandler _caller;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param type either higher or lower, who is sending it, relative to us.
	 * @param packet to handle.
	 * @param caller who called us.
	 */
	public ProtocolHandlerMessage( 
			ProtocolHandlerMessage.TYPE type, 
			IPacket packet, 
			IProtocolHandler caller ) {
		_type = type;
		_packet = packet;
		_caller = caller;
	}


/// Accessors/Mutators

	/**
	 * Obtains the type of this message.
	 * @return the type of this message.
	 */
	public TYPE getType() {
		return _type;
	}

	/**
	 * Obtains the packet to handle.
	 * @return the packet to handle.
	 */
	public IPacket getPacket() {
		return _packet;
	}

	/**
	 * Obtains the handler that called us.
	 * @return the handler that called us.
	 */
	public IProtocolHandler getCaller() {
		return _caller;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("ProtocolHandlerMessage [%s %s %s]", getType(), getPacket(), getCaller() );
	}
}
