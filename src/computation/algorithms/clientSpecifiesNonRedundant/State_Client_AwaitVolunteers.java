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
public class State_Client_AwaitVolunteers
		extends AbstractState 
		implements IState<AbstractAlgorithm> {

/// Fields	

	/** servers we have found. */
	protected List<IAddress> _servers;
	/** server total we seek. */
	protected int _volunteersSought;

/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_AwaitVolunteers( int servers ) {
		_volunteersSought = servers;
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
			
				case VOLUNTEER_ACCEPTED:
					// add it
					_servers.add( (IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS ) );
					
					// are we done looking?
					if( _servers.size() == _volunteersSought ) {
						updateStateHolder( new State_Client_Distribute( _servers ) );
						sendEvent( getStateHolder(), new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK ) );
					}
					break;
				// nothing else if worth our time
				default: break;
			}
		}
	}

	
}
