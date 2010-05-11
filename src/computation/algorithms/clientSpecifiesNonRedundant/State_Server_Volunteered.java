package computation.algorithms.clientSpecifiesNonRedundant;

import javax.swing.GroupLayout.Alignment;

import network.routing.IAddress;

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
				case CLIENT_ACCEPTS_VOLUNTEER: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_VOLUNTEERED", 0, 0, 0, 1, 0, 0) );
					AlgorithmMessage ack = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_ACKNOWLEDGES);
					ack.setValue( AlgorithmMessage.SERVER_ADDRESS, getStateHolder().getComputer().getAddress() );
					sendMessageDownStack(
							ack,
							(IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS ) );
					updateStateHolder( new State_Server_AwaitStorage() ); 
					
					break;
				case CLIENT_REJECTS_VOLUNTEER: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_VOLUNTEERED", 0, 0, 0, 1, 0, 0) );
					
					updateStateHolder( new State_NullRole() );
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
		return String.format( "State_Client_Volunteered" );
	}
}
