package network.communication;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import network.entities.IConnectionAdaptor;
import network.entities.Node;
import network.routing.CentralRouter;
import network.routing.IAddress;
import network.routing.IRoutingTable;
import network.routing.RoutingTable;

import messages.ConnectionAdaptorManagerMessage;
import messages.ConnectionAdaptorManagerOutMessage;
import messages.ConnectionAdaptorMessage;
import messages.NodeInMessage;
import messages.NodeOutMessage;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulator.DESimulator;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * Manages the ConnectionAdaptors for a node.  Installed as a 
 * NodeSimulatableListener.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptorManager 
		extends AbstractProtocolHandler<IPacket<IPacket>> 
		implements IProtocolHandler<IPacket<IPacket>>, ISimulatable {

/// Fields
	
	/** allows for selection of next hop for a packet. */
	protected IRoutingTable _table;
	/** all connection adaptors to be managed. */
	protected Set<IConnectionAdaptor> _adaptors;
	/** generic protocol used for typical addresses. */
	protected static String PROTOCAL = "DEFAULT";
	/** address of this adaptor. */
	protected IAddress _address;
	
	
/// Construction.
	
	/** Default constructor. */
	public ConnectionAdaptorManager() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * @param address to assign to this adaptor manager, generally should be
	 * appropriate to this "level" of the network stack.
	 */
	public ConnectionAdaptorManager(IAddress address) {
		super();
		setAddress(address);
	}
	
	/** Externalize instantiations. */
	protected void init() {
		super.init();
		_adaptors = new HashSet<IConnectionAdaptor>();
		_table = new RoutingTable();
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
	}
	
/// Methods
	
	/**
	 * Adds a connection to manage.
	 * @param adaptor to manage.
	 */
	public void addConnectionAdaptor(IConnectionAdaptor adaptor) {
		_adaptors.add(adaptor);
		adaptor.install(this, ConnectionAdaptorManager.PROTOCAL);
	}
	
	/**
	 * Removes a connection from management.
	 * @param adaptor to remove.
	 */
	public void removeConnectionAdaptor(IConnectionAdaptor adaptor) {
		_adaptors.remove(adaptor);
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

	/**
	 * General receiver.
	 * @param packet to receive.
	 */
	public void receive(IPacket packet) {
		if( getAddress().equals( packet.getDestination() ) ) {
			// pass it up
			receiveFromBelow(packet);
		} else if ( getAddress().equals( packet.getSource() ) ) {
			// pass it down
			receiveFromAbove(packet);
		} else {
			// route
			receiveFromAbove(packet);
		}
	}
	
	/**
	 * Packet is outgoing.
	 * @param packet to route away from above us on the stack.
	 */
	public void receiveFromAbove(IPacket packet) {
		// get destination
		IAddress destination = packet.getDestination();

		// get next hop
		IAddress nextHop = _table.getNextHop( destination );
		
		// create new packet
		IPacket outgoing = new Packet<IPacket>(packet, getAddress(), nextHop, ConnectionAdaptorManager.PROTOCAL, 5, 0);
		for( IConnectionAdaptor ca : _adaptors ) {
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<ConnectionAdaptorMessage>(
					(ISimulatable)this,
					(ISimulatable)ca, 
					getSimulator().getTime() + .00001,
					getSimulator(),
					new ConnectionAdaptorMessage(outgoing)));
		}
	}
	
	/**
	 * Packet is incoming from somewhere else and must be delivered up the 
	 * stack.
	 * @param packet to deliver up the stack from below.
	 */
	public void receiveFromBelow(IPacket<IPacket> packet) {
		AbstractProtocolHandler handler = super.getHandler( packet.getProtocol() );
		if( handler instanceof Node ) {
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<NodeInMessage>(
					(ISimulatable)this, 
					(ISimulatable)handler, 
					getSimulator().getTime() + getTransitTime(), 
					getSimulator(), 
					new NodeInMessage(
						(IMessage)packet.getContent(), packet.getProtocol())));
		}
	}

	
/// ISimulatable
	
	/*
	 * (non-Javadoc)
	 * @see simulation.PerformanceRestrictedSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		if( message instanceof ConnectionAdaptorManagerMessage ) {
			IPacket packet = ((ConnectionAdaptorManagerMessage)message).getPacket();
			handle(((ConnectionAdaptorManagerMessage)message).getPacket());
		} 
	}
	
/// IPacketHandler
	
	/*
	 * (non-Javadoc)
	 * @see network.AbstractPacketHandler#handle(network.IPacket)
	 */
	@Override
	public void handle(IPacket<IPacket> packetLikeObject) {
		receive(packetLikeObject);
	}

	/*
	 * (non-Javadoc)
	 * @see network.AbstractPacketHandler#getProtocal()
	 */
	@Override
	public String getProtocol() {
		return ConnectionAdaptorManager.PROTOCAL;
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
    	//router.addBiDirectionalRoute(n1, n2, 1);
    	//router.addBiDirectionalRoute(n0, n2, 1);
    	//router.addBiDirectionalRoute(n2, n3, 1);
    	//router.addBiDirectionalRoute(n3, n4, 1);
    	///map.addDirectRoute(start, end, cost)
    	//IAddress next = router.getNextHop(n0, n4);
    	
    	//System.out.println( next );
    	
    	Map<IAddress, String> test = new HashMap<IAddress, String>();
    	test.put(new Address(0), "set");
    	System.out.println( test.containsKey(new Address(0)) );
    	System.out.println((new Address(0)).equals(new Address(0)));
    	
    	IAddress next = router.getNextHop((IAddress)new Address(0), (IAddress)new Address(1));
    	
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n1, n0);
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n1, n1);
    	System.out.println( ">>>> next: " + next );
	}
}