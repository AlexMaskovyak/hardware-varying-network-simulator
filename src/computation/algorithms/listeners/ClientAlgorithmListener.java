package computation.algorithms.listeners;

import java.io.OutputStream;
import java.io.PrintWriter;

import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulatable.listeners.ReportingSimulatableListener;

/**
 * Listener for Algorithm events for Algorithms performing the client role.
 * @author Alex Maskovyak
 *
 */
public class ClientAlgorithmListener
		extends AlgorithmListener
		implements IAlgorithmListener, ISimulatableListener {

/// Fields
	
	/** time when local reading began. */
	protected double _localTimeStart;
	/** time when local reading ended, (i.e. last local response was sent.) */
	protected double _localTimeEnd;
	/** time when remote reading began. */
	protected double _remoteTimeStart;
	/** time when remote reading ended, (i.e. last remote response received.)*/
	protected double _remoteTimeEnd;
	
	/**
	 * Constructor.
	 * @param out stream to use for writing aggregated information.
	 */
	public ClientAlgorithmListener(OutputStream out) {
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
}
