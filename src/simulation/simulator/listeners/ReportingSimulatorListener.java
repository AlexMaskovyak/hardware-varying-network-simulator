package simulation.simulator.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

/**
 * A nice little class to display what is happening to a node.
 * @author Alex Maskovyak
 *
 */
public class ReportingSimulatorListener 
		implements IDESimulatorListener {
	
/// Protected Fields
	
	/** area for output. */
	protected PrintWriter _out;
	
	
/// Construction 
	
	/**
	 * Constructor.
	 * @param logPath path to the file to which to output.
	 * @throws FileNotFoundException if the file at the path cannot be found.
	 */
	public ReportingSimulatorListener( String logPath ) 
			throws FileNotFoundException {
		this( new File( logPath ) );
	}
	
	/**
	 * Constructor.
	 * @param logfile to which to write.
	 * @throws FileNotFoundException if the file at the path cannot be found.
	 */
	public ReportingSimulatorListener( File logfile ) 
			throws FileNotFoundException {
		this( new FileOutputStream( logfile ) );
	}
	
	/**
	 * Constructor.
	 * @param out stream to which to display debug.
	 */
	public ReportingSimulatorListener(OutputStream out) {
		this(
			new PrintWriter(
				new BufferedWriter(
					new OutputStreamWriter(out))));
	}
	
	/**
	 * Constructor.
	 * @param writer to which to write output.
	 */
	public ReportingSimulatorListener(PrintWriter writer) {
		setWriter( writer );
	}


/// Accessor/Mutator
	
	/**
	 * Sets the writer to be used for reporting.
	 * @param writer to be used.
	 */
	public void setWriter(PrintWriter writer) {
		_out = writer;
	}
	
	/**
	 * Obtain the writer.
	 * @return writer.
	 */
	protected PrintWriter getWriter() {
		return _out;
	}
	
	
/// ISimulatable
	
	/**
	 * This should be overridden by subclasses.
	 * @param e event of which to be notified.
	 */
	public void update(ISimulatorEvent e) {
		if( e instanceof IDESimulatorEvent ) {
			_out.write( ((IDESimulatorEvent)e).getScheduledEvent().toString() );
			_out.write( "\n" );
			_out.flush();
		}
	}

	@Override
	public void pauseUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resumeUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulatableRegisteredUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulatableUnregisteredUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulatedUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopUpdate(ISimulatorEvent e) {
		// TODO Auto-generated method stub
		
	}
}
