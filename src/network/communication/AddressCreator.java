package network.communication;

import network.routing.IAddress;

/**
 * Creates unique addresses for the ConnectionAdaptor.
 * @author Alex Maskovyak
 *
 */
public class AddressCreator {
	
	/** singleton. */
	protected static AddressCreator _instance;
	
	/** current address. */
	protected Integer _currentAddress;
	
	/** Default constructor. */
	protected AddressCreator() {
		init();
	}
	
	/** externalize instantiation. */
	protected void init() {
		_currentAddress = 0;
	}
	
	/**
	 * Factory for singleton.
	 * @return AddressCreator instance.
	 */
	public static AddressCreator getInstance() {
		if( AddressCreator._instance == null ) {
			AddressCreator._instance = new AddressCreator();
		}
		return AddressCreator._instance;
	}
	
	/**
	 * Reset everything.
	 */
	public void reset() {
		init();
	}
	
	/**
	 * Creates a new unique address for use in the system.
	 * @return
	 */
	public synchronized IAddress createUnique() {
		int address = _currentAddress++;
		return new Address(address);
	}
}