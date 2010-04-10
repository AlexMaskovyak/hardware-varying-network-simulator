package simulation.simulatable.listeners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

/**
 * A nice little class to display what is happening to a node.
 * @author Alex Maskovyak
 *
 */
public abstract class ReportingAbstractSimulatableListener 
		implements ISimulatableListener {
	
/// Protected Fields
	
	/** area for output. */
	protected PrintWriter _out;
	
	
/// Construction 
	
	/**
	 * Constructor.
	 * @param logPath path to the file to which to output.
	 * @throws FileNotFoundException if the file at the path cannot be found.
	 */
	public ReportingAbstractSimulatableListener( String logPath ) 
			throws FileNotFoundException {
		this( new File( logPath ) );
	}
	
	/**
	 * 
	 * @param logfile
	 * @throws FileNotFoundException 
	 */
	public ReportingAbstractSimulatableListener( File logfile ) 
			throws FileNotFoundException {
		this( new FileOutputStream( logfile ) );
	}
	
	/**
	 * Default constructor.
	 * @param out to which to display debug.
	 */
	public ReportingAbstractSimulatableListener(OutputStream out) {
		_out = 
			new PrintWriter(
				new BufferedWriter(
					new OutputStreamWriter(out)));
	}
	
	/**
	 * This should be overridden by subclasses.
	 * @param e event of which to be notified.
	 */
	public void update(ISimulatableEvent e) {
		_out.write( e.toString() );
	}
	
	/**
	 * Obtain the writer.
	 * @return writer.
	 */
	protected PrintWriter getWriter() {
		return _out;
	}
}
