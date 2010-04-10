package network.communication;

import network.routing.IAddress;

/**
 * Simple implementation of an address.
 * @author Alex Maskovyak
 *
 */
public class Address implements IAddress {

/// Fields
	
	/** underlying representation. */
	protected int _address;
	
	
/// Construction

	/**
	 * Default constructor.
	 * @param address integer to assign.
	 */
	public Address(int address) {
		_address = address;
	}

	
/// Field accessor
	
	/**
	 * Gets the underlying representation of this address.
	 * @return the underlying integer.
	 */
	public int getRepresentation() {
		return _address;
	}

	
/// Equality
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(o instanceof IAddress) {
			return equals((IAddress)o);
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IAddress#equals(network.IAddress)
	 */
	@Override
	public boolean equals(IAddress address) {
		if(address instanceof Address) {
			return equals((Address)address);
		}
		return false;
	}
	
	/**
	 * Test if the address is equal to this one.
	 * @param a address to test.
	 * @return true if the addresses are equal, false otherwise.
	 */
	public boolean equals(Address a) {
		return _address == a._address;
	}
	

/// Comparison
	
	/*
	 * (non-Javadoc)
	 * @see network.IAddress#compareTo(network.IAddress)
	 */
	public int compareTo(IAddress a) {
		if(a instanceof Address) {
			return compareTo((Address)a);
		}
		return -1;
	}
	
	/**
	 * Compares the address against this one.
	 * @param a address to compare against.
	 * @return -1, 0, 1, if the specified address is less than, equal to, or
	 * greater then this one.
	 */
	public int compareTo(Address a) {
		return _address - a._address;
	}

	
/// Utility
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("%d", _address);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (new Integer(_address)).hashCode();
	}
}
