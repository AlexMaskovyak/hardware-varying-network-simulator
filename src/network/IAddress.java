package network;

public interface IAddress {

	/**
	 * Determines whether the two addresses are equivalent.
	 * @param address to compare against.
	 * @return true if the addresses are equal, false otherwise.
	 */
	public boolean equals(IAddress address);
}
