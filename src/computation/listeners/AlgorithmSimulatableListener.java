package computation.listeners;

import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

/**
 * Listener for Algorithm events.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmSimulatableListener 
		extends AbstractSimulatable
		implements ISimulatableListener {

	
	public AlgorithmSimulatableListener() {
		
	}
	
	/**	 */
	protected void init() {
		
	}
	
	/**
	 * 
	 * @param e
	 */
	public void update(AlgorithmSimulatableEvent e) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.listeners.ISimulatableListener#tickHandledUpdate(simulation.simulatable.listeners.ISimulatableEvent)
	 */
	@Override
	public void tickHandledUpdate(ISimulatableEvent e) {}

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.listeners.ISimulatableListener#tickReceivedUpdate(simulation.simulatable.listeners.ISimulatableEvent)
	 */
	@Override
	public void tickReceivedUpdate(ISimulatableEvent e) {}

	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ISimulatableEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyListeners() {
		// TODO Auto-generated method stub
		
	}
}
