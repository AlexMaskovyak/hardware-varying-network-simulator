package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;

import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * The default role before receiving indication about client or server status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_AwaitVolunteers
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields	

	/** server total we seek. */
	protected int _volunteersSought;
	/** number of volunteers located. */
	protected int _volunteersFound;
	/** primary server's address. */
	protected IAddress _primaryServerAddress;
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param servers we need to obtain.
	 * @param primaryServerAddress to which to send volunteer addresses.
	 */
	public State_Client_AwaitVolunteers( int servers, IAddress primaryServerAddress ) {
		_volunteersSought = servers;
		_primaryServerAddress = primaryServerAddress;
	}
	
	/** externalize instantiation. */
	protected void init() {
		_volunteersFound = 1;
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
			
				case SERVER_VOLUNTEERS:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "CLIENT_AWAIT_VOLUNTEERS", 0, 0, 1, 1, 0, 0) );
					
					// tally it
					IAddress volunteerAddress = (IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS );
					_volunteersFound++;
					
					// relay to primary
					AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_VOLUNTEERS );
					response.setValue( AlgorithmMessage.VOLUNTEER_ADDRESS, volunteerAddress );
					
					// inform the primary server of a new volunteer
					sendMessageDownStack( 
						response, 
						_primaryServerAddress );
					
					// are we done looking?
					if( _volunteersFound == _volunteersSought ) {
						// we now wait for the server to finish setting itself
						// and its volunteers up.
						updateStateHolder( new State_Client_AwaitServerReady( _primaryServerAddress ) );						
					}

					break;
				// nothing else is worth our time
				default: break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Client_AwaitVolunteers" );
	}
}
