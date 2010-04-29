package computation.algorithms.clientSpecifiesNonRedundant;

import simulation.event.IDEvent;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;
import computation.state.IStateHolder;

/**
 * Performs all data storage/retrieval services in response to a client.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Service 
		extends AbstractState
		implements IState<AbstractAlgorithm> {
	
	@Override
	public void handleEventDelegate(IDEvent event) {
		// TODO Auto-generated method stub

	}

	
}
