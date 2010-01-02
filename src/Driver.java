import simulation.ISimulatable;
import simulation.ISimulator;
import network.IConnection;
import network.INode;

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
		ISimulator sim = new Simulator(10);
		INode n = new Node();
		INode n2 = new Node();
		IConnection c = new Connection();
		n.connect(c);
		n2.connect(c);
		sim.registerSimulatable((ISimulatable)n);
		sim.unregisterSimulatable((ISimulatable)n2);
		n.addListener(sim);
		n2.addListener(sim);
		Thread t = new Thread((Runnable)sim);
		t.start();
		t.join();
		
		//sim.start();
		//System.out.println("done");
		
	}
}
