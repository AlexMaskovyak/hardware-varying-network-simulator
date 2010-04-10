package computation.listeners;

import simulation.simulatable.ISimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.SimulatableEvent;

/**
 * Events created by algorithms.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmSimulatableEvent 
		extends SimulatableEvent 
		implements ISimulatableEvent {

	/** algorithm eposh during which this occurred. */
	protected String _epoch;
	/** amount of data sent. */
	protected int _dataSent;
	/** amount of data received. */
	protected int _dataRecevied;
	/** amount of data stored. */
	protected int _dataStored;
	/** amount of data retrieved */
	
	/**
	 * Default constructor.
	 * @param source simulatable that created this event.
	 * @param time the event occurred.
	 * @param epoch time epoch area of simulation that it occurred within.
	 * @param dataSent amount.
	 * @param dataReceived amount.
	 * @param dataStored amount.
	 * @param dataRetrieved amount.
	 * @param controlSent amount.
	 * @param controlReceived amount.
	 */
	public AlgorithmSimulatableEvent(
			ISimulatable source, 
			double time,
			String epoch,
			int dataSent,
			int dataReceived,
			int dataStored,
			int controlSent,
			int controlReceived ) {
		super(source, time);
		// TODO Auto-generated constructor stub
	}

}
