import computation.Data;
import computation.HardwareComputerNode;
import computation.IData;
import computation.algorithms.IAlgorithm;
import computation.algorithms.RandomDistributionAlgorithm;

import reporting.NodeReporter;
import simulation.event.DefaultDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent;
import simulation.event.IDiscreteScheduledEvent.IMessage;
import simulation.simulatable.ISimulatable;
import simulation.simulatable.PerformanceRestrictedSimulatable;
import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;
import simulation.simulator.DiscreteScheduledEventSimulator;
import simulation.simulator.IDiscreteScheduledEventSimulator;
import simulation.simulator.ISimulator;
import simulation.simulator.Simulator;
import messages.AlgorithmDoWorkMessage;
import network.communication.Address;
import network.communication.IPacket;
import network.communication.Packet;
import network.entities.ConnectionAdaptor;
import network.entities.ConnectionMedium;
import network.entities.IConnectionAdaptor;
import network.entities.IConnectionMedium;
import network.entities.INode;
import network.entities.Node;
import network.listeners.NodeSimulatableListener;

/**
 * Creates a simulator from a configuration file and runs a simulation.
 * 
 * @author Alex Maskovyak
 *
 */
public class DriverForDiscreteEvent {

	/**
	 * Read in the configuration file.
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		//if( args.length != 0 ) {
		//	System.err.println( "java -jar program.jar <path to config file>" );
		//}
		
		System.out.println("done");
		IDiscreteScheduledEventSimulator sim = new DiscreteScheduledEventSimulator();
		PerformanceRestrictedSimulatable prs = new RandomDistributionAlgorithm();
		sim.registerSimulatable( prs );
		sim.schedule(
			new DefaultDiscreteScheduledEvent<IMessage>(
				null, 
				prs, 
				1, 
				sim, 
				new AlgorithmDoWorkMessage()));
		
		Thread t = new Thread( (Runnable)sim );
		t.start();
		
		//HardwareComputerNode hcn = new HardwareComputerNode(new Address(0));
		System.out.println();
		//hcn.install( (IAlgorithm)prs );
		/*ISimulatable n = new Node(new Address(0));
		ISimulatable n2 = new Node(new Address(1));
		IConnectionMedium c = new ConnectionMedium();
		IConnectionAdaptor a = new ConnectionAdaptor();
		a.setAddress(((INode)n).getAddress());
		a.connect(c);
		((INode)n).addAdaptor(a);
		IConnectionAdaptor a2 = new ConnectionAdaptor();
		a2.setAddress(((INode)n2).getAddress());
		a2.connect(c);
		((INode)n2).addAdaptor(a2);
		n.addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%f! %s\n", e.getEventTime(), ((INode)e.getSource()).getAddress());				
			} } );
		n2.addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%f! %s\n", e.getEventTime(), ((INode)e.getSource()).getAddress());
			} } );
		//n.addListener(new NodeReporter((INode)n));
		n.addListener(new NodeSimulatableListener(System.out));
		n2.addListener(new NodeSimulatableListener(System.out));
		sim.registerSimulatable((ISimulatable)n);
		sim.registerSimulatable((ISimulatable)n2);

		Thread t = new Thread((Runnable)sim);
		sim.schedule(new DiscreteEventTest2((INode)n, sim, 1, new Data(1, null)));
		sim.schedule(new DiscreteEventTest((INode)n, 2, new Data(33, null)));
		t.start();
		//t.join();	
		System.out.println("what");
		System.out.flush();
		sim.schedule(new DiscreteEventTest((INode)n, 3, new Data(66, null)));
		Thread.sleep(5000);
		sim.pause();
		//sim.start();
		sim.run();
		sim.stop();*/
		//sim.start();
		//((DiscreteScheduledEventSimulator)sim).run();
		//sim.unregisterSimulatable((ISimulatable)n);
		//sim.start();
		//sim.simulate(5);
		
		
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
	}
}

class DiscreteEventTest implements IDiscreteScheduledEvent {

	protected INode _node;
	protected double _time;
	protected IData _data;
	
	public DiscreteEventTest(INode node, double time, IData data) {
		_node = node;
		_time = time;
		_data = data;
	}
	
	@Override
	public IMessage getMessage() {
		return new IMessage() {};
	}

	@Override
	public double getEventTime() {
		return _time;
	}
	
	public IPacket<IData> createPacket() {
		return new Packet<IData>(_data, new Address(1), new Address(2), "generic", 1, (int)_time );
	}

	@Override
	public ISimulatable getDestination() {
		return (ISimulatable)_node;
	}

	@Override
	public ISimulatable getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISimulator getSimulator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
}

class DiscreteEventTest2 implements IDiscreteScheduledEvent {

	protected INode _node;
	protected double _time;
	protected IData _data;
	protected IDiscreteScheduledEventSimulator _sim;
	
	public DiscreteEventTest2(INode node, IDiscreteScheduledEventSimulator sim, double time, IData data) {
		_node = node;
		_time = time;
		_data = data;
		_sim = sim;
	}
	
	@Override
	public IMessage getMessage() {
		_node.receive(createPacket());
		_time = _sim.getTime();
		_time++;
		return new IMessage() {};
		//_sim.schedule(this);
	}

	@Override
	public double getEventTime() {
		return _time;
	}
	
	
	public IPacket<IData> createPacket() {
		return new Packet<IData>(_data, new Address(1), new Address(2), "generic", 1, (int)_time );
	}

	@Override
	public ISimulatable getDestination() {
		return (ISimulatable)_node;
	}

	@Override
	public ISimulatable getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISimulator getSimulator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}
}