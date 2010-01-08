import reporting.NodeReporter;
import simulation.DiscreteScheduledEventSimulator;
import simulation.IDiscreteScheduledEvent;
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
		ISimulator sim = new DiscreteScheduledEventSimulator();
		INode n = new Node();
		INode n2 = new Node();
		IConnection c = new Connection();
		n.registerConnection(c);
		n2.registerConnection(c);
		((ISimulatable)n).addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%d! %s\n", e.getEventTime(), ((INode)e.getSimulatable()).getId());				
			} } );
		((ISimulatable)n2).addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%d! %s\n", e.getEventTime(), ((INode)e.getSimulatable()).getId());
			} } );
		((ISimulatable)n).addListener(new NodeReporter(n));
		sim.registerSimulatable((ISimulatable)n);
		sim.registerSimulatable((ISimulatable)n2);

		Thread t = new Thread((Runnable)sim);
		((DiscreteScheduledEventSimulator)sim).schedule(new DiscreteEventTest(n, 1, new Data(1, null)));
		((DiscreteScheduledEventSimulator)sim).schedule(new DiscreteEventTest(n, 2, new Data(33, null)));
		t.start();
		//t.join();	
		System.out.println("what");
		System.out.flush();
		((DiscreteScheduledEventSimulator)sim).schedule(new DiscreteEventTest(n, 3, new Data(66, null)));
		sim.stop();
		sim.start();
		((DiscreteScheduledEventSimulator)sim).run();
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
