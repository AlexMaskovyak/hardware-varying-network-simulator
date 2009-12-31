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
	public static void main(String... args) {
		//if( args.length != 0 ) {
		//	System.err.println( "java -jar program.jar <path to config file>" );
		//}
		
		ISimulator sim = new Simulator(1000);
		INode n = new Node();
		INode n2 = new Node();
		IConnection c = new Connection();
		n.connect(c);
		n2.connect(c);
		sim.addListener((ISimulatable)n);
		sim.addListener((ISimulatable)n2);
	}
}
