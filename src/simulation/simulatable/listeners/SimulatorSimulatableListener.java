package simulation.simulatable.listeners;

import simulation.simulator.ISimulator;

/**
 * Standard SimulatableListeners for a Simulator.
 * @author Alex Maskovyak
 *
 */
public class SimulatorSimulatableListener implements ISimulatableListener {

	/** ISimulator which is to handle this event. */
	protected ISimulator _delegate;
	
	/**
	 * Default constructor.
	 * @param delegate ISimulator which is to be informed that a Simulatable has
	 * completed all operations on its tick.
	 */
	public SimulatorSimulatableListener(ISimulator delegate) {
		_delegate = delegate;
	}
	
	/**
	 * Obtains the ISimulator delegate.
	 * @return ISimulator delegate.
	 */
	public ISimulator getDelegate() {
		return _delegate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatableListener#tickReceivedUpdate(simulation.ISimulatableEvent)
	 */
	@Override
	public void tickReceivedUpdate(ISimulatableEvent e) {
		// the Simulator doesn't care if the node has gotten the tick since we 
		// all live on the same system, it only cares if the tick has been 
		// handled
	}

	/*
	 * (non-Javadoc)
	 * @see simulation.ISimulatableListener#tickHandledUpdate(simulation.ISimulatableEvent)
	 */
	@Override
	public void tickHandledUpdate(ISimulatableEvent e) {
		_delegate.signalDone(e.getSource());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof SimulatorSimulatableListener) {
			return equals((SimulatorSimulatableListener)o);
		}
		return false;
	}
	
	/**
	 * Compares another SimulatorSimulatableListener to this one.
	 * @param listener to which to compare.
	 * @return true if both listeners have the same delegate, false otherwise.
	 */
	public boolean equals(SimulatorSimulatableListener listener) {
		return (getDelegate() == listener.getDelegate());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return _delegate.hashCode();
	}
}
