package simulation.simulator.listeners;

import simulation.simulatable.ISimulatable;

/**
 * Standard implementation of ISimulatableListener for a ISimulatable object.
 * @author Alex Maskovyak
 *
 */
public class SimulatableSimulatorListener implements ISimulatorListener {

	/** ISimulatable which is to receive the tick event. */
	protected ISimulatable _delegate;
	
	/**
	 * Default constructor.
	 * @param delegate ISimulatable which is to respond to tick events.
	 */
	public SimulatableSimulatorListener(ISimulatable delegate) {
		_delegate = delegate;
	}

	@Override
	public void tickUpdate(ISimulatorEvent e) {
		_delegate.handleTickEvent(e);
	}
	
	/**
	 * Obtains the ISimulator delegate.
	 * @return ISimulator delegate.
	 */
	public ISimulatable getDelegate() {
		return _delegate;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof SimulatableSimulatorListener) {
			return equals((SimulatableSimulatorListener)o);
		}
		return false;
	}
	
	public boolean equals(SimulatableSimulatorListener listener) {
		return (getDelegate() == listener.getDelegate());
	}
	
	@Override
	public int hashCode() {
		return _delegate.hashCode();
	}
	
	@Override
	public void pauseUpdate(ISimulatorEvent e) {}

	@Override
	public void resumeUpdate(ISimulatorEvent e) {}

	@Override
	public void simulatableRegisteredUpdate(ISimulatorEvent e) {}

	@Override
	public void simulatableUnregisteredUpdate(ISimulatorEvent e) {}

	@Override
	public void simulatedUpdate(ISimulatorEvent e) {}

	@Override
	public void startUpdate(ISimulatorEvent e) {}

	@Override
	public void stepUpdate(ISimulatorEvent e) {}

	@Override
	public void stopUpdate(ISimulatorEvent e) {}
}
