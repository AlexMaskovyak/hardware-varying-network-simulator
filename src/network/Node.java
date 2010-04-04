package network;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import routing.IAddress;
import simulation.DefaultDiscreteScheduledEvent;
import simulation.DiscreteScheduledEventSimulator;
import simulation.IDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEventSimulator;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.ISimulatorEvent;
import simulation.AbstractSimulatable;
import simulation.IDiscreteScheduledEvent.IMessage;

import messages.AlgorithmMessage;
import messages.ConnectionAdaptorManagerInMessage;
import messages.ConnectionAdaptorManagerOutMessage;
import messages.NodeInMessage;
import messages.NodeOutMessage;
import network.INode;


/**
 * Basic simulatable network component.  Serves as an information source and 
 * sink.  Can be "physically" connected to a IConnectionMedium through its
 * adaptor.
 * @author Alex Maskovyak
 *
 */
public class Node 
		extends AbstractProtocolHandler<IPacket> 
		implements INode, ISimulatable, Comparable<INode> {

/// Fields	
	
	/** holds possible Node states, used for communication with listeners. */
	protected enum State { RECEIVED, SENT, GOT_TICK, HANDLED_TICK, IDLE };
	/** the current state of this node. */
	protected State _currentState; 
	
	/** address of this Node. */
	protected IAddress _address;
	/** manages all of our connections. */
	protected ConnectionAdaptorManager _manager;

/// Construction.
	
	/**
	 * Default constructor.
	 */
	public Node() {
		super();
	}
	
	/**
	 * Constructor allowing id specification.
	 * @param address to assign this node.
	 */
	public Node(IAddress address) {
		super();
		_address = address;
		_manager.setAddress(address);
		_manager.install(this, AbstractProtocolHandler.DEFAULT_PROTOCAL);
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		super.init();
		_currentState = State.IDLE;
		_manager = new ConnectionAdaptorManager();
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
	 * @see network.INode#receive(network.IPacket)
	 */
	@Override
	public void receive(IPacket packet) {
		System.out.printf("%s Node Receives from below %s\n", getAddress(), packet);
		
		// notify listeners that we've received data
		_currentState = State.RECEIVED;
		notify(new NodeSimulatableEvent(this, -1, "Got data.", packet));
		
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
		IPacket<Object> packet = new Packet<Object>(data, getAddress(), address, "algorithm", 5, 5);
		// send it down the stack
		
		getSimulator().schedule(
			new DefaultDiscreteScheduledEvent(
				this,
				_manager,
				getSimulator().getTime(),
				getSimulator(),
				new ConnectionAdaptorManagerOutMessage(data, address)));
		_manager.receive(packet);
		
		notify(new NodeSimulatableEvent(this, -1, "Sent data.", packet));
		_currentState = State.IDLE;
	}

	/*
	 * (non-Javadoc)
	 * @see network.INode#addAdaptor(network.IConnectionAdaptor)
	 */
	@Override
	public void addAdaptor(IConnectionAdaptor adaptor) {
		_manager.addConnectionAdaptor(adaptor);
	}

	/*
	 * (non-Javadoc)
	 * @see network.INode#removeAdaptor(network.IConnectionAdaptor)
	 */
	@Override
	public void removeAdaptor(IConnectionAdaptor adaptor) {
		_manager.removeConnectionAdaptor(adaptor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.INode#getAdaptors()
	 */
	@Override
	public Collection<IConnectionAdaptor> getAdaptors() {
		return _manager.getConnectionAdaptors();
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
		_manager.setSimulator( simulator );
	}
	
	boolean doit = true;
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleEvent(simulation.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		if( message instanceof NodeOutMessage ) {
			//if( !doit ) { return; } 
			//doit = false;
			NodeOutMessage nodeMessage = ((NodeOutMessage)message);
			IAddress destination = nodeMessage.getAddress();
			IData data = nodeMessage.getData();
			getSimulator().schedule( 
				new DefaultDiscreteScheduledEvent<ConnectionAdaptorManagerInMessage>(
					this, 
					_manager, 
					e.getEventTime() + .00001, 
					e.getSimulator(), 
					new ConnectionAdaptorManagerInMessage(
						new Packet(
							data, 
							getAddress(),
							destination,
							"DISTR_ALGORITHM",
							-1,
							-1 ) ) ));
		} else if( message instanceof NodeInMessage ) {
			System.out.println("GOT node in");
			NodeInMessage nodeMessage = ((NodeInMessage)message);
			String protocol = nodeMessage.getProtocol();
			IData data = nodeMessage.getData();
			AbstractProtocolHandler handler = getHandler( protocol );
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<AlgorithmMessage>(
					this, 
					handler, 
					getSimulator().getTime() + .0001, 
					getSimulator(),
					new AlgorithmMessage(data)));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#handleTickEvent(simulation.ISimulatorEvent)
	 */
	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		// notify that we've gotten a tick
		_currentState = State.GOT_TICK;
		notify(new NodeSimulatableEvent(this, o.getEventTime(), "Got tick.", null));
		
		// here is where we would do something, like perhaps move some data along a connection
		// send outbound data across links
//		if( !_bufferOut.isEmpty() ) {/
//			send();
//		}
		
		// call Simulatable's to distribute the fact that we've handled it
		_currentState = State.HANDLED_TICK;
		notify(new NodeSimulatableEvent(this, o.getEventTime(), "Handled tick.", null));
		super.handleTickEvent(o);
		_currentState = State.IDLE;
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractSimulatable#notify(simulation.ISimulatableEvent)
	 */
	@Override
	public void notify(ISimulatableEvent e) {
		// copy the set and enumerate over that so that listeners can unregister themselves
		// as a part of their computation
		Set<ISimulatableListener> _listenersCopy = new HashSet<ISimulatableListener>(_listeners);
		for( ISimulatableListener listener : _listenersCopy ) {
			switch(_currentState) {
				case GOT_TICK: listener.tickReceivedUpdate(e); break;
				case HANDLED_TICK: listener.tickHandledUpdate(e); break;
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
	public String getProtocal() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see network.AbstractProtocolHandler#handle(java.lang.Object)
	 */
	@Override
	public void handle(IPacket packetLikeObject) {
		receive( packetLikeObject );
	}
}