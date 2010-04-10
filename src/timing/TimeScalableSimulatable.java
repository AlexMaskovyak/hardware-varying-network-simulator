package timing;

import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulatable.listeners.SimulatableEvent;
import simulation.simulator.IDESimulator;
import simulation.simulator.ISimulator;
import simulation.simulator.listeners.ISimulatorEvent;

/**
 * Simulatable whose response rate is scalable.  As in, they perform a tick 
 * operation only on fraction of the ticks received.
 * @author Alex Maskovyak
 *
 */
public class TimeScalableSimulatable extends PerformanceRestrictedSimulatable implements ITimeScalable, ISimulatable {

	/** baseline performance in operations per tick. */
	protected int _baselinePerformance;
	/** scaled performance in ticks per operation, relative to all scalables
	 *  being simulated. */
	protected int _scaledPerformance;
	
	/** number of ticks received.  when ticks received is equivalent to
	 *  scaled performance, an operation can be completed. */
	protected int _ticksReceived;
	
	
	/**
	 * Default constructor.
	 * @param baselinePerformance
	 */
	public TimeScalableSimulatable(int baselinePerformance) {
		_baselinePerformance = baselinePerformance;
		_scaledPerformance = _baselinePerformance;
	}
	
	@Override
	public int getBaselinePerformance() {
		return _baselinePerformance;
	}

	@Override
	public void scaleTickResponse(int ticks) {
		_scaledPerformance = ticks;
		_ticksReceived = ticks;
	}

	@Override
	public int getScaledPerformance() {
		return _scaledPerformance;
	}

	public void handleEvent(ISimulatorEvent e) {
		// we've received a tick
		updateTickReceivedCount();
		
		// determine if we've received enough ticks
		if( !canPerformOperation() ) {
			// make the simulator happy
			//super.handleEvent(e);
			return;
		}
		
		// reset the received count since we are acting		
		resetTicksReceivedCount();
		// notify everyone that we received an actionable tick
		super.notifyListeners(new SimulatableEvent(this, e.getEventTime()));
		// ensure the simulator is happy
		//super.handleTickEvent(e);
	}

	@Override
	public boolean canPerformOperation() {
		return (_ticksReceived == _scaledPerformance);
	}
	
	/**
	 * Updates the count of ticks received to the threshold value of scaled
	 * performance.
	 */
	protected void updateTickReceivedCount() {
		if( _ticksReceived < _scaledPerformance ) {
			++_ticksReceived;
		}
	}
	
	/**
	 * Sets the Scalable to the lowest state of ability/being computational
	 * capable to act.
	 */
	protected void resetTicksReceivedCount() {
		_ticksReceived = 0;
	}

	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ISimulatableListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IDESimulator getSimulator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyListeners(ISimulatableEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(ISimulatableListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSimulator(ISimulator simulator) {
		// TODO Auto-generated method stub
		
	}
}
