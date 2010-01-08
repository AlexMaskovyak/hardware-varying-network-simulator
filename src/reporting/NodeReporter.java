package reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;

import network.AbstractNodeSimulatableEventListener;
import network.INode;
import network.Node;
import network.NodeSimulatableEvent;

public class NodeReporter extends AbstractNodeSimulatableEventListener {

	protected INode _node;
	protected static String _outputPath = "";
	protected PrintWriter _out;
	
	public NodeReporter(INode node) throws Exception {
		this(node, 
				new PrintWriter( 
					new BufferedWriter( 
						new FileWriter( 
							new File( 
								String.format(
									"%s%s%s", 
									_outputPath, 
									File.separator, 
									node.getId() ) ) ) ) ) );		
	}
	
	public NodeReporter(INode node, PrintWriter out) {
		_node = node;
		_out = out;
	}
	
	@Override
	public void tickUpdate(NodeSimulatableEvent e) {
		_out.write(
			String.format( "%d %d %d\n", 
					e.getEventTime(), 
					(e.getDataReceived() == null ) ? 0 : e.getDataReceived().length, 
					(e.getDataSent() == null ) ? 0 : e.getDataSent().length) );
		_out.flush();
	}

	
}
