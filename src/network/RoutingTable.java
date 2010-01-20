package network;

import java.util.HashMap;

/**
 * 
 * @author Alex Maskovyak
 *
 */
public class RoutingTable extends HashMap<IAddress, IAddress>{

	public RoutingTable() {}
	
	public IAddress getNextHop(IAddress address) {
		return this.get(address);
	}
	
	public void setNextHop(IAddress destination, IAddress nextHop) {
		this.put(destination, nextHop);
	}
}
