package network;


/**
 * An atomic network element.  Represents an information propagation mechanism.
 * 
 * @author Alex Maskovyak
 *
 */
public interface IConnectionMedium {

	/**
	 * Connects a series of adaptors.
	 * @param nodes to connect.
	 */
	public void connect(IConnectionAdaptor... adaptors);
	
	/**
	 * Connects a single adaptor.
	 * @param node to connect.
	 */
	public void connect(IConnectionAdaptor adaptor);
	
	/**
	 * Disconnects a single adaptor.
	 * @param node to disconnect.
	 */
	public void disconnect(IConnectionAdaptor adaptor);
	
	/**
	 * Obtains an array of INodes connected to this IConnection.
	 * @return INodes connected.
	 */
	public IConnectionAdaptor[] getConnectedAdaptors();
	
	/**
	 * Receives data.
	 * @param packet to receive.
	 */
	public void receive(IPacket packet);
	
	/**
	 * Receives data from the specified node.
	 * @param sender who sent data.
	 * @param packet to receive.
	 */
	public void receive(IConnectionAdaptor sender, IPacket packet);
	
	/**
	 * Sends data to connected nodes in an implementation specific way.
	 * @param sender node sending information.
	 * @param packet to be sent.
	 */
	public void send(IConnectionAdaptor sender, IPacket packet);
}
