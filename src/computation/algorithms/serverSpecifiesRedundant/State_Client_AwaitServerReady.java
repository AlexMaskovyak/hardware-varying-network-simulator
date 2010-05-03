package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_AwaitServerReady 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields
	
	/** server's address. */
	protected IAddress _serverAddress;
	

/// Construction
	
	/**
	 * Default constructor.
	 * @param serverAddress address of the server.
	 */
	public State_Client_AwaitServerReady( IAddress serverAddress ) {
		_serverAddress = serverAddress;
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
				case SERVER_VOLUNTEERS:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "CLIENT_AWAIT_SERVER_READY", 0, 0, 1, 1, 0, 0) );
					
					// reject volunteers since we already have enough
					sendMessageDownStack( 
						new AlgorithmMessage( 
							AlgorithmMessage.TYPE.CLIENT_REJECTS_VOLUNTEER), 
							(IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS ) );
					break;
				
				// the primary server is ready, begin the distribution stage
				case SERVER_INDICATES_STORE_READY: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "CLIENT_AWAIT_SERVER_READY", 0, 0, 0, 1, 0, 0) );
					System.out.println( "client got store ready from server." );
					// create next state
					updateStateHolder( 
						new State_Client_Distribute( 
							_serverAddress, 
							0, 
							getStateHolder().getDataAmount() - 1 ) ); 
					
					// start the work distribute cycle
					sendEvent( 
						getStateHolder(), 
						new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_DO_DISTRIBUTE ),
						DEvent.INTERNAL );
					break;
				default: break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Client_AwaitServerReady" );
	}
}
