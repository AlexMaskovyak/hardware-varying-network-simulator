package network.entities;

import network.communication.AbstractProtocolHandler;
import network.communication.ConnectionAdaptorManager;
import network.communication.IPacket;
import network.communication.IProtocolHandler;
import network.routing.IAddress;
import network.routing.IRoutingTable;
import network.routing.RoutingTable;
import messages.ConnectionAdaptorMessage;
import messages.ConnectionMediumMessage;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.ISimulatable;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * Allows for the transmission of information to ConnectionAdaptors.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptor 
		extends AbstractProtocolHandler<IPacket<IPacket>>
		implements IConnectionAdaptor<IPacket<IPacket>>, ISimulatable {

// Fields
	
	/** allows for selection of next hop for a packet. */
	protected IRoutingTable _table;
	/** address of this connection. */
	protected IAddress _address;
	/** manager in charge of us. */
	protected ConnectionAdaptorManager _manager;
	/** node we're connected to. */
	protected INode _node;
	/** connection medium. */
	protected IConnectionMedium _medium;
	/** the protocal we should be installed to another handler as... */
	protected static String DEFAULT_PROTOCAL = "DEFAULT";
	
// Construction. 
	
	/**
	 * Constructor.
	 * @param node onto which this ConnectionAdaptor is installed.
	 * @param connection onto which we are to be connected for transmission.
	 * @param manager which is to manage this adaptor.
	 * @param address associated with this adapator.
	 */
	public ConnectionAdaptor() {
		super();
	}

	/**
	 * Capture all constructor instantiations.
	 */
	protected void init() {
		super.init();
		_address = null;
		_manager = null;
		_node = null;
		_medium = null;
		_table = new RoutingTable();
		setTransitTime( 1 );
		setMaxAllowedOperations( 5 );
		setRefreshInterval( 5 );
	}
	
	
// IConnectionAdaptor
	
	/* (non-Javadoc)
	 * @see network.IConnectionAdaptor#getAddress()
	 */
	@Override
	public IAddress getAddress() {
		return _address;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#setAddress(network.IAddress)
	 */
	@Override
	public void setAddress(IAddress address) {
		_address = address;
		_table = new RoutingTable( address );
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#getConnectedNode()
	 */
	public INode getConnectedNode() {
		return _node;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#setConnectedNode(network.INode)
	 */
	public void setConnectedNode(INode node) {
		_node = node;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#connect(network.IConnectionMedium)
	 */
	@Override
	public void connect(IConnectionMedium medium) {
		_medium = medium;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#disconnect(network.IConnectionMedium)
	 */
	@Override
	public void disconnect(IConnectionMedium medium) {
		_medium = null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#getConnectedMedium()
	 */
	@Override
	public IConnectionMedium getConnectedMedium() {
		return _medium;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IConnectionAdaptor#send(network.IPacket)
	 */
	@Override
	public void send(IPacket<IPacket> packet) {
		if( _medium != null ) { 
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<ConnectionMediumMessage>(
					(ISimulatable)this, 
					(ISimulatable)_medium, 
					getSimulator().getTime() + getTransitTime(), 
					getSimulator(), 
					new ConnectionMediumMessage( packet ) ) );
		}
	}
	
	/* (non-Javadoc)
	 * @see network.IConnectionAdaptor#receive(network.IPacket)
	 */
	@Override
	public void receive(IPacket<IPacket> packet) {
		// is this for us?
		if( getAddress().equals( packet.getDestination() ) ) {
			IProtocolHandler ph = _protocalMappings.get( packet.getProtocol() );
			ph.handle(packet.getContent());
		} else if ( getAddress().equals( packet.getSource() ) ) {
			// we're sending it
			send( packet );
		}
		// there is no route discovery currently
		// everyone has a universal view, otherwise we might update our table in
		// this method
	}
	
// IPacketHandler
	
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
		return ConnectionAdaptor.DEFAULT_PROTOCAL; 
	}

	/*
	 * 
	 */
	@Override
	public void handleEventDelegate(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		if( message instanceof ConnectionAdaptorMessage ) {
			ConnectionAdaptorMessage caMessage = ((ConnectionAdaptorMessage)message);
			handle( caMessage.getPacket() );
		}
	}
	
	
// Equals
	
	/**
	 * Allow equivalence testing with other IConnectionAdaptors.
	 */
	public boolean equals(IConnectionAdaptor adaptor) {
		return (adaptor instanceof ConnectionAdaptor)
			? equals((ConnectionAdaptor)adaptor)
			: false;
	}
	
	/**
	 * Naively adaptors are equal if they 
	 * @param adaptor
	 * @return
	 */
	public boolean equals(ConnectionAdaptor adaptor) {
		return 
			(this == adaptor) // reference equals
			|| 
			( getConnectedNode() == adaptor.getConnectedNode() ) // same node
			&& ( getAddress().equals( adaptor.getAddress() )	// equal address
			&& ( getConnectedMedium() == adaptor.getConnectedMedium() ) ); // same medium
	}
}
