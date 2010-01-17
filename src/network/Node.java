package network;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;
import simulation.AbstractSimulatable;

import network.IConnection;
import network.IData;
import network.INode;


/**
 * Basic simulatable network component.  Serves as an information source and sink.  Can be "physically" connected to a IConnection.
 * @author Alex Maskovyak
 *
 */
public class Node extends AbstractSimulatable implements INode, ISimulatable {

	protected enum State { RECEIVED, SENT, GOT_TICK, HANDLED_TICK, IDLE };
	
	protected Set<IConnection> _connections;
	protected RoutingTable _routingTable;
	
	protected Queue<IData> _bufferIn;
	protected Queue<IData> _bufferOut;
	
	protected String _id;
	
	protected State _currentState; 
	
	/**
	 * Default constructor.
	 */
	public Node() {
		this(UUID.randomUUID().toString());
	}
	
	/**
	 * Constructor allowing id specification.
	 * @param id to assign this node.
	 */
	public Node(String id) {
		super();
		_id = id;
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		super.init();
		_connections = new HashSet<IConnection>();
		_bufferIn = new LinkedList<IData>();
		_bufferOut = new LinkedList<IData>();
		_currentState = State.IDLE;
		_routingTable = new RoutingTable();
	}
	
	@Override
	public String getId() {
		return _id;
	}
	
	@Override
	public void registerConnection(IConnection connect) {
		_connections.add(connect);
		connect.connect(this);
	}

	public void unregisterConnection(IConnection connect) {
		_connections.remove(connect);
		connect.disconnect(this);
	}
	
	@Override
	public void receive(IData data) {
		_bufferIn.offer(data);
		_bufferOut.offer(_bufferIn.poll());
		// notify listeners that we've received data
		_currentState = State.RECEIVED;
		notify(new NodeSimulatableEvent(this, -1, "Got data.", data, null));
		_currentState = State.IDLE;
	}

	@Override
	public void receive(IData data, IConnection connection) {
		receive(data);
	}
	
	public void send() {
		send(_bufferOut.poll());
	}
	
	@Override
	public void send(IData data) {
		for( IConnection connection : _connections ) {
			send(data, connection);
		}
	}

	@Override
	public void send(IData data, IConnection connection) {
		connection.receive(this, data);
		// notify listeners that we sent data.
		_currentState = State.SENT;
		notify(new NodeSimulatableEvent(this, -1, "Sent data.", null, data));
		_currentState = State.IDLE;
	}

	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		// notify that we've gotten a tick
		_currentState = State.GOT_TICK;
		notify(new NodeSimulatableEvent(this, o.getTime(), "Got tick.", null, null));
		
		// here is where we would do something, like perhaps move some data along a connection
		// send outbound data across links
		if( !_bufferOut.isEmpty() ) {
			send();
		}
		
		// call Simulatable's to distribute the fact that we've handled it
		_currentState = State.HANDLED_TICK;
		notify(new NodeSimulatableEvent(this, o.getTime(), "Handled tick.", null, null));
		_currentState = State.IDLE;
	}

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
}

