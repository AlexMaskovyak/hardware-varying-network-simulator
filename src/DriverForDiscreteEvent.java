import reporting.NodeReporter;
import simulation.DiscreteScheduledEventSimulator;
import simulation.IDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEventSimulator;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.Simulator;
import network.Address;
import network.ConnectionAdaptor;
import network.ConnectionMedium;
import network.Data;
import network.IConnectionAdaptor;
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
		ISimulatable n = new Node(new Address(0));
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
		sim.stop();
		//sim.start();
		//((DiscreteScheduledEventSimulator)sim).run();
		//sim.unregisterSimulatable((ISimulatable)n);
		//sim.start();
		//sim.simulate(5);
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
	public void getMessage() {
		_node.receive(createPacket());
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
	public void getMessage() {
		_node.receive(createPacket());
		_time = _sim.getTime();
		_time++;
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
}