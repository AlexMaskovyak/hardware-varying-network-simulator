import java.util.Collection;
import java.util.List;
import java.util.Set;

import computation.ComputerNetworkSimulator;

import reporting.NodeReporter;
import routing.IAddress;
import simulation.DefaultDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.Simulator;
import messages.NodeOutMessage;
import network.Address;
import network.ConnectionMedium;
import network.Data;
import network.IConnectionMedium;
import network.IData;
import network.INode;
import network.IPacket;
import network.NetworkSimulator;
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
		List<ISimulatable> simulatables = sim.createRingOfConnectedNodes(5);
		INode n0 = (INode) simulatables.get(1);
	//	System.out.println(simulatables.size());
		/*INode n0 = sim.createNode(); //  new Node(new Address(1));
		INode n1 = sim.createNode();
		IConnectionMedium medium = sim.createConnectionMedium();
		sim.connect(n0, medium);
		sim.connect(n1, medium);
		
		INode n2 = sim.createNode();
		IConnectionMedium medium2 = sim.createConnectionMedium();
		sim.connect(n1, medium2);
		sim.connect(n2, medium2);
		
		INode n3 = sim.createNode();
		IConnectionMedium medium3 = sim.createConnectionMedium();
		sim.connect(n2, medium3);
		sim.connect(n3, medium3);
		
		INode n4 = sim.createNode();
		IConnectionMedium medium4 = sim.createConnectionMedium();
		sim.connect(n3, medium4);
		sim.connect(n4, medium4);
		
		//sim.disconnect( medium4 );
		//sim.disconnect( n4, n3 );
		
		((ISimulatable)n0).addListener(new NodeSimulatableListener(System.out));
		((ISimulatable)n1).addListener(new NodeSimulatableListener(System.out));*/
		
		Thread t = new Thread((Runnable)sim);
		t.start();
		//t.join();
		sim.schedule(new DefaultDiscreteScheduledEvent<NodeOutMessage>(null, (ISimulatable)n0, 3, sim, new NodeOutMessage( new Data(1, new byte[] {0, 0, 0} ), new Address(4) ) ) );
		//sim.schedule(new DefaultDiscreteScheduledEvent<NodeMessage>(null, (ISimulatable)n4, 1, sim, new NodeMessage( new Data(1, new byte[] {0, 0, 0} ), new Address(0) ) ) );
		//sim.schedule(new DefaultDiscreteScheduledEvent<NodeMessage>(null, (ISimulatable)n0, -100, sim, new NodeMessage( new Data(1, new byte[] {0, 0, 0} ), new Address(4) ) ) );
		//sim.start();
		//sim.simulate(5);
		//n0.send(new Data(15, new byte[] {0} ), n4.getAddress());
		//n0.send(new Data(15, new byte[] {0} ), new Address(4));
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