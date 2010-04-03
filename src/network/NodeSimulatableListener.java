package network;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;

/**
 * A nice little class to display what is happening to a node.
 * @author Alex Maskovyak
 *
 */
public class NodeSimulatableListener implements ISimulatableListener, INodeSimulatableListener {

	/** area for output. */
	protected PrintWriter _out;
	
	/**
	 * Default constructor.
	 * @param out to which to display debug.
	 */
	public NodeSimulatableListener(OutputStream out) {
		_out = 
			new PrintWriter(
				new BufferedWriter(
					new OutputStreamWriter(out)));
	}
	
	@Override
	public void tickReceivedUpdate(ISimulatableEvent e) {
		if( e.getSource() instanceof INode ) {
			_out.printf("%s received tick #%f.\n", 
					((INode)e.getSource()).getAddress(), 
					e.getEventTime());
			_out.flush();
		}
	}
	
	@Override
	public void tickHandledUpdate(ISimulatableEvent e) {
		if( e.getSource() instanceof INode ) {
			_out.printf("%s handled tick #%f.\n", 
					((INode)e.getSource()).getAddress(), 
					e.getEventTime());
			_out.flush();
		}
	}

	@Override
	public void receiveUpdate(NodeSimulatableEvent e) {
		_out.printf("%s received packet: %d.\n", 
				((INode)e.getSource()).getAddress(), 
				e.getPacket().getSequence());
		_out.flush();
	}

	@Override
	public void sendUpdate(NodeSimulatableEvent e) {
		_out.printf("%s sent data: %d.\n", 
				((INode)e.getSource()).getAddress(), 
				e.getPacket().getSequence());
		_out.flush();
	}	
}
