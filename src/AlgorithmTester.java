import network.communication.Address;

import computation.HardwareComputerNode;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.ClientSpecifiesNonRedundantAlgorithm2;
import computation.state.IState;


public class AlgorithmTester {

	/**
	 * Driver.
	 * @param args
	 */
	public static void main(String... args) {
		HardwareComputerNode node = new HardwareComputerNode( new Address(0));
		ClientSpecifiesNonRedundantAlgorithm2 alg = new ClientSpecifiesNonRedundantAlgorithm2( node );
		IState<AbstractAlgorithm> currentState = alg.getIState();
		if( alg == currentState.getStateHolder() ) {
			System.out.println("okay");
		}
	}
	
}
