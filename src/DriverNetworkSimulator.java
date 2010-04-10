
import java.util.Collection;
import java.util.List;
import java.util.Set;

import computation.Data;
import computation.HardwareComputerNode;
import computation.IData;
import computation.algorithms.DummyAlgorithm;

import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.ISimulatable;
import simulation.simulator.ComputerNetworkSimulator;
import simulation.simulator.ISimulator;
import simulation.simulator.NetworkSimulator;
import network.communication.Address;
import network.entities.INode;


/**
 * Creates a simulator from a configuration file and runs a simulation.
 * 
 * @author Alex Maskovyak
 *
 */
public class DriverNetworkSimulator {

	/**
	 * Read in the configuration file.
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		if( args.length != 0 ) {
			System.err.println( "java -jar program.jar <path to config file>" );
		}
		
		System.out.println("starting");
		
		
		NetworkSimulator sim = new ComputerNetworkSimulator();
		//IHardwareComputer n0 = (IHardwareComputer)sim.createNode();
		//System.out.println("\n");
		//n0.install( new RandomDistributionAlgorithm( n0 ) );
		List<ISimulatable> simulatables = sim.createRandomlyConnectedNodes( 200, 100 ); //sim.createSeriesOfConnectedNodes(8);
		System.out.println(simulatables.size());
		INode n0 = null;
		for( ISimulatable simulatable : simulatables ) {
			if( simulatable instanceof INode ) {
				if( ((INode)simulatable).getAddress().equals(new Address(0)) ) {
					n0 = (INode)simulatable;
				}
			}
		}
		System.out.println(n0.getAddress());
		
		
		Thread t = new Thread((Runnable)sim);
		t.start();
		sim.start();
		//sim.start();
		HardwareComputerNode c = (HardwareComputerNode)n0;
		DummyAlgorithm alg = (DummyAlgorithm)c.getInstalledAlgorithm();
		alg.setAddressRange( new Address(1), new Address(5));
		//n0.send(new IMessage() {}, new Address(7));
		c.start();
		//sim.pause();
		
		//Thread.currentThread().sleep( 4000);
		//sim.resume();
	}
}