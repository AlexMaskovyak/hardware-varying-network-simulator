package simulation.simulator.listeners;

/**
 * Listener for a DiscreteEventSimulator.
 * @author Alex Maskovyak
 *
 */
public interface IDESimulatorListener extends ISimulatorListener {

	/**
	 * Event executed by this simulator.
	 * @param e ISimulatorEvent containing information about this event.
	 */
	public void update(ISimulatorEvent e);
}
