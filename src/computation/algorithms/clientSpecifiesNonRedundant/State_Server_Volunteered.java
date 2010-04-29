package computation.algorithms.clientSpecifiesNonRedundant;

import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Volunteered 
		extends AbstractState
		implements IState<AbstractAlgorithm> {

/// IState

	/*
	 * (non-Javadoc)
	 * @see computation.algorithms.clientSpecifiesNonRedundant.AbstractState#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	public void handleEventDelegate(IDEvent event) {
		IMessage message = event.getMessage();
		if( message instanceof AlgorithmMessage ) {
			AlgorithmMessage aMessage = (AlgorithmMessage)message;
			switch( aMessage.getType() ) {
			
				case VOLUNTEER_ACCEPTED: 
					updateStateHolder( new State_Server_AwaitStorage() ); 
					break;
				case VOLUNTEER_REJECTED: 
					updateStateHolder( new State_NullRole() );
					break;
				default: break;
			}
		}
	}

}
