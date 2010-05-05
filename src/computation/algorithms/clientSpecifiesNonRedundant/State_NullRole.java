package computation.algorithms.clientSpecifiesNonRedundant;

import network.communication.Address;
import network.routing.IAddress;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * The default role before receiving indication about client or server status.
 * @author Alex Maskovyak
 *
 */
public class State_NullRole
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
				case SET_CLIENT: 
					// send volunteer request
					AlgorithmMessage volunteerRequest = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_VOLUNTEERS );
					volunteerRequest.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					volunteerRequest.setValue( AlgorithmMessage.AMOUNT, getStateHolder().getDataAmount() / getStateHolder().getServerCount() );
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "NULL_ROLE", 0, 0, 1, 0, 0, 0) );
					sendMessageDownStack( volunteerRequest, Address.BROADCAST );
					
					// record servers needed and set next state
					int servers =  getStateHolder().getServerCount();
					updateStateHolder( new State_Client_AwaitVolunteers( servers ) ); 
					sendEvent( getStateHolder(), new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK ) );
					
					break;
				case CLIENT_REQUESTS_VOLUNTEERS: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "NULL_ROLE", 0, 0, 1, 1, 0, 0) );
					
					// check to make sure we have enough space
					if( (Integer)aMessage.getValue( AlgorithmMessage.AMOUNT ) 
							>= getStateHolder().getComputer().getHarddrive().freeSpace() ) {
						return;
					}
					
					// send response to client
					IAddress clientAddress = (IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS );
					AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_VOLUNTEERS );
					response.setValue( AlgorithmMessage.VOLUNTEER_ADDRESS, getStateHolder().getComputer().getAddress() );
					sendMessageDownStack( response, clientAddress );
					
					// re-broadcast volunteer request to others
					sendMessageDownStack( aMessage, Address.BROADCAST );
					
					// go to volunteered state
					updateStateHolder( new State_Server_Volunteered() );
					break;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_NullRole" );
	}
}
