package computation.algorithms.clientSpecifiesNonRedundant;

import network.communication.Address;
import network.communication.Packet;
import network.routing.IAddress;
import messages.ProtocolHandlerMessage;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import simulation.simulatable.ISimulatable;
import computation.algorithms.AbstractAlgorithm;
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
					AlgorithmMessage volunteerRequest = new AlgorithmMessage( AlgorithmMessage.TYPE.VOLUNTEER_REQUEST );
					volunteerRequest.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					sendMessageDownStack( volunteerRequest, Address.BROADCAST );
					sendEvent( getStateHolder(), new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK ) );
					
					// record servers needed and set next state
					int servers = (Integer)aMessage.getValue( AlgorithmMessage.SERVERS );
					updateStateHolder( new State_Client_AwaitVolunteers( servers ) ); 
					break;
				case VOLUNTEER_REQUEST: 
					// send response to client
					IAddress clientAddress = (IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS );
					AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.VOLUNTEER_ACCEPTED );
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
}
