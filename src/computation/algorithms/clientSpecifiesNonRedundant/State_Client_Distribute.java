package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.List;

import network.routing.IAddress;

import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.AbstractAlgorithm;
import computation.state.IState;

/**
 * The default role before receiving indication about client or server status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_Distribute
		extends AbstractState 
		implements IState<AbstractAlgorithm> {

/// Fields	

	/** servers we have found. */
	protected List<IAddress> _servers;


/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_Distribute( List<IAddress> servers ) {
		_servers = servers;
	}
	
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
			
				// nope, you missed your chance, maybe some other time
				case VOLUNTEER_ACCEPTED:
					sendMessageDownStack( 
						new AlgorithmMessage( 
							AlgorithmMessage.TYPE.VOLUNTEER_REJECTED), 
							(IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS ) );
					break;
				// this is our call to distribute information
				case DO_WORK:
					
					
					//getStateHolder().g
					
					sendEvent( getStateHolder(), new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK ) );
					break;
			}
		}
	}

	
}
