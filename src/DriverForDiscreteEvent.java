import reporting.NodeReporter;
import simulation.DiscreteScheduledEventSimulator;
import simulation.IDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEventSimulator;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.Simulator;
import network.Connection;
import network.Data;
import network.IConnection;
import network.IData;
import network.INode;
import network.Node;

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
		ISimulatable n = new Node();
		ISimulatable n2 = new Node();
		IConnection c = new Connection();
		((INode)n).registerConnection(c);
		((INode)n2).registerConnection(c);
		n.addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%d! %s\n", e.getEventTime(), ((INode)e.getSimulatable()).getId());				
			} } );
		n2.addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%d! %s\n", e.getEventTime(), ((INode)e.getSimulatable()).getId());
			} } );
		n.addListener(new NodeReporter((INode)n));
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
		sim.start();
		//sim.stop();
		//sim.start();
		//((DiscreteScheduledEventSimulator)sim).run();
		//sim.unregisterSimulatable((ISimulatable)n);
		//sim.start();
		//sim.simulate(5);
	}
}

class DiscreteEventTest implements IDiscreteScheduledEvent {

	protected INode _node;
	protected int _time;
	protected IData _data;
	
	public DiscreteEventTest(INode node, int time, IData data) {
		_node = node;
		_time = time;
		_data = data;
	}
	
	@Override
	public void execute() {
		_node.receive(_data);
	}

	@Override
	public int getTime() {
		return _time;
	}
}

class DiscreteEventTest2 implements IDiscreteScheduledEvent {

	protected INode _node;
	protected int _time;
	protected IData _data;
	protected IDiscreteScheduledEventSimulator _sim;
	
	public DiscreteEventTest2(INode node, IDiscreteScheduledEventSimulator sim, int time, IData data) {
		_node = node;
		_time = time;
		_data = data;
		_sim = sim;
	}
	
	@Override
	public void execute() {
		_node.receive(_data);
		_time = _sim.getTime();
		_time++;
		_sim.schedule(this);
	}

	@Override
	public int getTime() {
		return _time;
	}
}