package network;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;

/**
 * A nice little class to display what is happening to a node.
 * @author Alex Maskovyak
 *
 */
public class NodeSimulatableListener implements ISimulatableListener, INodeSimulatableListener {

	protected PrintWriter _out;
	
	public NodeSimulatableListener(OutputStream out) {
		_out = 
			new PrintWriter(
				new BufferedWriter(
					new OutputStreamWriter(out)));
	}
	
	@Override
	public void tickReceivedUpdate(ISimulatableEvent e) {
		if( e.getSimulatable() instanceof INode ) {
			_out.printf("%s received tick #%d.\n", ((INode)e.getSimulatable()).getId(), e.getEventTime());
			_out.flush();
		}
	}
	
	@Override
	public void tickHandledUpdate(ISimulatableEvent e) {
		if( e.getSimulatable() instanceof INode ) {
			_out.printf("%s handled tick #%d.\n", ((INode)e.getSimulatable()).getId(), e.getEventTime());
			_out.flush();
		}
	}

	@Override
	public void receiveUpdate(NodeSimulatableEvent e) {
		_out.printf("%s received data: %d.\n", ((INode)e.getSimulatable()).getId(), e.getDataReceived().getID());
		_out.flush();
	}

	@Override
	public void sendUpdate(NodeSimulatableEvent e) {
		_out.printf("%s sent data: %d.\n", ((INode)e.getSimulatable()).getId(), e.getDataSent().getID());
		_out.flush();
	}	
}
