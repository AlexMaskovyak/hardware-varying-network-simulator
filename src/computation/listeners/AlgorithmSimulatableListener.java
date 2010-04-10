package computation.listeners;

import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

/**
 * Listener for Algorithm events.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmSimulatableListener 
		implements ISimulatableListener {


	public AlgorithmSimulatableListener() {
		
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
}
