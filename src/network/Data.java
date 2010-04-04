package network;

/**
 * Implements a simple Data container.
 * @author Alex Maskovyak
 *
 */
public class Data implements IData {

	/** identifier. */
	protected int _ID;
	/** content contained by this container, does not currently have a use. */
	protected byte[] _content;
	
	/**
	 * Default constructor.
	 * @param ID for this container.
	 * @param content to hold.
	 */
	public Data(int ID, byte[] content) {
		_ID = ID;
		_content = content;
	}
	
	@Override
	public int getID() {
		return _ID;
	}

	@Override
	public byte[] getContent() {
		return _content;
	}
}
