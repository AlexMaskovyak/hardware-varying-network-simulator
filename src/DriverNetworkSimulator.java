
import java.io.File;

import javax.management.OperationsException;

import org.apache.log4j.PropertyConfigurator;

import computation.HardwareComputerNode;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.IAlgorithm;
import computation.algorithms.dummy.DummyAlgorithm;
import computation.hardware.Cache;
import computation.hardware.Harddrive;
import configuration.ConfigurationManager;
import configuration.ConfigurationSetManager;

import simulation.simulator.ComputerNetworkSimulator;
import network.entities.ConnectionAdaptor;
import network.entities.ConnectionMedium;
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
		PropertyConfigurator.configure( "log4j.properties" ); // disable it
		if( args.length != 2 ) {
			System.err.println( "java -jar hardwareSimulation.jar <run amount> <configuration set collection directory>" );
		}
		
		File collectionDirectory = null;
		int runs = 0;
		
		try {
			collectionDirectory = new File( args[ 0 ] );
			runs = Integer.parseInt( args[ 1 ] );
		} catch( Exception e ) {
			System.err.println( "java -jar hardwareSimulation.jar <run amount> <configuration set collection directory>" );
			return;
		}

		File[] setDirectories = collectionDirectory.listFiles();
		System.out.println( "Processing Collection of Configuration Sets." );
		for( File setDirectory : setDirectories ) {
			ConfigurationSetManager setManager = new ConfigurationSetManager( setDirectory.getAbsoluteFile() );
			//if( !((new File("C:\\Users\\user\\workspaces\\gradproject\\configurations-algorithm2\\config_set_5_cache_size")).equals( setDirectory ) ) ) {
			//	System.out.println( setDirectory );
			//	continue;
			//}
			System.out.printf( "Processing Configuration Set %s.\n", setDirectory );
			for( ConfigurationManager configManager : setManager.getConfigurationManagers() ) {
				for( int i = 0; i < runs; ++i ) {
					
					/*if( (new File("C:\\Users\\user\\workspaces\\gradproject\\configurations-algorithm2\\config_set_2_server_count\\config_10.cfg").equals( configManager.getConfigFile() ) ) ) {
						System.out.println( "continue: " + configManager.getConfigFile() );
						continue;
					}
					if( (new File("C:\\Users\\user\\workspaces\\gradproject\\configurations-algorithm2\\config_set_2_server_count\\config_1.cfg").equals( configManager.getConfigFile() ) ) ) {
						System.out.println( "continue: " + configManager.getConfigFile() );
						continue;
					}*/
					
					
					File outputPath = configManager.makeNewRunDirectory();
					System.out.println( "===" );
					System.out.printf( "Configuration file: '%s'\n", configManager.getConfigFile() );
					System.out.printf( "Runs directory: '%s'\n", configManager.getRunsDirectory() );
					System.out.printf( "Run number: '%d'\n", configManager.getMaxRunNumber() );
					
					System.out.println( "Starting." );
					
					ComputerNetworkSimulator sim = configManager.configureSimulator();
					//sim.addListener(new ReportingSimulatorListener(new File("C:\\Users\\user\\workspaces\\gradproject\\hardware-varying-network-simulator-5\\output\\sim.txt")));
					sim.setOutputPath( outputPath.getAbsolutePath() );
					sim.addAlgorithmListeners();
					
					Thread t = new Thread( new SimRunnable( configManager, sim ) );
					t.start();
					sim.start();
					
			
					HardwareComputerNode c = (HardwareComputerNode)sim.getClient();
					c.start();
					t.join();
					
					System.out.println( "Finished." );
				}
			}
		}
	}
	
	/**
	 * Read in the configuration file.
	 * @param args
	 */
	public static void mainOld(String... args) throws Exception {
		PropertyConfigurator.configure("log4j.properties"); // disable it
		if( args.length != 3 ) {
			System.err.println( "java -jar hardwareSimulation.jar <path to config file> <config file name>" );
		}
		
		String runsDirectory = null;
		String configFile = null;
		int runs = 0;
		
		try {
			runsDirectory = args[ 0 ]; 
			configFile = args[ 1 ]; //runsDirectory + File.separator + args[ 1 ]; 
			runs = Integer.parseInt( args[ 2 ] );
		} catch( Exception e ) {
			System.err.println( "java -jar hardwareSimulation.jar <path to config file> <config file name> <runs>" );
			return;
		}
		
		ConfigurationManager configManager = new ConfigurationManager( runsDirectory, configFile );
		
		for( int i = 0; i < runs; ++i ) {
			File outputPath = configManager.makeNewRunDirectory();		
			System.out.println("starting");
			
			ComputerNetworkSimulator sim = configManager.configureSimulator();
			//sim.addListener(new ReportingSimulatorListener(new File("C:\\Users\\user\\workspaces\\gradproject\\hardware-varying-network-simulator-5\\output\\sim.txt")));
			sim.setOutputPath( outputPath.getAbsolutePath() );
			sim.addAlgorithmListeners();
			
			Thread t = new Thread( new SimRunnable( configManager, sim ) );
			t.start();
			sim.start();
			
	
			HardwareComputerNode c = (HardwareComputerNode)sim.getClient();
			c.start();
			t.join();
		}
	}
	
	public static void oldMain( String... args) throws OperationsException, InterruptedException {
		
		//IHardwareComputer n0 = (IHardwareComputer)sim.createNode();
		//System.out.println("\n");
		//n0.install( new RandomDistributionAlgorithm( n0 ) );
		
		// create the simulator
		ComputerNetworkSimulator sim = new ComputerNetworkSimulator();
		
		// create network level items
		HardwareComputerNode node = new HardwareComputerNode();
		ConnectionAdaptor adaptor = new ConnectionAdaptor();
		adaptor.setMaxAllowedOperations( 100 );
		adaptor.setRefreshInterval( 1.0 );
		ConnectionMedium medium = new ConnectionMedium();
		medium.setTransitTime( 2.0 );
		medium.setMaxAllowedOperations( 200 );
		

		// add them to the simulator
		sim.setBaseNode( node );
		sim.setBaseAdaptor( adaptor );
		sim.setBaseMedium( medium );
		
		
		// create node level entities
		Harddrive harddrive = new Harddrive();
		harddrive.setMaxAllowedOperations( 100 );
		harddrive.setRefreshInterval( 1.0 );
		harddrive.setCapacity( 100000 );
		harddrive.setTransitTime( 10.0 );
		
		Cache cache = new Cache();
		cache.setMaxAllowedOperations( 1000 );
		cache.setRefreshInterval( 1.0 );
		cache.setCapacity( 10 );
		cache.setTransitTime( 1.0 );

		AbstractAlgorithm algorithm = new DummyAlgorithm();
		algorithm.setServerCount( 10 );
		algorithm.setDataAmount( 1000000 );

		
		// set node entities
		node.setHarddrive( harddrive );
		node.setCache( cache );
		node.install( (IAlgorithm)algorithm );
		
		// create nodes, connect nodes
		INode[] nodes = sim.createNodes( 100 );
		sim.connectRandomly( 100, nodes );
		
		// setup client
		HardwareComputerNode client = (HardwareComputerNode)nodes[ 0 ];
		Harddrive clientHarddrive = (Harddrive)harddrive.clone();
		clientHarddrive.setCapacity( 10000000 );		
		client.setHarddrive( clientHarddrive );

		// setup reporters
		sim.setOutputPath( "configSet1/config1/" );
		sim.addAlgorithmListeners();
		
		
		// start simulation
		(new Thread((Runnable)sim)).start();
		sim.start();

		// start client
		client.start();
		
		
		//Thread.currentThread().sleep( 4000 );
		//configManager.makeAveragesDirectory();
		
		//sim.resume();
		
	}
}

class SimRunnable implements Runnable {
	
	/** configuration manager. */
	protected ConfigurationManager _manager;
	/** simulator. */
	protected ComputerNetworkSimulator _sim;
	
	/**
	 * Default constructor.
	 * @param sim to run.
	 * @param manager configured to create averages.
	 */
	public SimRunnable( ConfigurationManager manager, ComputerNetworkSimulator sim ) {
		_manager = manager;
		_sim = sim;
	}
	
	/**
	 * 
	 */
	public void run() {
		try {
			_sim.run();
			_manager.makeAveragesDirectory();
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
}