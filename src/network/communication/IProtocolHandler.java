package network.communication;

import network.routing.IAddress;

/**
 * Handles a packet.  Basically a protocal or part of the stack.
 * @author Alex Maskovyak
 *
 */
public interface IProtocolHandler<U, L> {

	/** 
	 * Handle the payload from the higher level protocol.
	 * @param payload from the higher level protocol which is to be wrapped.
	 * @param destination to which to send payload.
	 * @param sender reference which is invoking us.
	 */
	public void handleHigher(U payload, IProtocolHandler sender);
	
	/**
	 * Handle the message from the lower protocol.
	 * @param message in the format we understand.
	 * @param sender reference which is invoking us.
	 */
	public void handleLower(L message, IProtocolHandler sender);
	
	/**
	 * Gets the higher level protocol handler.
	 * @return higher level protocol handler.
	 */
	public IProtocolHandler getHigherHandler();
	
	/**
	 * Gets the lower level protocol handler.
	 * @return the lower level protocol handler.
	 */
	public IProtocolHandler getLowerHandler();
	
	/**
	 * Installs a protocol handler to receive requests from this one.
	 * @param handler to which to send processed information from below.
	 */
	public void installHigherHandler(IProtocolHandler handler);
		
	/**
	 * Installs a protocol handler to which to send requests from this one.
	 * @param handler to which to send processed informationn from above.
	 */
	public void installLowerHandler(IProtocolHandler handler);
	
	/** obtains the protocol with which this handler identifies itself. 
	 * @return name of the protocol handled by this handler. */
	public String getProtocol();
}
