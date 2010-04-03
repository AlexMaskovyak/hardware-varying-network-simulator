package routing;

import java.util.HashMap;

import network.Address;


/**
 * Allows a device to route/get the next hop in a network.
 * @author Alex Maskovyak
 *
 */
public class RoutingTable extends HashMap<IAddress, IAddress> implements IRoutingTable{

	/** default version id. */
	private static final long serialVersionUID = -5636400272666287427L;
	/** backbone behind simple router table. */
	protected CentralRouter _router;
	/** address we are locating. */
	protected IAddress _address;
	
	/** Default constructor. */
	public RoutingTable() {
		this(null);
	}
	
	/** Default constructor. */
	public RoutingTable(IAddress address) {
		init();
		setStartAddress(address);
	}
	
	/** Externalize instantiations. */
	protected void init() {
		_router = CentralRouter.getInstance();
	}
	
	/**
	 * Set the starting address for the RoutingTable since it has a universal 
	 * non-sourced picture.  It needs a starting address to perform operations.
	 * @param address
	 */
	public void setStartAddress(IAddress address) {
		_address = address;
	}
	
	/* (non-Javadoc)
	 * @see network.IRoutingTable#getNextHop(network.IAddress)
	 */
	public IAddress getNextHop(IAddress destination) {
		return _router.getNextHop(_address, destination);
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
    	IRoutingTable table0 = new RoutingTable(n0);
    	CentralRouter router = CentralRouter.getInstance();
    	router.addBiDirectionalRoute(n0, n1, 1);
    	//router.addBiDirectionalRoute(n1, n2, 1);
    	//router.addBiDirectionalRoute(n0, n2, 1);
    	//router.addBiDirectionalRoute(n2, n3, 1);
    	//router.addBiDirectionalRoute(n3, n4, 1);
    	///map.addDirectRoute(start, end, cost)
    	//IAddress next = router.getNextHop(n0, n4);
    	
    	//System.out.println( next );
    	
    	IAddress next = table0.getNextHop(n1);
    	
    	System.out.println( ">>>> next: " + next );
    	
    	IRoutingTable table1 = new RoutingTable(n1);
    
    	next = table1.getNextHop(n0);
    	System.out.println( ">>>> next: " + next );
    	
    	next = table1.getNextHop(n1);
    	System.out.println( ">>>> next: " + next );
	}
}
