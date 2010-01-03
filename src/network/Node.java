package network;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import simulation.ISimulatable;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.ISimulatorEvent;
import simulation.SimulatableEvent;

import network.IConnection;
import network.IData;
import network.INode;


/**
 * Basic simulatable network component.  Serves as an information source and sink.  Can be "physically" connected to a IConnection.
 * @author Alex Maskovyak
 *
 */
public class Node implements INode, ISimulatable {

	protected Set<IConnection> _connections;
	protected Set<ISimulatableListener> _listeners;
	
	protected Queue<IData> _bufferIn;
	protected Queue<IData> _bufferOut;
	
	protected String _id;
	
	/**
	 * Default constructor.
	 */
	public Node() {
		init();
	}
	
	public Node(String id) {
		init();
		_id = id;
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		_connections = new HashSet<IConnection>();
		_id = UUID.randomUUID().toString();
		_bufferIn = new LinkedList<IData>();
		_bufferOut = new LinkedList<IData>();
		_listeners = new HashSet<ISimulatableListener>();
	}
	
	@Override
	public void addListener(ISimulatableListener listener) {
		_listeners.add(listener);
	}
	
	@Override
	public void removeListener(ISimulatableListener listener) {
		_listeners.remove(listener);
	}
	
	@Override
	public String getId() {
		return _id;
	}
	
	@Override
	public void connect(IConnection connect) {
		_connections.add(connect);
		connect.connect(this);
	}

	@Override
	public void receive(IData data) {
		_bufferIn.offer(data);
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
		// TODO Auto-generated method stub
		for( ISimulatableListener listener : _listeners ) {
			listener.tickUpdate(new SimulatableEvent(this));
		}
	}

}

