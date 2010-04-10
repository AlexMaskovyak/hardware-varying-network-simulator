package simulation.simulatable.listeners;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import network.entities.INode;

import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

/**
 * A nice little class to display what is happening to a node.
 * @author Alex Maskovyak
 *
 */
public abstract class ReportingAbstractSimulatableListener 
		implements ISimulatableListener {

	/** area for output. */
	protected PrintWriter _out;
	
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
