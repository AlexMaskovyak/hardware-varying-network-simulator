package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Volunteered 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields
	
	/** amount of data to be stored. */
	protected int _dataAmount;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param dataAmount
	 */
	public State_Server_Volunteered( int dataAmount ) {
		_dataAmount = dataAmount;
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
				// we've been assigned as a primary
				case CLIENT_ACCEPTS_VOLUNTEER_AS_PRIMARY: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_VOLUNTEER", 0, 0, 0, 1, 0, 0) );
					
					// get requested values for storage
					IAddress clientAddress = (IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS );
					int servers = (Integer)aMessage.getValue( AlgorithmMessage.SERVERS_REQUESTED );
					int redundancy = (Integer)aMessage.getValue( AlgorithmMessage.REDUNDANCY_REQUESTED );
					int endIndex = (Integer)aMessage.getValue( AlgorithmMessage.END_INDEX );
					
					updateStateHolder( new State_Server_Primary_AwaitVolunteers( clientAddress, getStateHolder().getComputer().getAddress(), servers, redundancy, 0, endIndex ) ); 
					
					// allows us to check whether we need to gather more volunteers
					sendEvent(
						getStateHolder(),
						new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK ),
						
						DEvent.INTERNAL );
					
					break;
				// we are a secondary storage unit for a primary storage
				case SERVER_ACCEPTS_VOLUNTEER_AS_SECONDARY:
					// go to the secondary storage state		
					// server needs to tell secondary how much is to be stored so
					// that the secondary can confirm receipt of all info
					updateStateHolder( 
						new State_Server_Secondary_AwaitStorage( 
							(IAddress)aMessage.getValue( AlgorithmMessage.SERVER_ADDRESS ),
							_dataAmount ) );
					break;
				// we've been rejected and don't have a role aside from existing
				case CLIENT_REJECTS_VOLUNTEER: 
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_VOLUNTEER", 0, 0, 0, 1, 0, 0) );
					
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
		return String.format( "State_Server_Volunteered" );
	}
}
