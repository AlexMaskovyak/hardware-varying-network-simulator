package network;

import java.util.HashMap;
import java.util.Map;

import simulation.AbstractSimulatable;

/**
 * Basic implementation of a PacketHandler.  It has a protocal mapping set and can handle packets
 * based upon that mapping.
 * @author Alex Maskovyak
 *
 * @param <T>
 */
public abstract class AbstractProtocolHandler<T> 
		extends AbstractSimulatable
		implements IProtocolHandler<T> {

	/** protocals installed on this adaptor. */
	protected Map<String, AbstractProtocolHandler> _protocalMappings;
	/** universal accept protocal, if no other protocal is present check this. */ 
	public static String DEFAULT_PROTOCAL = "UNIVERSAL_HANDLER";
	
	
	/** Default constructor. */
	protected AbstractProtocolHandler() {
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		super.init();
		_protocalMappings = new HashMap<String, AbstractProtocolHandler>();
	}
	
	@Override
	public abstract void handle(T packetLikeObject);	
	
	@Override
	public AbstractProtocolHandler getHandler(String protocal) {
		IProtocolHandler result = _protocalMappings.get(protocal);
		return (AbstractProtocolHandler) 
			((result != null)
			? result 
			: _protocalMappings.get(AbstractProtocolHandler.DEFAULT_PROTOCAL));
	}
	
	@Override
	public abstract String getProtocal();
	
	@Override
	public void install(IProtocolHandler handler, String protocal) {
		if( handler instanceof AbstractProtocolHandler ) {
			_protocalMappings.put(protocal, (AbstractProtocolHandler)handler);
		}
	}

	@Override
	public void uninstall(IProtocolHandler handler) {
		_protocalMappings.values().remove(handler);
	}

	@Override
	public void uninstall(String protocal) {
		_protocalMappings.remove(protocal);
	}
}
