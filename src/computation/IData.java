package computation;

/**
 * Describes an abstract unit of data.
 * @author Alex Maskovyak
 *
 */
public interface IData {

	/**
	 * Obtains the unique identifier for this piece.
	 * @return the ID;
	 */
	public int getID();
	
	/**
	 * Obtains the actual content stored in this data wrapper.
	 * @return Data's content;
	 */
	public byte[] getContent();
}
