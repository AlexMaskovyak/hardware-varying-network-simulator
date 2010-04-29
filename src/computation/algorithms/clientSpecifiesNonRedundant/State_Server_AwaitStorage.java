package computation.algorithms.clientSpecifiesNonRedundant;

import messages.StorageDeviceDataStoreMessage;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;

/**
 * State where the server awaits storage requests from the Client.
 * @author Alex Maskovyak
 *
 */
public class State_Server_AwaitStorage 
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
			
				case SERVER_DATA_STORE: 
					AbstractAlgorithm algorithm = getStateHolder();
					algorithm.sendEvent( 
						algorithm.getComputer().getHarddrive(), 
						new StorageDeviceDataStoreMessage( 
							(Integer)aMessage.getValue(AlgorithmMessage.INDEX), 
							(IData)aMessage.getValue(AlgorithmMessage.DATA) ) );
					break;
				case SERVER_DATA_STORE_COMPLETE:
					updateStateHolder( new State_Server_Service() );
					break;
				default: break;
			}
		}
	}
}
