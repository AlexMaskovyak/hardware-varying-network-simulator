package network.communication;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import network.entities.IConnectionAdaptor;
import network.routing.CentralRouter;
import network.routing.IAddress;
import network.routing.IRoutingTable;
import network.routing.RoutingTable;

import messages.ProtocolHandlerMessage;

import simulation.event.IDEvent;
import simulation.simulatable.ISimulatable;

/**
 * Manages the ConnectionAdaptors for a node.  Installed as a 
 * NodeSimulatableListener.
 * @author Alex Maskovyak
 *
 */
public class NetworkProtocolHandler 
		extends AbstractProtocolHandler<IPacket<IPacket>, IPacket<IPacket>> 
		implements IProtocolHandler<IPacket<IPacket>, IPacket<IPacket>>, ISimulatable {

/// Fields
	
	/** allows for selection of next hop for a packet. */
	protected IRoutingTable _table;
	/** all connection adaptors to be managed. */
	protected Set<IConnectionAdaptor> _adaptors;
	/** generic protocol used for typical addresses. */
	protected static String PROTOCAL = "NETWORK";
	/** address of this adaptor. */
	protected IAddress _address;
	
	/** default time tive for packets. */
	protected int _ttl;
	
/// Construction.
	
	/** Default constructor. */
	public NetworkProtocolHandler() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * @param address to assign to this adaptor manager, generally should be
	 * appropriate to this "level" of the network stack.
	 */
	public NetworkProtocolHandler(IAddress address) {
		super();
		setAddress(address);
	}
	
	/** Externalize instantiations. */
	protected void init() {
		super.init();
		_adaptors = new HashSet<IConnectionAdaptor>();
		_table = new RoutingTable();
		_protocol = NetworkProtocolHandler.PROTOCAL;
		setTTL( 10 );
		setTransitTime( .0000001 );
	}

/// Accessors / Mutators
	
	/**
	 * Obtains the address of this manager.
	 * @return address of this manager.
	 */
	public IAddress getAddress() {
		return _address;
	}
	
	/**
	 * Sets the address for this manager.
	 * @param address to assign to this address manager, generally should be 
	 * appropriate for this level of the stack or set to a universal routable
	 * address for the object which contains it.
	 */
	public void setAddress(IAddress address) {
		_address = address;
		_table = new RoutingTable(address);
		for( IConnectionAdaptor adaptor : _adaptors ) {
			adaptor.setAddress( address );
		}
	}
	
	/**
	 * Gets the time to live to install on new outgoing packets.
	 * @return ttl to install on new outgoing packets.
	 */
	public int getTTL() {
		return _ttl;
	}
	
	/**
	 * Sets the time to live to install on new outgoing packets.
	 * @param ttl to install on new outgoing packets.
	 */
	public void setTTL( int ttl ) {
		_ttl = ttl;
	}
	
	
/// Methods
	
	/**
	 * Adds a connection to manage.
	 * @param adaptor to manage.
	 */
	public void addConnectionAdaptor(IConnectionAdaptor adaptor) {
		_adaptors.add(adaptor);
		adaptor.installHigherHandler( this );
	}
	
	/**
	 * Removes a connection from management.
	 * @param adaptor to remove.
	 */
	public void removeConnectionAdaptor(IConnectionAdaptor adaptor) {
		_adaptors.remove(adaptor);
		adaptor.installHigherHandler( null );
	}

	/**
	 * Retrieves all adaptors being managed.  This collection has references to
	 * all adaptors being managed, but is not the actual collection in use by 
	 * this manager.  Changes made to adaptors themselves will be reflected, not
	 * changes to the collection composition.
	 * @return all managed connection adaptors.
	 */
	public Collection<IConnectionAdaptor> getConnectionAdaptors() {
		Collection<IConnectionAdaptor> copy = new HashSet<IConnectionAdaptor>();
		copy.addAll( _adaptors );
		return copy;
	}

	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.PerformanceRestrictedSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDEvent e) {
		ProtocolHandlerMessage message = (ProtocolHandlerMessage)e.getMessage();
		switch( message.getType() ) {
			
			case HANDLE_HIGHER: handleHigher( message.getPacket(), message.getCaller() ); break;
			case HANDLE_LOWER: handleLower( message.getPacket(), message.getCaller() ); break;
			default: throw new RuntimeException( "Unsupported message type." );
		}
	}
	
/// IPacketHandler
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleHigher(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleHigher(IPacket payload, IProtocolHandler sender) {
		// get destination
		IAddress destination = payload.getDestination();

		// get next hop
		IAddress nextHop = _table.getNextHop( destination );
		
		// create new packet
		IPacket outgoing = new Packet<IPacket>(payload, getAddress(), nextHop, NetworkProtocolHandler.PROTOCAL, getTTL(), 0);
		
		sendOut( outgoing, null );
	}

	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleLower(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleLower(IPacket<IPacket> message, IProtocolHandler sender) {
		if( getAddress().equals( message.getContent().getDestination() ) ) {
			sendEvent(
				(ISimulatable)getHigherHandler(),
				new ProtocolHandlerMessage(
					ProtocolHandlerMessage.TYPE.HANDLE_LOWER, 
					message.getContent(), 
					this ) );
			return;
		} 
		
		// let's make sure this can still live
		int ttl = message.getTTL() - 1;
		// if the ttl is less than 0, then it's ttd
		if( ttl < 0 ) { return; }
		
		// get destination
		IAddress destination = message.getContent().getDestination();

		// get next hop
		IAddress nextHop = _table.getNextHop( destination );
		IPacket<IPacket> newMessage = 
			new Packet(
				message.getContent(), 
				getAddress(), 
				nextHop,
				message.getProtocol(), 
				ttl, 
				-1 );
		
		sendOut( newMessage, sender );
	}
	
	/**
	 * Sends out the constructed packet to all adaptors, except if one of those
	 * adaptors happened to originate this packet.
	 * @param outgoing packet to send.
	 * @param sender who is not to receive the packet.
	 */
	protected void sendOut( IPacket outgoing, IProtocolHandler sender ) {
		for( IConnectionAdaptor ca : _adaptors ) {
			// don't send across the same originator of this message
			if( ca == sender ) { continue; }
			sendEvent(
				(ISimulatable)ca, 
				new ProtocolHandlerMessage( 
					ProtocolHandlerMessage.TYPE.HANDLE_HIGHER, 
					outgoing, 
					this ) );
		}
	}
	
	
/// Display
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format( "NetworkPH[%s]", getAddress() );
	}
	
/// Testing

	/**
	 * Driver/tester.
	 * @param args N/A
	 */
	public static void main(String... args) {
	   	IAddress n0 = new Address(0);
    	IAddress n1 = new Address(1);
    	IAddress n2 = new Address(2);
    	IAddress n3 = new Address(3);
    	IAddress n4 = new Address(4);
 
    	
    	//RoutesMap<INode> map = new SparseRoutesMap<INode>();
    	//DijkstraEngine<INode> engine = new DijkstraEngine<INode>(map);
    	CentralRouter router = CentralRouter.getInstance();
    	router.addBiDirectionalRoute(n0, n1, 1);
    	router.addBiDirectionalRoute(n1, n2, 1);
    	router.addBiDirectionalRoute(n0, n2, 1);
    	router.addBiDirectionalRoute(n2, n3, 1);
    	router.addBiDirectionalRoute(n3, n4, 1);
    	///map.addDirectRoute(start, end, cost)
    	//IAddress next = router.getNextHop(n0, n4);
    	
    	//System.out.println( next );
    	
    	Map<IAddress, String> test = new HashMap<IAddress, String>();
    	test.put(new Address(0), "set");
    	System.out.println( test.containsKey(new Address(0)) );
    	System.out.println((new Address(0)).equals(new Address(0)));
    	
    	IAddress next = router.getNextHop((IAddress)new Address(0), (IAddress)new Address(4));
    	
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n1, n0);
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n1, n1);
    	System.out.println( ">>>> next: " + next );

    	next = router.getNextHop(n1, n4);
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n2, n4);
    	System.out.println( ">>>> next: " + next );
    
    	next = router.getNextHop(n3, n4);
    	System.out.println( ">>>> next: " + next );

	}
}