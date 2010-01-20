package network;

/**
 * Allows for the transmission of information to Connections.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptor {

	/** allows for selection of next hop for a packet. */
	protected RoutingTable _table;
	/** address of this connection. */
	protected IAddress _address;
	/** one up the chain of packethandling... */
	protected PacketHandler _packetHandler;
	
	
	/**
	 * Default constructor.
	 * @param address of this adaptor.
	 */
	public ConnectionAdaptor(IAddress address) {
		_address = address;
	}

	/**
	 * Capture all constructor instantiations.
	 */
	protected void init() {
		_table = new RoutingTable();		
	}
	
	/**
	 * Obtains this adaptor's address.
	 * @return address of this adaptor.
	 */
	public IAddress getAddress() {
		return _address;
	}
	
	/**
	 * Installs this for a particular Node to a Connection.
	 * @param node
	 * @param connection
	 */
	public void install(INode node, IConnection connection) {
		
	}
	
	
	
	/**
	 * Receive a packet.
	 * @param packet
	 */
	public void receive(Packet packet) {
		// is this for us?
		if( getAddress().equals( packet.getDestination() ) ) {
			_packetHandler.handle(packet);
		} else {
			//_table.ge
		}
	}
}
