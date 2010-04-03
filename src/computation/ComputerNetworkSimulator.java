package computation;

import network.NetworkSimulator;
import simulation.ISimulator;

/**
 * ComputerNetworkSimulator creates ComputerNodes instead of normal Nodes.
 * @author Alex Maskovyak
 *
 */
public class ComputerNetworkSimulator extends NetworkSimulator implements
		ISimulator {

	/*
	 * (non-Javadoc)
	 * @see network.NetworkSimulator#init()
	 */
	protected void init() {
		super.init();
		_baseline = new ComputerNode();
	}
}
