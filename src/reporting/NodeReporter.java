package reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;

import simulation.ISimulatableEvent;

import network.INode;
import network.INodeSimulatableListener;
import network.Node;
import network.NodeSimulatableEvent;
import network.NodeSimulatableListener;

public class NodeReporter extends NodeSimulatableListener implements INodeSimulatableListener {

	protected INode _node;
	protected static String _outputPath = "";
	
	public NodeReporter(INode node) throws Exception {
		this(
			node, 
			new FileOutputStream(
				new File(String.format("%s%s%s", _outputPath, File.separator, node.getId()))));
	}
	
	public NodeReporter(INode node, OutputStream out) {
		super(out);
		_node = node;
	}
	
	@Override
	public void tickHandledUpdate(ISimulatableEvent e) {
		if(e instanceof NodeSimulatableEvent) {
			NodeSimulatableEvent ne = (NodeSimulatableEvent)e;
			
			_out.write(
					String.format( "%d %d %d\n", 
							e.getEventTime(), 
							(ne.getDataReceived() == null ) ? 0 : ne.getDataReceived().getID(), 
							(ne.getDataSent() == null ) ? 0 : ne.getDataSent().getID()) );
			_out.flush();
		}
	}

	@Override
	public void receiveUpdate(NodeSimulatableEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendUpdate(NodeSimulatableEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickReceivedUpdate(ISimulatableEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
