package network.routing;


/**
 * Very slim version of what a Routing Table should provide.  
 * @author Alex Maskovyak
 *
 */
public interface IRoutingTable {

	/**
	 * Obtains the next hop address to the specified destination.
	 * @param destination to which we are attempting to route.
	 * @return next hop address along the way to the destination.
	 */
	public abstract IAddress getNextHop(IAddress destination);
}