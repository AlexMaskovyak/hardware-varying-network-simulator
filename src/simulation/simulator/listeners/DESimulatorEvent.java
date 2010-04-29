package simulation.simulator.listeners;

import simulation.event.IDEvent;
import simulation.simulator.ISimulator;

/**
 * Event created by a Simulator.
 * @author Alex Maskovyak
 *
 */
public class DESimulatorEvent 
		extends SimulatorEvent
		implements ISimulatorEvent, IDESimulatorEvent {
	
/// Fields
	
	/** discrete event. */
	protected IDEvent _dEvent;
	

/// Construction
	
	/**
	 * Default constructor.
	 * @param source of this event.
	 * @param time of the event.
	 * @param dEvent the discrete event that prompted this simulator event.
	 */
	public DESimulatorEvent(
			ISimulator source, 
			double time, 
			IDEvent dEvent ) {
		super( source, time );
		_dEvent = dEvent;
	}
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulator.listeners.IDESimulatorEvent#getScheduledEvent()
	 */
	@Override
	public IDEvent getScheduledEvent() {
		return _dEvent;
	}
}