package computation;

/**
 * Implements a simple Data container.
 * @author Alex Maskovyak
 *
 */
public class Data implements IData {

/// Fields
	/** identifier. */
	protected int _ID;
	/** content contained by this container, does not currently have a use. */
	protected byte[] _content;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param ID for this container.
	 * @param content to hold.
	 */
	public Data(int ID, byte[] content) {
		_ID = ID;
		_content = content;
	}
	

/// Accessors 
	
	/*
	 * (non-Javadoc)
	 * @see computation.IData#getID()
	 */
	@Override
	public int getID() {
		return _ID;
	}

	/*
	 * (non-Javadoc)
	 * @see computation.IData#getContent()
	 */
	@Override
	public byte[] getContent() {
		return _content;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		return ( o instanceof IData ) ? equals((IData)o) : false;
	}
	
	/**
	 * Determines whether 2 IData representation are equivalent.
	 * @param data to test against.
	 * @return true if the are value-equal, false otherwise.
	 */
	public boolean equals(IData data) {
		return this.getID() == data.getID() && this.getContent() == data.getContent();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format( "Data [%d]", getID() );
	}
}
