package network.entities;

import network.communication.AbstractProtocolHandler;
import network.communication.NetworkProtocolHandler;
import network.communication.IPacket;
import network.communication.IProtocolHandler;
import network.routing.IAddress;
import messages.ConnectionAdaptorMessage;
import messages.ConnectionMediumMessage;
import messages.ProtocolHandlerMessage;

import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;

/**
 * Allows for the transmission of information to ConnectionAdaptors.
 * @author Alex Maskovyak
 *
 */
public class ConnectionAdaptor 
		extends AbstractProtocolHandler<IPacket<IPacket>, IPacket<IPacket>>
		implements IConnectionAdaptor<IPacket<IPacket>, IPacket<IPacket>>, ISimulatable {

// Fields
	
	/** address of this connection. */
	protected IAddress _address;
	/** manager in charge of us. */
	protected NetworkProtocolHandler _manager;
	/** node we're connected to. */
	protected INode _node;
	/** connection medium. */
	protected IConnectionMedium _medium;
	/** the protocal we should be installed to another handler as... */
	protected static String DEFAULT_PROTOCAL = "DEFAULT";
	
	
// Construction. 
	
	/** Constructor. */
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
		_protocol = ConnectionAdaptor.DEFAULT_PROTOCAL; 
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
	public void send(IPacket packet) {
		handleHigher( packet, null );
	}
	
	/* (non-Javadoc)
	 * @see network.IConnectionAdaptor#receive(network.IPacket)
	 */
	@Override
	public void receive(IPacket packet) {
		// is this for us?
		if( getAddress().equals( packet.getDestination() ) ) {
			handleLower( packet, null );
		} else if ( getAddress().equals( packet.getSource() ) ) {
			// we're sending it
			handleHigher( packet, null );
		}
		// there is no route discovery currently
		// everyone has a universal view, otherwise we might update our table in
		// this method
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleHigher(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleHigher(IPacket<IPacket> packet, IProtocolHandler caller) {
		if( _medium != null ) { 
			sendEvent( 
				(ISimulatable)getConnectedMedium(), new ConnectionMediumMessage( (IPacket)packet.clone() ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleLower(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleLower(IPacket<IPacket> packet, IProtocolHandler caller) {
		if ( getAddress().equals( packet.getDestination() ) ) {
			sendEvent( 
				(ISimulatable)getHigherHandler(), 
				new ProtocolHandlerMessage(
					ProtocolHandlerMessage.TYPE.HANDLE_LOWER, 
					(IPacket)packet.clone(), 
					(IProtocolHandler)this ), 
				.000001 );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEventDelegate(IDEvent e) {
		IMessage original = e.getMessage();
		
		if( original instanceof ProtocolHandlerMessage ) {
			ProtocolHandlerMessage message = (ProtocolHandlerMessage)original;
			switch( message.getType() ) {
			
				case HANDLE_HIGHER: handleHigher( message.getPacket(), message.getCaller() ); break;
				case HANDLE_LOWER: handleLower( message.getPacket(), message.getCaller() ); break;
				default: throw new RuntimeException( "Unsupported message type." );
			}
		} else if( original instanceof ConnectionAdaptorMessage ) {
			ConnectionAdaptorMessage message = (ConnectionAdaptorMessage)original;
			handleLower( message.getPacket(), null );
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
	
	
// PublicCloneable 

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		return new ConnectionAdaptor();	
	}


/// Display
	
	public String toString() {
		return String.format("Adaptor[%s]", getAddress());
	}
}
