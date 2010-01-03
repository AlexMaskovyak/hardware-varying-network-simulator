import simulation.ISimulatable;
import simulation.ISimulatableEvent;
import simulation.ISimulatableListener;
import simulation.ISimulator;
import simulation.Simulator;
import network.IConnection;
import network.INode;
import network.Node;

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
		n.connect(c);
		n2.connect(c);
		((ISimulatable)n).addListener(new ISimulatableListener() { 
			@Override
			public void tickUpdate(ISimulatableEvent e) {
				System.out.printf("Got a tick! %s\n", ((INode)e.getSimulatable()).getId());
			} } );
		((ISimulatable)n2).addListener(new ISimulatableListener() { 
			@Override
			public void tickUpdate(ISimulatableEvent e) {
				System.out.printf("Got a tick! %s\n", ((INode)e.getSimulatable()).getId());
			} } );
		
		sim.registerSimulatable((ISimulatable)n);
		sim.registerSimulatable((ISimulatable)n2);

		Thread t = new Thread((Runnable)sim);
		t.start();
		t.join();	
	}
}
