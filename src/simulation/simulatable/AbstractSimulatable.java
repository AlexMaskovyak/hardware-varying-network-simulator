package simulation.simulatable;

import java.util.HashSet;
import java.util.Set;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulator.IDESimulator;
import simulation.simulator.ISimulator;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * Implements the ISimulatable interface, providing listener support for 
 * subclasses.
 * 
 * @author Alex Maskovyak
 *
 */
public abstract class AbstractSimulatable 
		implements ISimulatable {
	
/// Fields
	
	/** listeners to be informed of events. */
	protected Set<ISimulatableListener> _listeners;	
	/** simulator to which we are registered. */
	protected IDESimulator _simulator;
	/** time it takes to send a message (how far into the future to schedule it) */
	protected double _transitTime;
	
/// Construction
	
	/** Default constructor. */
	public AbstractSimulatable() {
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_listeners = new HashSet<ISimulatableListener>();
	}

	
/// Field accessor/mutator
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#setSimulator(simulation.ISimulator)
	 */
	public void setSimulator(ISimulator simulator) {
		_simulator = (IDESimulator) simulator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#getSimulator()
	 */
	public IDESimulator getSimulator() {
		return _simulator;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#getTransitTime()
	 */
	@Override
	public double getTransitTime() {
		return _transitTime;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#setTransitTime(double)
	 */
	@Override
	public void setTransitTime( double transitTime ) {
		_transitTime = transitTime;
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#setTransitTime(double)
	 */
	public void setTransitTime( Double transitTime ) {
		_transitTime = transitTime;
	}
	
	
/// Listener handling
	
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
	public void notifyListeners(ISimulatableEvent e) {
		// copy the set and enumerate over that so that listeners can unregister 
		// themselves as a part of their computation
		Set<ISimulatableListener> _listenersCopy = new HashSet<ISimulatableListener>(_listeners);
		for( ISimulatableListener listener : _listenersCopy ) {
			listener.update(e);
		}
	}

	
// Event handling
	
	@Override
	public abstract void handleEvent(IDiscreteScheduledEvent e);
	
	/*
	 * By default we can perform operations.
	 * (non-Javadoc)
	 * @see simulation.ISimulatable#canPerformOperation()
	 */
	@Override
	public boolean canPerformOperation() {
		return true;
	}
	
	/**
	 * Aids in event sending.
	 * @param destination to receive the message.
	 * @param message to send to the destination.
	 */
	protected void sendEvent( 
			ISimulatable destination, IDiscreteScheduledEvent.IMessage message ) {
		
		getSimulator().schedule(
				new DefaultDiscreteScheduledEvent (
					this, 
					destination, 
					getSimulator().getTime() + getTransitTime(), 
					getSimulator(), 
					message));
	}
}
