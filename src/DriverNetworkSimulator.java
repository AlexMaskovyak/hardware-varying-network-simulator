import java.util.Collection;
import java.util.List;
import java.util.Set;

import computation.HardwareComputerNode;
import computation.IComputer;
import computation.IHardwareComputer;
import computation.RandomDistributionAlgorithm;

import reporting.NodeReporter;
import routing.IAddress;
import simulation.ComputerNetworkSimulator;
import simulation.DefaultDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.NetworkSimulator;
import simulation.Simulator;
import messages.AlgorithmResponseMessage;
import messages.NodeOutMessage;
import network.Address;
import network.ConnectionMedium;
import network.Data;
import network.IConnectionMedium;
import network.IData;
import network.INode;
import network.IPacket;
import network.Node;
import network.NodeSimulatableListener;
import network.Packet;

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
		//if( args.length != 0 ) {
		//	System.err.println( "java -jar program.jar <path to config file>" );
		//}
		
		System.out.println("starting");
		
		
		NetworkSimulator sim = new ComputerNetworkSimulator();
		//IHardwareComputer n0 = (IHardwareComputer)sim.createNode();
		//System.out.println("\n");
		//n0.install( new RandomDistributionAlgorithm( n0 ) );
		List<ISimulatable> simulatables = sim.createSeriesOfConnectedNodes(8);
		INode n0 = (INode) simulatables.get(0);
		
		//System.out.println(simulatables.size());

		
		Thread t = new Thread((Runnable)sim);
		t.start();
		//t.join();
		sim.start();
		HardwareComputerNode c = (HardwareComputerNode)n0;
		RandomDistributionAlgorithm alg = (RandomDistributionAlgorithm)c.getInstalledAlgorithm();
		alg.setAddressRange( new Address(1), new Address(5));
		c.start();
		sim.pause();
		
		Thread.currentThread().sleep( 4000);
		sim.resume();
	}
}

class DiscretePacketEventTest 
		extends DefaultDiscreteScheduledEvent
		implements IDiscreteScheduledEvent {

/// Fields
	
	/** data to send. */
	protected IData _data;
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param eventTime
	 * @param simulator
	 * @param data
	 */
	public DiscretePacketEventTest(
			ISimulatable source,
			ISimulatable destination, 
			double eventTime, 
			ISimulator simulator,
			IData data) {
		super(destination, destination, eventTime, simulator, new IMessage() {});
		_data = data;
	}

/// IDiscreteSimulatableEvent
	
	/*
	 * (non-Javadoc)
	 * @see simulation.AbstractDiscreteScheduledEvent#getMessage()
	 */
	@Override
	public IMessage getMessage() {
		return new IMessage() {};//_data;
	}
}