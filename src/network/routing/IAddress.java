package network.routing;

/**
 * Simple addresses for nodes, connectionadaptors, etc.
 * @author Alex Maskovyak
 *
 */
public interface IAddress extends Comparable<IAddress> {

	/**
	 * Determines whether the two addresses are equivalent.
	 * @param address to compare against.
	 * @return true if the addresses are equal, false otherwise.
	 */
	public boolean equals(IAddress address);
	
	/**
	 * Compares this address to the specified address.
	 * @param address to which to compare.
	 * @return -1, 0, 1 if the current object is less than, equal to, or 
	 * greater than the specified object.
	 */
	public int compareTo(IAddress address);
}
