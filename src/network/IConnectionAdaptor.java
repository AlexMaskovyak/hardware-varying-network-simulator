package network;

import routing.IAddress;

/**
 * Allows for the transmission of information to ConnectionAdaptors.
 * @author Alex Maskovyak
 *
 */
public interface IConnectionAdaptor<T> extends IProtocolHandler<T> {

	/**
	 * Obtains this adaptor's address.
	 * @return address of this adaptor.
	 */
	public abstract IAddress getAddress();

	/**
	 * Sets this adaptor's address.
	 * @param address to assign to this address.
	 */
	public abstract void setAddress(IAddress address);
	
	/**
	 * Connects this adaptor to the medium.  Note: this does not affect the
	 * medium at all.  Additional steps may be necessary to ensure the medium
	 * understands that this has made a connection.
	 * @param medium to which to connect.
	 */
	public abstract void connect(IConnectionMedium medium);
	
	/**
	 * Removes a medium from this adaptor.  Note: this does not affect the 
	 * medium at all.  Additional steps may be necessary to ensure the medium
	 * understands that this has made a connection.
	 * @param medium to which to disconnect.
	 */
	public abstract void disconnect(IConnectionMedium medium);
	
	/**
	 * Retrieves the medium to which this adaptor is connected.
	 * @return the medium to which this adaptor is connected, null otherwise.
	 */
	public abstract IConnectionMedium getConnectedMedium();
	
	/**
	 * Retrieves the node to which this adaptor is connected.
	 * @return node to which we are connected.
	 */
	public abstract INode getConnectedNode();
	
	/**
	 * Sets the node to which this adaptor is connected.
	 * @param node to which  we are connected.
	 */
	public abstract void setConnectedNode(INode node);
	
	/**
	 * Receive a packet.  If it is for us, the move it up the protocal stack.
	 * If it is from us, send it out!
	 * @param packet to inspect.
	 */
	public abstract void receive(IPacket<IPacket> packet);

	/**
	 * Sends out the packet across the medium.
	 * @param packet to send across the medium.
	 */
	public void send(IPacket<IPacket> packet);
}