package simulation;

import java.util.EventObject;



public interface ISimulator {
	
	/**
	 * Adds an event listener.
	 * @param simulatorListener to add.
	 */
	public void addListener(ISimulatorListener simulatorListener);
	
	/**
	 * Removes an event listener.
	 * @param simulatorListener to remove.
	 */
	public void removeListener(ISimulatorListener simulatorListener);
	
	
	public void registerSimulatable(ISimulatable simulatable);
	
	public void unregisterSimulatable(ISimulatable simulatable);
	
	public void fireEvent(EventObject o);
	
	public void simulate(int time);
	
	public void start();
	
	public void step();
	
	public void pause();
	
	public void stop();

	public void signalDone(ISimulatable simulatable);
	
}
