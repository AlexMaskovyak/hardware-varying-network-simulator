package network;

/**
 * Handles a packet.  Basically a protocal or part of the stack.
 * @author Alex Maskovyak
 *
 */
public interface IProtocolHandler<T> {

	/** handle the packet supplied in a protocol specific way. */
	public void handle(T packet);
	
	/** obtains the handler for a particular protocol. */
	public IProtocolHandler getHandler(String protocol);
	
	/** obtains the protocol with which this handler identifies itself. */
	public String getProtocal();
	
	/** install a packethandler to handle for the specified protocal. */
	public void install(IProtocolHandler handler, String protocal);
	
	/** uninstall the packethandler from all protocals established. */
	public void uninstall(IProtocolHandler handler);
	
	/** uninstall the protocal. */
	public void uninstall(String protocal);
}
