package network;

public class Address {

	protected int _address;
	
	public Address(int address) {
		_address = address;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Address) {
			return equals((Address)o);
		}
		return false;
	}
	
	public boolean equals(Address a) {
		return _address == a._address;
	}
}
