
import java.io.File;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import computation.HardwareComputerNode;
import computation.algorithms.DummyAlgorithm;
import configuration.ConfigurationManager;

import simulation.simulatable.ISimulatable;
import simulation.simulator.ComputerNetworkSimulator;
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
		PropertyConfigurator.configure("log4j.properties"); // disable it
		if( args.length != 2 ) {
			System.err.println( "java -jar hardwareSimulation.jar <path to config file>" );
		}
		
		// inspect parent directory
		// get last directory with name run_#
		// increment #
		//ConfigurationSetManager setManager = new ConfigurationSetManager();
		
		ConfigurationManager configManager = new ConfigurationManager( args[ 0 ], args[ 0 ] + File.separator + args[ 1 ], "run_" );
		File outputPath = configManager.makeNewRunDirectory();
		
		
		System.out.println("starting");
		
		
		ComputerNetworkSimulator sim = configManager.configureSimulator(); //new ComputerNetworkSimulator();
		sim.setOutputPath( outputPath.getAbsolutePath() );
		sim.addAlgorithmListeners();
		
		Thread t = new Thread((Runnable)sim);
		t.start();
		sim.start();
		//sim.start();
		HardwareComputerNode c = (HardwareComputerNode)sim.getClient();
		DummyAlgorithm alg = (DummyAlgorithm)c.getInstalledAlgorithm();
		//sim.getDataAmount();
		//alg.setInitialData(data)
		alg.setAddressRange( new Address(1), new Address(5));
		//n0.send(new IMessage() {}, new Address(7));
		c.start();
		
		//IHardwareComputer n0 = (IHardwareComputer)sim.createNode();
		//System.out.println("\n");
		//n0.install( new RandomDistributionAlgorithm( n0 ) );
		/*List<ISimulatable> simulatables = sim.createRandomlyConnectedNodes( 200, 100 ); //sim.createSeriesOfConnectedNodes(8);
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
		
		
		Thread.currentThread().sleep( 4000 );
		//configManager.makeAveragesDirectory();
		
		//sim.resume();
		 * 
		 */
	}
}