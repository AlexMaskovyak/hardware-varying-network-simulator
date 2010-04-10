import simulation.simulator.NetworkSimulator;


/**
 * Create a random network topology.
 * @author Alex Maskovyak
 *
 */
public class TopologyCreator {

	/** network simulator. */
	protected NetworkSimulator _simulator;
	
	/** default constructor. */
	public TopologyCreator() {
		init();
	}
	
	/** externalize instantiation. */
	protected void init() {
		_simulator = new NetworkSimulator();
	}
	
	
}
