import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.UUID;

import simulation.ISimulatable;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.ISimulatorEvent;

import network.IConnection;
import network.IData;
import network.INode;


/**
 * 
 * @author Alex Maskovyak
 *
 */
public class NodeOld implements INode, ISimulatable {

	protected List<IConnection> _connections;
	protected List<ISimulator> _listeners;
	
	protected Queue<IData> _bufferIn;
	protected Queue<IData> _bufferOut;
	
	protected String _id;
	
	public NodeOld() {
		_connections = new ArrayList<IConnection>();
		_id = UUID.randomUUID().toString();
		_bufferIn = new LinkedList<IData>();
		_bufferOut = new LinkedList<IData>();
		_listeners = new ArrayList<ISimulator>();
	}
	
	public void addListener(ISimulator simulator) {
		_listeners.add(simulator);
	}
	
	public void removeListener(ISimulator simulator) {
		_listeners.remove(simulator);
	}
	
	@Override
	public void registerConnection(IConnection connect) {
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
		System.out.printf("Got a tick! %s\n", _id);
		for( ISimulator simulator : _listeners ) {
			simulator.signalDone(this);
		}
	}

	@Override
	public void addListener(ISimulatableListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ISimulatableListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receive(IData data, IConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterConnection(IConnection connection) {
		// TODO Auto-generated method stub
		
	}
}

