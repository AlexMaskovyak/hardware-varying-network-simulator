package simulation.simulator;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import network.communication.AddressCreator;
import network.entities.ConnectionAdaptor;
import network.entities.ConnectionMedium;
import network.entities.IConnectionAdaptor;
import network.entities.IConnectionMedium;
import network.entities.INode;
import network.entities.Node;
import network.routing.CentralRouter;

import reporting.NodeReporter;
import simulation.simulatable.ISimulatable;


/**
 * Simulates a network of nodes, connectionmediums, etc.
 * @author Alex Maskovyak
 *
 */
public class NetworkSimulator 
		extends DESimulator 
		implements ISimulator, IDESimulator {

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
	
	/**
	 * Creates a series of Nodes, installs Adaptors, and ConnectionMediums and
	 * connects them all randomly using the seed as a base.  Connectedness is 
	 * one.  Nodes are guaranteed to only connect to one node other than
	 * themselves.
	 * @param seed value to use for connection algorithm.
	 * @param number of nodes to create and connected.
	 * @return collection of nodes and mediums.
	 */
	public List<ISimulatable> createRandomlyConnectedNodes( 
			int seed, 
			int number ) {
		return createRandomlyConnectedNodes( seed, number, 1 );
	}
	
	/**
	 * Creates a series of Nodes, installs Adaptors, and ConnectionMediums and
	 * connects them all randomly using the seed as a base.  Connectedness 
	 * defines the average number of connections a node is to have with other 
	 * nodes.  Nodes are guaranteed not to connect to themselves.
	 * @param seed value to use for connection algorithm.
	 * @param number of nodes to create and connected.
	 * @param connectedness of the nodes (how many connections should be created? )
	 * @return collection of nodes and mediums.
	 */
	public List<ISimulatable> createRandomlyConnectedNodes( 
			int seed, 
			int number, 
			int connectedness ) {
		return connectRandomly( seed, createNodes( number ) );
	}
	
	
	
/// Anti-factories.
	
	/**
	 * Remove the ISimulatable and any associated simulatables from this 
	 * network and simulator.
	 * @param simulatable to remove.
	 */
	public void remove( ISimulatable simulatable ) {
		// delegate
		if( simulatable instanceof INode ) {
			remove( (INode)simulatable );
		} else if ( simulatable instanceof IConnectionMedium ) {
			remove( (IConnectionMedium)simulatable );
		} else if ( simulatable instanceof IConnectionAdaptor ) {
			remove( (IConnectionAdaptor)simulatable );
		}
	}

	/**
	 * Removes the node and associated adaptors from the simulator.
	 * @param node to remove.
	 */
	public void remove( INode node) {
		disconnect( node );
		unregisterSimulatable( (ISimulatable)node );
	}
	
	/**
	 * Removes the medium from the simulator and disconnects associated 
	 * adaptors.
	 * @param medium to remove.
	 */
	public void remove( IConnectionMedium medium ) {
		disconnect( medium );
		unregisterSimulatable( (ISimulatable) medium );
	}
	
	/**
	 * Removes the connection adaptor from the simulator.
	 * @param adaptor to remove.
	 */
	public void remove( IConnectionAdaptor adaptor ) {
		disconnect( adaptor );
		unregisterSimulatable( (ISimulatable) adaptor );
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
	 * Connects a node to several other nodes with ConnectionMediums generated
	 * between them.
	 * @param node to connect to the others.
	 * @param nodes which will be connected to the first specified node.
	 * @return all nodes and connection mediums.
	 */
	public Collection<ISimulatable> connect( INode node, INode...nodes  ) {
		return connect( node, Arrays.asList( nodes ) );
	}
	
	/**
	 * Connects a node to several other nodes with ConnectionMediums generated
	 * between them.
	 * @param node to connect to the others.
	 * @param nodes which will be connected to the first specified node.
	 * @return all nodes and connection mediums.
	 */
	public Collection<ISimulatable> connect( INode node, Collection<INode> nodes ) {
		Collection<ISimulatable> result = new ArrayList<ISimulatable>();
		for( INode nodeInCollection : nodes ) {
			try { result.add( (ISimulatable)connect( node, nodeInCollection ) ); }
			catch( Exception e ) { /* cannot fail. */ }
		}
		return result;
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
	 * Connects all nodes in a random fashion.  All nodes will be reachable.  
	 * Repeated calls of this method with the same seed will produce the same
	 * resultant tree.
	 * @param nodes to connected.
	 * @return list of all ISimulatables connected (all nodes, all mediums).
	 */
	public List<ISimulatable> connectRandomly( int seed, INode... nodes ) {
		System.out.println("connectrandomly");
		Random r = new Random(seed);
		List<ISimulatable> result = 
			new ArrayList<ISimulatable>( (2 * nodes.length) - 1);
		List<INode> notConnected = new ArrayList<INode>( Arrays.asList( nodes ) );
		List<INode> connected = new ArrayList<INode>();
	
		INode notNode = null;
		INode conNode = null;
		
		// get one node from the notConnected list and add to the other
		// get us started with the first one
		notNode = notConnected.get( r.nextInt( notConnected.size() ));
		notConnected.remove( notNode );
		connected.add( notNode );
		
		for( int i = 0; i < nodes.length; ++i ) {
			try {
				if( notConnected.size() > 0 ) {
					notNode = notConnected.get( r.nextInt( notConnected.size() ) );
					conNode = connected.get( r.nextInt( connected.size() ) );
					result.add( (ISimulatable)notNode );
					result.add( (ISimulatable)connect( notNode, conNode ) );
					notConnected.remove( notNode );
					connected.add( notNode );
				}
			} catch( Exception e ) { e.printStackTrace(); }
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

	
/// Disconnect Helpers
	
	/**
	 * Disconnects several nodes from the network.  Note: the nodes themselves
	 * will stay remain registered with the simulator!
	 * @param nodes to disconnect.
	 */
	public void disconnectNodes( INode... nodes ) {
		disconnectNodes( Arrays.asList( nodes ) );
	}
	
	/**
	 * Disconnects several nodes from the network.  Note: the nodes themselves
	 * will stay remain registered with the simulator!
	 * @param nodes to disconnect.
	 */
	public void disconnectNodes( Collection<INode> nodes ) {
		for( INode node : nodes ) {
			disconnect( node );
		}
	}
	
	/**
	 * Disconnects a node from the network, basically removes its adaptors, and
	 * updates the routing information.  Note: the node itself will remain
	 * registered with the simulator!  See {@link #remove(INode)} for actual
	 * removal from the simulation environment.
	 * @param node to disconnect.
	 */
	public void disconnect( INode node ) {
		Collection<IConnectionAdaptor> adaptors = node.getAdaptors();
		IConnectionMedium medium = null;
		// disconnect
		for( IConnectionAdaptor adaptor : adaptors ) {
			disconnect( adaptor );
			unregisterSimulatable( (ISimulatable)adaptor );	// adaptors should not exist if not connected
		}
	}
	
	/**
	 * Disconnects a node from another node.  Disconnects the adaptors that 
	 * connect to a medium shared between the two.
	 * @param node1 to disconnect from node2.
	 * @param node2 to disconnect from node1.
	 */
	public void disconnect( INode node1, INode node2 ) {
		Collection<IConnectionAdaptor> adaptors1 = node1.getAdaptors();
		Collection<IConnectionAdaptor> adaptors2 = node2.getAdaptors();
		
		// O(n^2) search to find those adaptors with common mediums
		for( IConnectionAdaptor adaptor1 : adaptors1 ) {
			for( IConnectionAdaptor adaptor2 : adaptors2 ) {
				// do they connect to the same medium?
				// this is a direct link!
				if( adaptor1.getConnectedMedium() == adaptor2.getConnectedMedium() ) {
					disconnect( adaptor1 );
					disconnect( adaptor2 );
				}
			}
		}
	}
	
	/**
	 * Disconnects a node from a medium.  Removes any adaptors to that medium.
	 * @param node to disconnect from the medium.
	 * @param medium to disconnect from the node.
	 */
	public void disconnect( INode node, IConnectionMedium medium ) {
		Collection<IConnectionAdaptor> nodeAdaptors = node.getAdaptors();
		Collection<IConnectionAdaptor> mediumAdaptors = 
			Arrays.asList( medium.getConnectedAdaptors() );
		
		// disconnect the adaptor held in common
		nodeAdaptors.retainAll(mediumAdaptors);	// retain the common ones
		disconnectAdaptors( nodeAdaptors );		// disconnect the commons (should be one)
	}
	
	/**
	 * Disconnects a medium from adaptors.
	 * @param medium to disconnect.
	 */
	public void disconnect( IConnectionMedium medium ) {
		// queue it up
		Queue<IConnectionAdaptor> queue = 
			new LinkedList<IConnectionAdaptor>( 
				Arrays.asList( medium.getConnectedAdaptors() ) );
		
		// take each node out in turn and disconnects it from the medium
		while( !queue.isEmpty() ) {
			IConnectionAdaptor adaptor = queue.poll();
			
			adaptor.disconnect( medium );		// disconnect medium
			medium.disconnect( adaptor );		// disconnect adaptor
			
			// tell the router that this one no longer has a bi-directional 
			// route to the others
			for( IConnectionAdaptor other : queue ) {
				_router.removeBiDirectionalRoute( 
					adaptor.getAddress(), 
					other.getAddress() );
			}
		}
	}
	
	/**
	 * Disconnects all adaptors from their connected Nodes and mediums.  
	 * Typically this will not be called by an outside user.  
	 * @param adaptors to disconnect.
	 */
	public void disconnectAdaptors( Collection<IConnectionAdaptor> adaptors ) {
		for( IConnectionAdaptor adaptor : adaptors ) {
			disconnect( adaptor );
		}
	}
	
	/**
	 * Disconnects an adaptor from a Node and the medium.  Typically this will
	 * not be called by an outside user.  Usual use is as a helper for 
	 * {@link #disconnect(INode)}.
	 * @param adaptor to disconnect.
	 */
	public void disconnect( IConnectionAdaptor adaptor ) {
		// deal with the node
		INode node = adaptor.getConnectedNode();
		node.removeAdaptor( adaptor );
		adaptor.setConnectedNode( null );
		
		// deal with the medium
		IConnectionMedium medium = adaptor.getConnectedMedium();
		medium.disconnect( adaptor );
		adaptor.disconnect( medium );
		
		// perform central router disconnect
		for( IConnectionAdaptor mediumAdaptor: medium.getConnectedAdaptors() ) {
			_router.removeBiDirectionalRoute( 
				adaptor.getAddress(), 
				mediumAdaptor.getAddress() );
		}
	}

	
/// Internal Support
	
	/**
	 * Safe connecting for use inside of the factory methods themselves where we
	 * can be guaranteed that no outside simulatables are being introduced.  
	 * This method is the final delegate for all connection operations!  There
	 * would have to be a good reason not to call this method when implementing
	 * an additional one.
	 * @param node which is to be connected to the specified medium.
	 * @param medium which is to be connected to the specified node.
	 */
	protected void internalConnect( INode node, IConnectionMedium medium ) {
		IConnectionAdaptor[] existingAdapators = medium.getConnectedAdaptors();
		IConnectionAdaptor newAdaptor = createAdaptor();
		newAdaptor.setConnectedNode( node );
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