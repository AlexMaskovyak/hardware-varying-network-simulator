package network.communication;

import java.util.HashMap;
import java.util.Map;

import messages.ProtocolHandlerMessage;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;

/**
 * Basic implementation of a ProtocolHandler.  It has a protocol mapping set and 
 * can handle messages based upon that mapping.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public abstract class AbstractProtocolHandler<U, L> 
		extends PerformanceRestrictedSimulatable
		implements IProtocolHandler<U, L> {
	
/// Fields
	
	/** protocals installed on this adaptor. */
	protected Map<String, AbstractProtocolHandler> _protocalMappings;
	/** universal accept protocal, if no other protocal is present check this. */ 
	public static String DEFAULT_PROTOCAL = "UNIVERSAL_HANDLER";
	
	/** higher level handler installed atop us. */
	protected IProtocolHandler _higherHandler;
	/** lower level handler on which we are installed atop. */
	protected IProtocolHandler _lowerHandler;
	
	/** name of the protocol we handle. */
	protected String _protocol;
	
/// Construction
	
	/** Default constructor. */
	protected AbstractProtocolHandler() {
		super();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		super.init();
		_protocalMappings = new HashMap<String, AbstractProtocolHandler>();
	}
	

/// IProtocolHandler
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IProtocolHandler#handleHigher(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public abstract void handleHigher(U payload, IProtocolHandler sender);
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IProtocolHandler#handleLower(java.lang.Object, network.communication.IProtocolHandler)
	 */
	@Override
	public abstract void handleLower(L message, IProtocolHandler sender);
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IProtocolHandler#installHigherHandler(network.communication.IProtocolHandler)
	 */
	@Override
	public void installHigherHandler(IProtocolHandler higherHandler) {
		_higherHandler = higherHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IProtocolHandler#installLowerHandler(network.communication.IProtocolHandler)
	 */
	@Override
	public void installLowerHandler(IProtocolHandler lowerHandler) {
		_lowerHandler = lowerHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IProtocolHandler#getHigherHandler()
	 */
	@Override
	public IProtocolHandler getHigherHandler() {
		return _higherHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.communication.IProtocolHandler#getLowerHandler()
	 */
	@Override
	public IProtocolHandler getLowerHandler() {
		return _lowerHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return _protocol;
	}
}
