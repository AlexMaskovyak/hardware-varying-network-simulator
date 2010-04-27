package network.communication;

import network.routing.IAddress;

/**
 * Basic unit of transmission on the network.
 * @author Alex Maskovyak
 *
 */
public class Packet<T> 
		implements IPacket<T> {

/// Fields
	
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
	protected long _sequence;
	

/// Construction
	
	/**
	 * Default constructor.
	 * @param content to store in this packet.
	 * @param source address which created this packet.
	 * @param destination address which is to receive this packet.
	 * @param protocol which defines the type of handler.
	 * @param ttl time to live, max hops before being trashed.
	 * @param sequence number in a series of packets.
	 */
	public Packet(T content, IAddress source, IAddress destination, String protocol, int ttl, long sequence) {
		_content = content;
		_source = source;
		_destination = destination;
		_protocol = protocol;
		_ttl = ttl;
		_sequence = sequence;
	}


/// Accessors/Mutators
	
	/* (non-Javadoc)
	 * @see network.IPacket#getContent()
	 */
	public T getContent() {
		return _content;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IPacket#setContent(java.lang.Object)
	 */
	@Override
	public void setContent(T content) {
		_content = content;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getSource()
	 */
	public IAddress getSource() {
		return _source;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IPacket#setSource(network.routing.IAddress)
	 */
	@Override
	public void setSource(IAddress source) {
		_source = source;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getDestination()
	 */
	public IAddress getDestination() {
		return _destination;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IPacket#setDestination(network.routing.IAddress)
	 */
	@Override
	public void setDestination(IAddress destination) {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getProtocol()
	 */
	public String getProtocol() {
		return _protocol;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IPacket#setProtocol(java.lang.String)
	 */
	@Override
	public void setProtocol(String protocol) {
		_protocol = protocol;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getTTL()
	 */
	public int getTTL() {
		return _ttl;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IPacket#setTTL(int)
	 */
	@Override
	public void setTTL( int ttl ) {
		_ttl = ttl;
	}
	
	/* (non-Javadoc)
	 * @see network.IPacket#getSequence()
	 */
	public long getSequence() {
		return _sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see network.communication.IPacket#setSequence(int)
	 */
	@Override
	public void setSequence(long sequence) {
		_sequence = sequence;
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
	
	
/// IPublicCloneable
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return 
			new Packet( 
				this.getContent(), 
				this.getSource(), 
				this.getDestination(), 
				this.getProtocol(), 
				this.getTTL(), 
				this.getSequence() );
	}
}
