package network.listeners;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import network.entities.INode;

import simulation.simulatable.AbstractSimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulatable.listeners.ReportingSimulatableListener;

/**
 * A nice little class to display what is happening to a node.
 * @author Alex Maskovyak
 *
 */
public class NodeSimulatableListener 
		extends ReportingSimulatableListener
		implements ISimulatableListener, INodeSimulatableListener {

	/**
	 * Default constructor.
	 * @param out to which to display debug.
	 */
	public NodeSimulatableListener(OutputStream out) {
		super( out );
	}
	
	public void tickReceivedUpdate(ISimulatableEvent e) {
		if( e.getSource() instanceof INode ) {
			getWriter().printf("%s received tick #%f.\n", 
					((INode)e.getSource()).getAddress(), 
					e.getEventTime());
			getWriter().flush();
		}
	}
	
	public void tickHandledUpdate(ISimulatableEvent e) {
		if( e.getSource() instanceof INode ) {
			getWriter().printf("%s handled tick #%f.\n", 
					((INode)e.getSource()).getAddress(), 
					e.getEventTime());
			getWriter().flush();
		}
	}

	public void receiveUpdate(NodeSimulatableEvent e) {
		getWriter().printf("%s received packet: %d.\n", 
				((INode)e.getSource()).getAddress(), 
				e.getPacket().getSequence());
		getWriter().flush();
	}

	public void sendUpdate(NodeSimulatableEvent e) {
		getWriter().printf("%s sent data: %d.\n", 
				((INode)e.getSource()).getAddress(), 
				e.getPacket().getSequence());
		getWriter().flush();
	}

}
