package network;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import simulation.ISimulatable;
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

	protected Set<IConnection> _connections;
	
	protected Queue<IData> _bufferIn;
	protected Queue<IData> _bufferOut;
	
	protected String _id;
	
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
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		super.init();
		_connections = new HashSet<IConnection>();
		_bufferIn = new LinkedList<IData>();
		_bufferOut = new LinkedList<IData>();
		_listeners = new HashSet<ISimulatableListener>();
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
	}
	
	@Override
	public void receive(IData data) {
		_bufferIn.offer(data);
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
		connection.send(this, data);		
	}

	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		// here is where we would do something, like perhaps move some data along a connection
		
		// call Simulatable's to distribute the fact that we've handled it
		super.handleTickEvent(o);
	}

}

