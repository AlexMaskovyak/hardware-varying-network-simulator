package network;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import simulation.ISimulatable;
import simulation.ISimulatableListener;
import simulation.ISimulatorEvent;
import simulation.AbstractSimulatable;

/**
 * Bidirectional, multicast connection.
 * @author Alex Maskovyak
 *
 */
public class Connection extends AbstractSimulatable implements IConnection, ISimulatable {

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
	public INode[] getConnectedNodes() {
		INode[] result = new INode[_nodes.size()];
		result = _nodes.toArray(result);
		return result;
	}
	
	@Override
	public void receive(IData data) {
		_bufferIn.offer(data);
		System.out.printf("Connection got %d\n", data.getID());
	}

	@Override
	public void receive(INode sender, IData data) {
		receive(data);
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
