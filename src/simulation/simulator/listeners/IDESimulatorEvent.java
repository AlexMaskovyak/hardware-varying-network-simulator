package simulation.simulator.listeners;

import simulation.event.IDEvent;

/**
 * IDESimulator event generated object.
 * @author Alex Maskovyak
 *
 */
public interface IDESimulatorEvent extends ISimulatorEvent {

	/**
	 * Obtains the Discrete scheduled event that this simulator is reporting.
	 * @return the discrete scheduled event that sparked this ide event.
	 */
	public IDEvent getScheduledEvent();
}
