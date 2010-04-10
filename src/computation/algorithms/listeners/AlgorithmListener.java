package computation.algorithms.listeners;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;

import network.entities.INode;

import computation.algorithms.IAlgorithm;

import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulatable.listeners.ReportingAbstractSimulatableListener;

/**
 * Listener for Algorithm events.
 * @author Alex Maskovyak
 *
 */
public class AlgorithmListener 
		extends ReportingAbstractSimulatableListener
		implements IAlgorithmListener, ISimulatableListener {

/// Control values
	
	/** determine whether to output the header, or if it has already been 
	 * output. */
	protected boolean _outputHeader;


/// Construction
	
	/**
	 * Constructor.
	 * @param configDirectory containing the configuration file for the network.
	 * @param algorithm to which we are being installed.
	 * @throws FileNotFoundException if the constructed file-path to which we 
	 * are storing our logging information does not exist.
	 */
	public AlgorithmListener(String configDirectory, IAlgorithm algorithm) 
			throws FileNotFoundException {
		super( String.format(
			"%s%s%s.log",
			configDirectory,
			File.separator,
			((INode)algorithm.getComputer()).getAddress()) );
	}
	
	/**
	 * Default constructor.
	 * @param out stream to use for writing.
	 */
	public AlgorithmListener(OutputStream out) {
		super(out);
	}

	/*
	 * (non-Javadoc)
	 * @see computation.listeners.IAlgorithmSimulatableListener#update(computation.listeners.AlgorithmSimulatableEvent)
	 */
	@Override
	public void update(AlgorithmEvent e) {
		if( _outputHeader ) { 
			getWriter().write(
				"Epoch Time Date-Sent Data-Received Control-Set " +
				"Control-Received Data-Stored Data-Retrieved\n" ); 
		}
		getWriter().write(
			String.format("%s %f %d %d %d %d %d %d\n", 
				e.getEpoch(), 
				e.getEventTime(),
				e.getDataSent(),
				e.getDataReceived(), 
				e.getControlSent(),
				e.getControlReceived(),
				e.getDataStored(),
				e.getDataRetrieved() ) );
		getWriter().flush();
	}
	
	/**
	 * Override to reroute to AlgorithmSimulatableEvent handling method.
	 */
	@Override
	public void update(ISimulatableEvent e) {
		if( e instanceof AlgorithmEvent ) {
			update( (AlgorithmEvent)e );
		}
	}
}
