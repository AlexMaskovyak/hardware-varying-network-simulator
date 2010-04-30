package computation.algorithms.listeners;

import simulation.simulatable.ISimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.SimulatableEvent;

/**
 * Events created by algorithms.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmEvent 
		extends SimulatableEvent 
		implements ISimulatableEvent {

	/** algorithm eposh during which this occurred. */
	protected String _epoch;
	/** amount of data sent. */
	protected int _dataSent;
	/** amount of data received. */
	protected int _dataReceived;
	/** amount of data stored. */
	protected int _dataStored;
	/** amount of data retrieved */
	protected int _dataRetrieved;
	/** amount of control info sent. */
	protected int _controlSent;
	/** amount of control info received. */
	protected int _controlReceived;
	
	/**
	 * Default constructor.
	 * @param source simulatable that created this event.
	 * @param time the event occurred.
	 * @param epoch time epoch area of simulation that it occurred within.
	 * @param dataSent amount.
	 * @param dataReceived amount.
	 * @param controlSent amount.
	 * @param controlReceived amount.
	 * @param dataStored amount.
	 * @param dataRetrieved amount.
	 */
	public AlgorithmEvent(
			ISimulatable source, 
			double time,
			String epoch,
			int dataSent,
			int dataReceived,
			int controlSent,
			int controlReceived,
			int dataStored,
			int dataRetrieved ) {
		super(source, time);
		_epoch = epoch;
		_dataSent = dataSent;
		_dataReceived = dataReceived;
		_controlSent = controlSent;
		_controlReceived = controlReceived;
		_dataStored = dataStored;
		_dataRetrieved = dataRetrieved;
	}
	
	/**
	 * Obtain the algorithm epoch during which this occurred.
	 * @return algorithm epoch during with this occurred (e.g., read, distr).
	 */
	public String getEpoch() {
		return _epoch;
	}
	
	/**
	 * Obtains the amount of data sent during this time.
	 * @return amount of data sent. 
	 */
	public int getDataSent() {
		return _dataSent;
	}
	
	/**
	 * Obtains the amount of data received during this time.
	 * @return amount of data received.
	 */
	public int getDataReceived() {
		return _dataReceived;
	}
	
	/**
	 * Obtains the amount of data stored for someone during this time.
	 * @return amount of data stored.
	 */
	public int getDataStored() {
		return _dataStored;
	}
	
	/**
	 * Obtains the amount of data retrieved from storage for a request during 
	 * this time.
	 * @return amount of data retrieved from storage.
	 */
	public int getDataRetrieved() {
		return _dataRetrieved;
	}
	
	/**
	 * Obtains the amount of control information sent during this time.
	 * @return amount of control related (algorithm overhead, not data) 
	 * information sent.
	 */
	public int getControlSent() {
		return _controlSent;
	}

	/**
	 * Obtains the amount of control information received during this time.
	 * @return amount of controol related (algorithm overhead, not data) 
	 * information sent.
	 */
	public int getControlReceived() {
		return _controlReceived;
	}
}
