package network;

import routing.IAddress;

/**
 * Basic unit of transmission on the network.
 * @author Alex Maskovyak
 *
 */
public class Packet<T> implements IPacket<T> {

	/** content of this packet.  Could possibly be another packet. */
	protected T _content;
	/** source of this packet. */
	protected IAddress _source;
	/** destination for this packet. */
	protected IAddress _destination;
	/** receiving protocol handler should register with this protocol. */
	protected String _protocol;
	/** time to live */
	protected int _ttl;
	/** number of this packet */
	protected int _sequence;
	
	
	/**
	 * Default constructor.
	 * @param content to store in this packet.
	 * @param source address which created this packet.
	 * @param destination address which is to receive this packet.
	 * @param protocol which defines the type of handler.
	 * @param ttl time to live, max hops before being trashed.
	 */
	public Packet(T content, IAddress source, IAddress destination, String protocol, int ttl, int sequence) {
		_content = content;
		_source = source;
		_destination = destination;
		_protocol = protocol;
		_ttl = ttl;
		_sequence = sequence;
	}
	
	
	/* (non-Javadoc)
	 * @see network.IPacket#getContent()
	 */
	public T getContent() {
		return _content;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getSource()
	 */
	public IAddress getSource() {
		return _source;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getDestination()
	 */
	public IAddress getDestination() {
		return _destination;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getProtocol()
	 */
	public String getProtocol() {
		return _protocol;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getTTL()
	 */
	public int getTTL() {
		return _ttl;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getSequence()
	 */
	public int getSequence() {
		return _sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("[ SRC:%s DST:%s PRT:%s TTL:%s SEQ:%s CONT:%S ]", 
				getSource(), 
				getDestination(), 
				getProtocol(),
				getTTL(),
				getSequence(),
				getContent());
	}
}
