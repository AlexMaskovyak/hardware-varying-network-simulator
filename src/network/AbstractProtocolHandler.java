package network;

import java.util.HashMap;
import java.util.Map;

import simulation.PerformanceRestrictedSimulatable;

/**
 * Basic implementation of a ProtocolHandler.  It has a protocol mapping set and 
 * can handle messages based upon that mapping.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public abstract class AbstractProtocolHandler<T> 
		extends PerformanceRestrictedSimulatable
		implements IProtocolHandler<T> {
	
/// Fields
	
	/** protocals installed on this adaptor. */
	protected Map<String, AbstractProtocolHandler> _protocalMappings;
	/** universal accept protocal, if no other protocal is present check this. */ 
	public static String DEFAULT_PROTOCAL = "UNIVERSAL_HANDLER";
	

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
	 * @see network.IProtocolHandler#handle(java.lang.Object)
	 */
	@Override
	public abstract void handle(T packetLikeObject);	
	
	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#getHandler(java.lang.String)
	 */
	@Override
	public AbstractProtocolHandler getHandler(String protocal) {
		IProtocolHandler result = _protocalMappings.get(protocal);
		return (AbstractProtocolHandler) 
			((result != null)
			? result 
			: _protocalMappings.get(AbstractProtocolHandler.DEFAULT_PROTOCAL));
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#getProtocol()
	 */
	@Override
	public abstract String getProtocol();
	
	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#install(network.IProtocolHandler)
	 */
	@Override
	public void install(IProtocolHandler handler) {
		install( handler, handler.getProtocol() );
	}
	
	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#install(network.IProtocolHandler, java.lang.String)
	 */
	@Override
	public void install(IProtocolHandler handler, String protocal) {
		if( handler instanceof AbstractProtocolHandler ) {
			_protocalMappings.put(protocal, (AbstractProtocolHandler)handler);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#uninstall(network.IProtocolHandler)
	 */
	@Override
	public void uninstall(IProtocolHandler handler) {
		_protocalMappings.values().remove(handler);
	}

	/*
	 * (non-Javadoc)
	 * @see network.IProtocolHandler#uninstall(java.lang.String)
	 */
	@Override
	public void uninstall(String protocal) {
		_protocalMappings.remove(protocal);
	}
}
