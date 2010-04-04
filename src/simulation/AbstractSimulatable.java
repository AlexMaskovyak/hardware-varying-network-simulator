package simulation;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements the ISimulatable interface, providing listener support for 
 * subclasses.
 * 
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractSimulatable 
		implements ISimulatable {
	
// Fields
	
	/** listeners to be informed of events. */
	protected Set<ISimulatableListener> _listeners;	
	/** simulator to which we are registered. */
	protected IDiscreteScheduledEventSimulator _simulator;
	
// Construction
	
	/** Default constructor. */
	public AbstractSimulatable() {
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_listeners = new HashSet<ISimulatableListener>();
	}

// Listener handling
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#addListener(simulation.ISimulatableListener)
	 */
	@Override
	public void addListener(ISimulatableListener listener) {
		_listeners.add(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#removeListener(simulation.ISimulatableListener)
	 */
	@Override
	public void removeListener(ISimulatableListener listener) {
		_listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#notify(simulation.ISimulatableEvent)
	 */
	@Override
	public void notify(ISimulatableEvent e) {
		// copy the set and enumerate over that so that listeners can unregister 
		// themselves as a part of their computation
		Set<ISimulatableListener> _listenersCopy = new HashSet<ISimulatableListener>(_listeners);
		for( ISimulatableListener listener : _listenersCopy ) {
			listener.tickHandledUpdate(e);
		}
	}

// Field accessor/mutator
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#setSimulator(simulation.ISimulator)
	 */
	public void setSimulator(ISimulator simulator) {
		_simulator = (IDiscreteScheduledEventSimulator) simulator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#getSimulator()
	 */
	public IDiscreteScheduledEventSimulator getSimulator() {
		return _simulator;
	}
	
// Event handling
	
	@Override
	public abstract void handleEvent(IDiscreteScheduledEvent e);
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#handleTickEvent(simulation.ISimulatorEvent)
	 */
	@Override
	public void handleTickEvent(ISimulatorEvent e) {
		e.getSimulator().signalDone(this);
	}
	
	/*
	 * By default we can perform operations.
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#canPerformOperation()
	 */
	@Override
	public boolean canPerformOperation() {
		return true;
	}
}
