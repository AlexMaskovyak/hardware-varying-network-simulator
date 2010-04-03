package network;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import reporting.NodeReporter;
import routing.CentralRouter;
import simulation.DiscreteScheduledEventSimulator;
import simulation.IDiscreteScheduledEventSimulator;
import simulation.ISimulatable;
import simulation.ISimulator;

/**
 * Simulates a network of nodes, connectionmediums, etc.
 * @author Alex Maskovyak
 *
 */
public class NetworkSimulator 
		extends DiscreteScheduledEventSimulator 
		implements ISimulator, IDiscreteScheduledEventSimulator {

/// Fields
	
	/** universal simple router for all nodes. */
	protected CentralRouter _router;
	/** baseline type to clone for production. */
	protected INode _baseline;
	/** address creating entity. */
	protected AddressCreator _addressCreator;
	
/// Construction
	
	/** Default constructor */
	public NetworkSimulator() {
		super();
	}
	
	/** externalize instantiations. */
	protected void init() {
		super.init();
		_addressCreator = AddressCreator.getInstance();
		_router = CentralRouter.getInstance();
		_baseline = new Node();
	}

/// Factory methods
	
	/** creates a Node and adds it to the simulator. */
	public INode createNode() {
		INode node = _baseline.createNew(_addressCreator.createUnique());
		try { ((ISimulatable)node).addListener( new NodeReporter(node, "C:\\tests") ); } 
		catch (Exception e) { e.printStackTrace(); }
		registerSimulatable((ISimulatable)node);
		return node;
	}
	
	/** creates an adaptor and adds it to the simulator. */
	public IConnectionAdaptor createAdaptor() {
		IConnectionAdaptor adaptor = new ConnectionAdaptor();
		registerSimulatable((ISimulatable)adaptor);
		return adaptor;
	}
	
	/** creates a connection medium and adds it to the simulator. */
	public IConnectionMedium createConnectionMedium() {
		IConnectionMedium medium = new ConnectionMedium();
		registerSimulatable((ISimulatable)medium);
		return medium;
	}


/// Mass factories.

	/**
	 * Creates a collection of nodes.
	 * @param number of nodes to create.
	 * @return array of simulatable nodes.
	 */
	public INode[] createNodes( int number ) {
		// create the required number of nodes
		INode[] nodes = new INode[ number ];
		for( int i = 0; i < number; ++i ) {
			nodes[ i ] = createNode();
		}
		return nodes;
	}
	
	/**
	 * Creates a series of Nodes, Adaptors, and ConnectionMediums in a row, 
	 * and then connects them in series.
	 * @param number of nodes to create and connect.
	 * @return collection of nodes.
	 */
	public List<ISimulatable> createSeriesOfConnectedNodes( int number ) {
		return connectAsSeries( createNodes( number ) ); // create>connect them
	}

	/**
	 * Creates a series of Nodes, Adaptors, and ConnectionMediums in a ring, 
	 * where every Node is connected in series to the others and the two 
	 * would-be ends are connected to each other.
	 * @param number of nodes to create and connect.
	 * @return collection of nodes.
	 */
	public List<ISimulatable> createRingOfConnectedNodes( int number ) {
		return connectAsRing( createNodes( number ) ); // create > connect them.
	}
	
	/**
	 * Creates a series of Nodes, installs Adaptors, and connects them all to
	 * a single ConnectionMedium as if they were in a bus.
	 * @param number of nodes to create and connect.
	 * @return collection of nodes and a medium.
	 */
	public List<ISimulatable> createBusOfConnectedNodes( int number ) {
		return connectAsBus( createNodes( number ) ); // create > connect them
	}
	
	/**
	 * Creates a series of Nodes, installs Adaptors, and ConnectionMediums and
	 * connects them all as if they were in a mesh.
	 * @param number of nodes to create and connect.
	 * @return collection of nodes and a medium.
	 */
	public List<ISimulatable> createMeshOfConnectedNodes( int number ) {
		return connectAsMesh( createNodes( number ) ); // create > connect them
	}
	
/// Connecting Support
	
	/**
	 * Connects 2 nodes together with a connectionmedium that is created by 
	 * the simulator.
	 * @param node to connect.
	 * @param node2 to connect.
	 * @return ConnectionMedium connecting the 2.
	 * @throws InvalidObjectException if a node created from outside of this 
	 * simulator is introduced.
	 */
	public IConnectionMedium connect( INode node, INode node2 ) 
			throws InvalidObjectException {
		IConnectionMedium medium = createConnectionMedium();
		connect( node, node2, medium );
		return medium;
	}
	
	/**
	 * Connects 2 nodes together with a connection medium.
	 * @param node to connect.
	 * @param node2 to connect.
	 * @throws InvalidObjectException if a node or medium created from outside
	 * of this simulator is introduced.
	 */
	public void connect( INode node, INode node2, IConnectionMedium medium ) 
			throws InvalidObjectException {
		connect( node, medium );
		connect( node2, medium );
	}
	
	/**
	 * Connects an arbitrary series of nodes with connection mediums placed in
	 * between them.
	 * @param nodes to connect in series.
	 */
	public List<ISimulatable> connectAsSeries( INode... nodes ) {
		// return value
		List<ISimulatable> result = new ArrayList<ISimulatable>();
		
		// looping and connecting constructs
		Queue<INode> unconnected = new LinkedList<INode>();	// not yet connected
		
		// do for all nodes
		for( INode node : nodes ) {
			result.add( (ISimulatable)node );
			unconnected.add( node );
			
			// every two, remove the first node and connect it to the second
			if( unconnected.size() == 2 ) {
				INode first = unconnected.poll();
				INode second = unconnected.peek();
				try { result.add( (ISimulatable)connect( first, second ) ); } 
				catch (InvalidObjectException e) { /* cannot occur */ }
			}
		}
		return result;
	}
	
	/**
	 * Connects an arbitrary series of nodes with connection mediums placed in
	 * between them.
	 * @param nodes to connect in series.
	 */
	public List<ISimulatable> connectAsRing( INode... nodes ) {
		List<ISimulatable> result = connectAsSeries( nodes );
		// true rings require 3 elements
		if( nodes.length >= 3 ) {
			INode head = (INode) result.get( 0 );
			INode tail = (INode) result.get( result.size() - 2 ); // end of list is the last medium
			try { result.add( (ISimulatable) connect( head, tail ) ); }
			catch( Exception e ) { /* cannot occur. */ }
		}
		return result;
	}
	
	/**
	 * Connects every node in the list to a single medium as a bus.
	 * @param nodes which are to be connected with each other.
	 * @return list of all ISimulatables connected (all nodes, plus one medium).
	 */
	public List<ISimulatable> connectAsBus( INode... nodes ) {
		// return value
		List<ISimulatable> result = new ArrayList<ISimulatable>();
		IConnectionMedium medium = createConnectionMedium();
		
		for( INode node : nodes ) {
			result.add( (ISimulatable)node );
			internalConnect( node, medium );
		}
		
		result.add( (ISimulatable)medium );
		
		return result;
	}
	
	/**
	 * Connect every node in the list to every other node in the list as a mesh
	 * of connections.
	 * @param nodes which are to be connected with each other as a mesh.
	 * @return list of all ISimultables connected (all nodes, all mediums).
	 */
	public List<ISimulatable> connectAsMesh( INode... nodes ) {
		// return value
		List<ISimulatable> result = new ArrayList<ISimulatable>();
		Queue<INode> queue = new LinkedList<INode>( Arrays.asList( nodes ) );
		
		// take each node out in turn and connect them with every other node
		while( !queue.isEmpty() ) {
			INode current = queue.poll();
			result.add( (ISimulatable) current );
			for( INode node : queue ) {
				try { result.add( (ISimulatable) connect( current, node ) ); }
				catch( Exception e ) { /* cannot occur. */ }
			}
		}
		return result;
	}
	
	/** 
	 * Register a connection between a node and a connectionmedium for 
	 * simulatables that are a part of this network.
	 * @param node which is to be connected to the specified medium.
	 * @param medium which is to be connected to the specified node.
	 * @throws InvalidObjectException if a node or medium created from outside
	 * of this simulator is introduced.
	 */
	public void connect( INode node, IConnectionMedium medium ) 
			throws InvalidObjectException {
		if( !(_simulatables.contains(node) && _simulatables.contains(medium)) ) {
			throw new InvalidObjectException(
				"Both the INode and IConnectionMedium must be a part of this " +
				"NetworkSimulator in order to be connected.");
		}
		internalConnect( node, medium );
	}

	
/// Internal Support
	
	/**
	 * Safe connecting for use inside of the factory methods themselves where we
	 * can be guaranteed that no outside simulatables are being introduced.
	 * @param node which is to be connected to the specified medium.
	 * @param medium which is to be connected to the specified node.
	 */
	protected void internalConnect( INode node, IConnectionMedium medium ) {
		IConnectionAdaptor[] existingAdapators = medium.getConnectedAdaptors();
		IConnectionAdaptor newAdaptor = createAdaptor();
		node.addAdaptor( newAdaptor );
		newAdaptor.setAddress( node.getAddress() );
		
		// wire it up
		newAdaptor.connect( medium );
		medium.connect( newAdaptor );
		
		for( IConnectionAdaptor adaptor : existingAdapators ) {
			_router.addBiDirectionalRoute( 
				newAdaptor.getAddress(), adaptor.getAddress(), 1);
		}
	}
}
