package network;

/**
 * Basic unit of transmission on the network.
 * @author Alex Maskovyak
 *
 */
public class Packet<T> {

	/** content of this packet.  Could possibly be another packet. */
	protected T _content;
	/** source of this packet. */
	protected Address _source;
	/** destination for this packet. */
	protected Address _destination;
	/** receiving protocol handler should register with this protocol. */
	protected String _protocol;
	
	/**
	 * Default cosntructor.
	 * @param content to store in this packet.
	 * @param source address which created this packet.
	 * @param destination address which is to receive this packet.
	 * @param protocol which defines the type of handler.
	 */
	public Packet(T content, Address source, Address destination, String protocol) {
		_content = content;
		_source = source;
		_destination = destination;
		_protocol = protocol;
	}
	
	
	public T getContent() {
		return _content;
	}
	
	public Address getSource() {
		return _source;
	}
	
	public Address getDestination() {
		return _destination;
	}
	
	public String getProtocol() {
		return _protocol;
	}
}
