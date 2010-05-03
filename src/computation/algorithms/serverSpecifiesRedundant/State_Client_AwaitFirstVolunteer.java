package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_AwaitFirstVolunteer 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields
	
	/** base number of slices/servers to use for one copy of the data. */
	protected int _serverAmount;
	/** multiplier to use against the base server amount */
	protected int _redundancy;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param serverAmount to obtain to store one copy of the data.
	 * @param redundancy level of the data to store (in this algorithm, it is
	 * used as a multiplier against the server amount)
	 */
	public State_Client_AwaitFirstVolunteer( int serverAmount, int redundancy ) {
		_serverAmount = serverAmount;
		_redundancy = redundancy;
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
				// got our first volunteer
				case SERVER_VOLUNTEERS: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "CLIENT_AWAIT_FIRST_VOLUNTEER", 0, 0, 1, 1, 0, 0) );
					
					// this will become the primary server
					IAddress serverAddress = (IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS );
					AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_ACCEPTS_VOLUNTEER_AS_PRIMARY );
					response.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					response.setValue( AlgorithmMessage.SERVERS_REQUESTED, _serverAmount );
					response.setValue( AlgorithmMessage.REDUNDANCY_REQUESTED, _redundancy );
					response.setValue( AlgorithmMessage.START_INDEX, 0);
					response.setValue( AlgorithmMessage.END_INDEX, getStateHolder().getDataAmount() - 1 );
					
					// await more volunteers to pass along to our primary
					updateStateHolder( new State_Client_AwaitVolunteers( ( _serverAmount * _redundancy ), serverAddress ) ); 
					
					// let them know the good news
					sendMessageDownStack( 
						response, 
						serverAddress );
					
					break;
				// we don't do anything explicitly for this case
				case SERVER_REJECTS_VOLUNTEER_REQUEST: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "CLIENT_AWAIT_FIRST_VOLUNTEER", 0, 0, 0, 1, 0, 0) );
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
		return String.format( "State_Client_AwaitFirstVolunteer" );
	}
}