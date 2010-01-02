package simulation;

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
	 * @param delegate ISimulator which is to be informed that a Simulatable has completed all operations on its tick.
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
	
	@Override
	public void tickUpdate(ISimulatableEvent e) {
		_delegate.signalDone(e.getSimulatable());
	}
	
	public boolean equals(Object o) {
		if(o instanceof SimulatorSimulatableListener) {
			return equals((SimulatorSimulatableListener)o);
		}
		return false;
	}
	
	public boolean equals(SimulatorSimulatableListener listener) {
		return (getDelegate() == listener.getDelegate());
	}
	
	public int hashCode() {
		return _delegate.hashCode();
	}
}
