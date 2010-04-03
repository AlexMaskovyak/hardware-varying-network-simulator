package timing;

import simulation.AbstractSimulatable;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.ISimulatorEvent;
import simulation.SimulatableEvent;

/**
 * Simulatable whose response rate is scalable.  As in, they perform a tick 
 * operation only on fraction of the ticks received.
 * @author Alex Maskovyak
 *
 */
public class TimeScalableSimulatable extends AbstractSimulatable implements ITimeScalable, ISimulatable {

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

	@Override
	public void handleTickEvent(ISimulatorEvent e) {
		// we've received a tick
		updateTickReceivedCount();
		
		// determine if we've received enough ticks
		if( !canPerformOperation() ) {
			// make the simulator happy
			super.handleTickEvent(e);
			return;
		}
		
		// reset the received count since we are acting		
		resetTicksReceivedCount();
		// notify everyone that we received an actionable tick
		super.notify(new SimulatableEvent(this, e.getEventTime()));
		// ensure the simulator is happy
		super.handleTickEvent(e);
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
}
