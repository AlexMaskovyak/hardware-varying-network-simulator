package network.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a stack of protocols.
 * @author Alex Maskovyak
 *
 */
public class ProtocolStack {

/// Fields
	
	/** protocol handler stack.  a level is a handler. */
	protected List<IProtocolHandler> _handlerStack;
	
	
/// Construction
	
	/** Default constructor. */
	public ProtocolStack() {
		init();
	}

	/** externalize instantiation. */
	protected void init() {
		_handlerStack = new ArrayList<IProtocolHandler>();
	}
	

/// Interface
	
	/**
	 * Installs a protocolhandler at the top level.
	 * @param handler to install. 
	 */
	public void addLevel( IProtocolHandler handler ) {
		IProtocolHandler oldTop = getTopLevel();
		mutualInstall( handler, oldTop );
		_handlerStack.add( handler );
	}
	
	/**
	 * Installs a protocolhandler at the bottom level.
	 */
	public void addBottomLevel( IProtocolHandler handler ) {
		IProtocolHandler oldBottom = getBottomLevel();
		mutualInstall( handler, oldBottom );
		_handlerStack.add( 0, handler );
	}
	
	/**
	 * Gets the size of the stack.
	 * @return stack's size.
	 */
	public int getSize() {
		return _handlerStack.size();
	}
	
	/**
	 * Obtains the protocol handler at the top of the stack.
	 * @return protocol handler at the top of the stack.
	 */
	public IProtocolHandler getTopLevel() {
		return _handlerStack.get( getTopLevelIndex() );
	}

	/**
	 * Obtains the bottommost protocol handler.
	 * @return protocol handler at the bottom of the stack.
	 */
	public IProtocolHandler getBottomLevel() {
		return _handlerStack.get( 0 );
	}
	
	
/// Support
	
	/**
	 * Retrieves the index of the top level of the stack.
	 * @return 
	 */
	protected int getTopLevelIndex() {
		return getSize() - 1;
	}
	
	/**
	 * Installs the handlers to each other, allowing for progression in both
	 * directions.
	 * @param handler1 to install on handler2.
	 * @param handler2 to install on handler1.
	 */
	protected void mutualInstall( 
			IProtocolHandler handler1, 
			IProtocolHandler handler2 ) {
		//handler1.installLowerHandler(handler)
		//handler1.install( handler2 );
		//handler2.install( handler1 );
	}
	
	/**
	 * Uninstalls the handlers from each other, allowing for progression in both
	 * directions.
	 * @param handler1 to uninstall from handler2.
	 * @param handler2 to uninstall from handler1.
	 */
	protected void mutualUninstall(
			IProtocolHandler handler1, 
			IProtocolHandler handler2 ) {
		//handler1.uninstall( handler2 );
		//handler2.uninstall( handler1 );
	}
}
