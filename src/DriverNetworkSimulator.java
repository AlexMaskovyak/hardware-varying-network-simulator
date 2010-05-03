
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.management.OperationsException;

import org.apache.log4j.PropertyConfigurator;

import computation.Data;
import computation.HardwareComputerNode;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.IAlgorithm;
import computation.algorithms.ClientSpecifiesNonRedundantAlgorithm;
import computation.algorithms.dummy.DummyAlgorithm;
import computation.hardware.Cache;
import computation.hardware.Harddrive;
import configuration.ConfigurationManager;

import simulation.simulator.ComputerNetworkSimulator;
import simulation.simulator.listeners.ReportingSimulatorListener;
import network.communication.Address;
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
	 * Read in the configuration file.J
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		PropertyConfigurator.configure("log4j.properties"); // disable it
		if( args.length != 2 ) {
			System.err.println( "java -jar hardwareSimulation.jar <path to config file> <config file name>" );
		}
		
		ConfigurationManager configManager = new ConfigurationManager( args[ 0 ], args[ 0 ] + File.separator + args[ 1 ], "run_" );
		File outputPath = configManager.makeNewRunDirectory();
		
		
		
		System.out.println("starting");
		
		
		ComputerNetworkSimulator sim = configManager.configureSimulator();
		sim.addListener(new ReportingSimulatorListener(new File("C:\\Users\\user\\workspaces\\gradproject\\hardware-varying-network-simulator-5\\output\\sim.txt")));
		sim.setOutputPath( outputPath.getAbsolutePath() );
		sim.addAlgorithmListeners();
		
		
		
		Thread t = new Thread((Runnable)sim);
		t.start();
		sim.start();
		HardwareComputerNode c = (HardwareComputerNode)sim.getClient();
		c.start();
		
		//configManager.
	}
	
	/**
	 * Generates the quantity of data specified.
	 * @param amount
	 * @return
	 */
	public static IData[] generateData( int amount ) {
		List<IData> data = new ArrayList<IData>( amount );
		for( int i = 0; i < amount; ++i ) {
			data.add( new Data( i, new byte[] { Byte.parseByte( String.format( "%d", i % 128 ) )  } ) );
		}
		IData[] dataArray = new IData[ data.size() ];
		dataArray = data.toArray( dataArray );
		return dataArray;
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