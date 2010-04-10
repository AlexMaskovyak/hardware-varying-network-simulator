package network;

/**
 * Handles a packet.  Basically a protocal or part of the stack.
 * @author Alex Maskovyak
 *
 */
public interface IProtocolHandler<T> {

	/** 
	 * handle the packet supplied in a protocol specific way.
	 * @param message
	 */
	public void handle(T message);
	
	/**
	 * obtains the handler for a particular protocol. 
	 * @param protocol associated with a handler.
	 * @return handler associated with the specified protocol, the universal 
	 * handler if none is specified.
	 */
	public IProtocolHandler getHandler(String protocol);
	
	/** obtains the protocol with which this handler identifies itself. */
	public String getProtocol();
	
	/**
	 * Installs a protocolhandler using that protocol handler's default protocol
	 * name for mapping.
	 * @param handler to install.
	 */
	public void install(IProtocolHandler handler);
	
	/** 
	 * install a ProtocolHandler to handle for the specified protocol. 
	 * @param handler to associate with the protocol.
	 * @param protocol name to associate with the handler.
	 */
	public void install(IProtocolHandler handler, String protocol);
	
	/** 
	 * uninstall the protocolhandler from all protocols established.
	 * @param handler to uninstall completely.
	 */
	public void uninstall(IProtocolHandler handler);
	
	/**
	 * uninstall the protocal and it associated handler. 
	 * @param protocal and associated handler to remove.
	 */
	public void uninstall(String protocal);
}
