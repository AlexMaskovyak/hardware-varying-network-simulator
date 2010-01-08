import reporting.NodeReporter;
import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.Simulator;
import network.Connection;
import network.Data;
import network.IConnection;
import network.INode;
import network.Node;
import network.NodeSimulatableListener;

/**
 * Creates a simulator from a configuration file and runs a simulation.
 * 
 * @author Alex Maskovyak
 *
 */
public class Driver {

	/**
	 * Read in the configuration file.
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		//if( args.length != 0 ) {
		//	System.err.println( "java -jar program.jar <path to config file>" );
		//}
		
		System.out.println("done");
		ISimulator sim = new Simulator();
		INode n = new Node();
		INode n2 = new Node();
		IConnection c = new Connection();
		n.registerConnection(c);
		n2.registerConnection(c);
		/*((ISimulatable)n).addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%d! %s\n", e.getEventTime(), ((INode)e.getSimulatable()).getId());
				
			} } );*/
		((ISimulatable)n).addListener(new NodeSimulatableListener(System.out));
		((ISimulatable)n).addListener(new ISimulatableListener() {			
			
			protected int _timesToRun = 5;
			protected int _timesRun = 0;
			
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				((INode)e.getSimulatable()).send(new Data(1, null));
				_timesRun++;
				if( _timesToRun == _timesRun ) {
					e.getSimulatable().removeListener(this);
				}
			}
		} );
		((ISimulatable)n2).addListener(new NodeSimulatableListener(System.out));
		/*((ISimulatable)n2).addListener(new ISimulatableListener() { 
			@Override
			public void tickHandledUpdate(ISimulatableEvent e) {
			}

			@Override
			public void tickReceivedUpdate(ISimulatableEvent e) {
				System.out.printf("Got tick #%d! %s\n", e.getEventTime(), ((INode)e.getSimulatable()).getId());
			} } );*/
		//((ISimulatable)n).addListener(new NodeReporter(n));
		sim.registerSimulatable((ISimulatable)n);
		sim.registerSimulatable((ISimulatable)n2);

		Thread t = new Thread((Runnable)sim);
		t.start();
		t.join();	
		sim.unregisterSimulatable((ISimulatable)n);
		sim.start();
		sim.simulate(5);
	}
}
