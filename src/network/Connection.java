package network;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import simulation.ISimulatable;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;
import simulation.Simulatable;
import simulation.SimulatableEvent;

/**
 * Bidirectional, multicast connection.
 * @author Alex Maskovyak
 *
 */
public class Connection extends Simulatable implements IConnection, ISimulatable {

	/** connected nodes. */
	protected Set<INode> _nodes;
	/** information coming in. */
	protected Queue<IData> _bufferIn;
	/** information ready to go out. */
	protected Queue<IData> _bufferOut;
	
	/**
	 * Default constructor.
	 */
	public Connection() {
		super();
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.
	 */
	protected void init() {
		super.init();
		_listeners = new HashSet<ISimulatableListener>();
		_nodes = new HashSet<INode>();
		_bufferIn = new LinkedList<IData>();
		_bufferOut = new LinkedList<IData>();		
	}
	
	@Override
	public void connect(INode... nodes) {
		for( INode node : nodes ) {
			connect(node);
		}
	}

	@Override
	public void connect(INode node) {
		_nodes.add(node);
	}
	
	@Override
	public void disconnect(INode node) {
		_nodes.remove(node);
	}
	
	@Override
	public void receive(IData data) {
		_bufferIn.offer(data);
	}

	public void send() {
		IData data = _bufferOut.poll();
		send(null, data);
	}
	
	@Override
	public void send(INode sender, IData data) {
		for(INode node : _nodes) {
			if( node == sender ) {
				continue;
			}
			node.receive(data);
		}
	}

	
	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		send();
		
		super.handleTickEvent(o);
	}

}
