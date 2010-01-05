package simulation;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements the ISimulatable interface, providing listener support for subclasses.
 * 
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractSimulatable implements ISimulatable {

	/** listeners to be informed of events. */
	protected Set<ISimulatableListener> _listeners;
	
	/**
	 * Default constructor.
	 */
	public AbstractSimulatable() {
		init();
	}
	
	/**
	 * Wraps all object instantiation code for the constructor for easier override-ability.  
	 */
	protected void init() {
		_listeners = new HashSet<ISimulatableListener>();
	}
	
	@Override
	public void addListener(ISimulatableListener listener) {
		_listeners.add(listener);
	}
	
	@Override
	public void removeListener(ISimulatableListener listener) {
		_listeners.remove(listener);
	}
	
	@Override
	public void handleTickEvent(ISimulatorEvent o) {
		for( ISimulatableListener listener : _listeners ) {
			listener.tickUpdate(new SimulatableEvent(this));
		}
	}
}
