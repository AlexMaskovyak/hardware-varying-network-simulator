
/**
 * An atomic network element.  Represents an information source and sink.
 * 
 * @author Alex Maskovyak
 *
 */
public interface INode {

	/**
	 * Connects this node to a connector and the connector to the node.
	 * @param connection to which to join.
	 */
	public void connect(IConnection connection);
	
	/**
	 * Receives data.
	 * @param data to have this node receive.
	 */
	public void receive(IData data);
	
	/**
	 * Sends data out to all connections.
	 * @param data to send to connected items.
	 */
	public void send(IData data);
	
	/**
	 * Sends data out across the specified connection.
	 * @param data to send to the connected item.
	 * @param connection to receive the data.
	 */
	public void send(IData data, IConnection connection);
}
