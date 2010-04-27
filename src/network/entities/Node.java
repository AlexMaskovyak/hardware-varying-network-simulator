package network.entities;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulator.ISimulator;

import messages.ProtocolHandlerMessage;
import network.communication.AbstractProtocolHandler;
import network.communication.Address;
import network.communication.IProtocolHandler;
import network.communication.NetworkProtocolHandler;
import network.communication.IPacket;
import network.communication.TransportProtocolHandler;
import network.listeners.NodeSimulatableEvent;
import network.listeners.NodeSimulatableListener;
import network.routing.IAddress;


/**
 * Basic simulatable network component.  Serves as an information source and 
 * sink.  Can be "physically" connected to a IConnectionMedium through its
 * adaptor.
 * @author Alex Maskovyak
 *
 */
public class Node 
		extends AbstractProtocolHandler<IPacket, IPacket> 
		implements INode, ISimulatable, Comparable<INode>, IPublicCloneable {

/// Fields	
	
	/** holds possible Node states, used for communication with listeners. */
	protected enum State { RECEIVED, SENT, GOT_TICK, HANDLED_TICK, IDLE };
	/** the current state of this node. */
	protected State _currentState; 
	
	/** address of this Node. */
	protected IAddress _address;
	/** manages transportation. */
	protected TransportProtocolHandler _transport;
	/** manages all of our connections. */
	protected NetworkProtocolHandler _networkHandler;
	/** baseline Adapator to clone for new connections. */
	protected IConnectionAdaptor _baseAdaptor;

	
/// Construction.
	
	/**
	 * Default constructor.
	 */
	public Node() {
		this( new Address(0) );
	}
	
	/**
	 * Constructor allowing id specification.
	 * @param address to assign this node.
	 */
	public Node(IAddress address) {
		super();
		setAddress( address );
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		super.init();
		_currentState = State.IDLE;
		_networkHandler = new NetworkProtocolHandler();
		_transport = new TransportProtocolHandler();
		_networkHandler.installHigherHandler( _transport ); //, AbstractProtocolHandler.DEFAULT_PROTOCAL );
		_transport.installLowerHandler( _networkHandler );
	}

	
/// INode
	
	/*
	 * (non-Javadoc)
	 * @see network.INode#getAddress()
	 */
	@Override
	public IAddress getAddress() {
		return _address;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.entities.INode#setAddress(network.routing.IAddress)
	 */
	@Override
	public void setAddress( IAddress address ) {
		_address = address;
		_transport.setAddress( address );
		_networkHandler.setAddress( address );
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.INode#receive(network.IPacket)
	 */
	@Override
	public void receive(IPacket packet) {
		// notify listeners that we've received data
		_currentState = State.RECEIVED;
		notifyListeners(new NodeSimulatableEvent(this, -1, "Got data.", packet));
		
		_currentState = State.IDLE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.INode#send(network.Data, network.IAddress)
	 */
	@Override
	public void send(Object data, IAddress address) {
		// notify listeners that we sent data.
		_currentState = State.SENT;
		
		/*_transport.send( data, address );
		// send it down the stack
		sendEvent( 
			(ISimulatable) getLowerHandler(), 
			new ProtocolHandlerMessage( 
				ProtocolHandlerMessage.TYPE.HANDLE_HIGHER, 
				new Packet( data, getAddress(), address, "algorithm", -1, -1 ),
				this ) );
		 */
		//notifyListeners(new NodeSimulatableEvent(this, -1, "Sent data.", packet));
		_currentState = State.IDLE;
	}

	/*
	 * (non-Javadoc)
	 * @see network.INode#addAdaptor(network.IConnectionAdaptor)
	 */
	@Override
	public void addAdaptor(IConnectionAdaptor adaptor) {
		_networkHandler.addConnectionAdaptor(adaptor);
	}

	/*
	 * (non-Javadoc)
	 * @see network.INode#removeAdaptor(network.IConnectionAdaptor)
	 */
	@Override
	public void removeAdaptor(IConnectionAdaptor adaptor) {
		_networkHandler.removeConnectionAdaptor(adaptor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.INode#getAdaptors()
	 */
	@Override
	public Collection<IConnectionAdaptor> getAdaptors() {
		return _networkHandler.getConnectionAdaptors();
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.INode#createNew(network.IAddress)
	 */
	@Override
	public INode createNew(IAddress address) {
		return new Node(address);
	}


/// ISimulatable

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#setSimulator(simulation.ISimulator)
	 */
	public void setSimulator(ISimulator simulator) {
		super.setSimulator( simulator );
		_networkHandler.setSimulator( simulator );
		_transport.setSimulator( simulator );
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		if( message instanceof ProtocolHandlerMessage ) {
			ProtocolHandlerMessage phMessage = (ProtocolHandlerMessage)message;
			switch( phMessage.getType() ) {
				
				case HANDLE_HIGHER: handleHigher( phMessage.getPacket(), phMessage.getCaller() ); break;
				case HANDLE_LOWER: handleLower( phMessage.getPacket(), phMessage.getCaller() ); break;
				default: throw new RuntimeException( "Unsupported message type." );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#notify(simulation.ISimulatableEvent)
	 */
	@Override
	public void notifyListeners(ISimulatableEvent e) {
		// copy the set and enumerate over that so that listeners can unregister themselves
		// as a part of their computation
		Set<ISimulatableListener> _listenersCopy = new HashSet<ISimulatableListener>(_listeners);
		for( ISimulatableListener listener : _listenersCopy ) {
			listener.update( e );
			switch(_currentState) {
				case GOT_TICK: listener.update(e); break;
				case HANDLED_TICK: listener.update(e); break;
				case SENT: 
					if (listener instanceof NodeSimulatableListener) {
						((NodeSimulatableListener)listener).sendUpdate((NodeSimulatableEvent)e);
					}
					break;
				case RECEIVED: 
					if (listener instanceof NodeSimulatableListener) {
						((NodeSimulatableListener)listener).receiveUpdate((NodeSimulatableEvent)e);
					}
					break;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#canPerformOperation()
	 */
	@Override
	public boolean canPerformOperation() {
		return true;
	}
	
	
/// Comparable
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(INode node) {
		return getAddress().compareTo(node.getAddress());
	}

// IProtocolHandler
	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#getProtocal()
	 */
	@Override
	public String getProtocol() {
		return null;
	}

	
// PublicCloneable 

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		return new Node();	
	}

	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleHigher(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleHigher(IPacket payload, IProtocolHandler sender) {
		/*NodeOutMessage nodeMessage = ((NodeOutMessage)message);
		IAddress destination = nodeMessage.getAddress();
		IMessage data = nodeMessage.getMessage();
		String protocol = nodeMessage.getProtocol();
		getSimulator().schedule( 
			new DefaultDiscreteScheduledEvent<ConnectionAdaptorManagerInMessage>(
				this, 
				_manager, 
				getSimulator().getTime() + getTransitTime(), 
				getSimulator(), 
				new ConnectionAdaptorManagerInMessage(
					new Packet(
						data, 
						getAddress(),
						destination,
						protocol,
						-1,
						-1 ) ) ));*/

	}

	/*
	 * (non-Javadoc)
	 * @see network.communication.AbstractProtocolHandler#handleLower(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public void handleLower(IPacket message, IProtocolHandler sender) {
		/*NodeInMessage nodeMessage = ((NodeInMessage)message);
		String protocol = nodeMessage.getProtocol();
		IMessage data = nodeMessage.getMessage();
		AbstractProtocolHandler handler = getHandler( protocol );
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent(
				this, 
				handler, 
				getSimulator().getTime() + .0001, 
				getSimulator(),
				data));*/
	}
}