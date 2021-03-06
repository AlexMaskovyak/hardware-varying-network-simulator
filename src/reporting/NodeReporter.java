package reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import simulation.ISimulatableEvent;

import network.IData;
import network.INode;
import network.INodeSimulatableListener;
import network.Node;
import network.NodeSimulatableEvent;
import network.NodeSimulatableListener;

/**
 * Aggregates multiple events to create lines in a data file for the activity if
 * a Node in the network.
 * @author Alex Maskovyak
 *
 */
public class NodeReporter 
		extends NodeSimulatableListener 
		implements INodeSimulatableListener {

/// Fields
	
	/** node we are installed upon. */
	protected INode _node;
	protected int _dataReceived;
	protected int _dataSent;

/// Construction
	
	/**
	 * Default constructor.
	 * @param node
	 * @throws Exception
	 */
	public NodeReporter(INode node, String outputPath) throws Exception {
		this(
			node, 
			new FileOutputStream(
				new File(
					String.format(
						"%s%s%s.txt", 
						outputPath, 
						File.separator, 
						node.getAddress()))));
	}
	
	/**
	 * Constructor.
	 * @param node onto which we are installed.
	 * @param out stream to report information.
	 */
	public NodeReporter(INode node, OutputStream out) {
		super(out);
		_node = node;
		_dataSent = 0;
		_dataReceived = 0;
	}
	
	@Override
	public void tickHandledUpdate(ISimulatableEvent e) {
		if(e instanceof NodeSimulatableEvent) {
			NodeSimulatableEvent ne = (NodeSimulatableEvent)e;
			
			_out.write(
					String.format( "%f %d %d\r\n", 
							e.getEventTime(), 
							_dataReceived, 
							_dataSent ));
			_out.flush();
		}
		
		// reset information
		_dataReceived = 0;
		_dataSent = 0;
	}

	@Override
	public void receiveUpdate(NodeSimulatableEvent e) {
		_dataReceived++;
	}

	@Override
	public void sendUpdate(NodeSimulatableEvent e) {
		_dataSent++;
	}

	@Override
	public void tickReceivedUpdate(ISimulatableEvent e) {
		
	}

	
}
