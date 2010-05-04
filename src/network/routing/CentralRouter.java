package network.routing;

import network.communication.Address;


/**
 * Centralized way to ensure everyone knows how to route packets.  Ideally we
 * might do packet inspection and have every Node broadcast new Nodes that 
 * join the network, but this is beyond the scope of this project.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public class CentralRouter {

	/** singleton. */
	protected static CentralRouter _instance;
	
	/** representation of the network */
	protected IRoutesMap<IAddress> _graphOfNetwork;
	/** calculates routes. */
	protected DijkstraEngine<IAddress> _engine;
	
	/** Default constructor. */
	public CentralRouter() {
		init();
	}
	
	/**
	 * Externalize instantiations.
	 */
	protected void init() {
		_graphOfNetwork = new SparseRoutesMap<IAddress>();
		_engine = new DijkstraEngine<IAddress>(_graphOfNetwork);
	}
	
	/**
	 * Obtains the singular CentralRouter instance.
	 * @return CentralRouter singleton.
	 */
	public static CentralRouter getInstance() {
		if( CentralRouter._instance == null ) {
			CentralRouter._instance = new CentralRouter();
		}
		return CentralRouter._instance;
	}
	
	/**
	 * Resets everything.
	 */
	public void reset() {
		init();
	}
	
	/**
	 * Add a connection to the CentralRouter's knowledge-base.
	 * @param a1 address of first node.
	 * @param a2 address of second node.
	 * @param cost cost associated with path.
	 */
	public void addBiDirectionalRoute(IAddress a1, IAddress a2, int cost) {
		_graphOfNetwork.addBiDirectRoute(a1, a2, cost);
	}
	
	/**
	 * Removes an address from consideration.
	 * @param address to remove.
	 */
	public void remove(IAddress address) {
		_graphOfNetwork.remove(address);
	}
	
	/**
	 * Removes a connection to the CentralRouter's knowledge-base.
	 * @param a1 address of first node.
	 * @param a2 address of second node.
	 */
	public void removeBiDirectionalRoute(IAddress a1, IAddress a2) {
		_graphOfNetwork.removeBiDirectRoute(a1, a2);
	}
	
	/**
	 * Obtains the next hop for this item.
	 * @param a1 starting address.
	 * @param a2 destination address.
	 * @return next hop address.
	 */
	public IAddress getNextHop(IAddress a1, IAddress a2) {
		_engine.execute(a1, a2);
		return _engine.getNextHop(a1, a2);
	}
	
	/**
	 * Driver/tester.
	 * @param args N/A
	 */
	public static void main(String... args) {
	   	IAddress n0 = new Address(0);
    	IAddress n1 = new Address(1);
    	IAddress n2 = new Address(2);
    	IAddress n3 = new Address(3);
    	IAddress n4 = new Address(4);
 
    	
    	//RoutesMap<INode> map = new SparseRoutesMap<INode>();
    	//DijkstraEngine<INode> engine = new DijkstraEngine<INode>(map);
    	CentralRouter router = CentralRouter.getInstance();
    	router.addBiDirectionalRoute(n0, n1, 1);
    	//router.addBiDirectionalRoute(n1, n2, 1);
    	//router.addBiDirectionalRoute(n0, n2, 1);
    	//router.addBiDirectionalRoute(n2, n3, 1);
    	//router.addBiDirectionalRoute(n3, n4, 1);
    	///map.addDirectRoute(start, end, cost)
    	//IAddress next = router.getNextHop(n0, n4);
    	
    	//System.out.println( next );
    	
    	IAddress next = router.getNextHop(n0, n2);
    	
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n1, n0);
    	System.out.println( ">>>> next: " + next );
    	
    	next = router.getNextHop(n1, n1);
    	System.out.println( ">>>> next: " + next );
	}
}
