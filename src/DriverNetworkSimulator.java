
import java.util.Collection;
import java.util.List;
import java.util.Set;

import computation.Data;
import computation.HardwareComputerNode;
import computation.IComputer;
import computation.IData;
import computation.IHardwareComputer;
import computation.algorithms.RandomDistributionAlgorithm;


import reporting.NodeReporter;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulator.ComputerNetworkSimulator;
import simulation.simulator.ISimulator;
import simulation.simulator.NetworkSimulator;
import simulation.simulator.Simulator;
import messages.AlgorithmResponseMessage;
import messages.NodeOutMessage;
import network.communication.Address;
import network.communication.IPacket;
import network.communication.Packet;
import network.entities.ConnectionMedium;
import network.entities.IConnectionMedium;
import network.entities.INode;
import network.entities.Node;
import network.listeners.NodeSimulatableListener;
import network.routing.IAddress;

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
		List<ISimulatable> simulatables = sim.createRandomlyConnectedNodes( 200, 26 ); //sim.createSeriesOfConnectedNodes(8);
		INode n0 = (INode) simulatables.get(0);
		System.out.println(n0.getAddress());
		
		//System.out.println(simulatables.size());

		
		Thread t = new Thread((Runnable)sim);
		t.start();
		//t.join();
		sim.start();
		HardwareComputerNode c = (HardwareComputerNode)n0;
		RandomDistributionAlgorithm alg = (RandomDistributionAlgorithm)c.getInstalledAlgorithm();
		alg.setAddressRange( new Address(1), new Address(5));
		//n0.send(new IMessage() {}, new Address(7));
		c.start();
		//sim.pause();
		
		//Thread.currentThread().sleep( 4000);
		//sim.resume();
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