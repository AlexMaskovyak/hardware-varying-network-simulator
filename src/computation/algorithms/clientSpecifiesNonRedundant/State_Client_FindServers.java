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
public class State_Client_FindServers
		extends AbstractState 
		implements IState<AbstractAlgorithm> {

/// Fields	

	protected List<IAddress> _servers;
	protected int _volunteersSought;

/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_FindServers( int servers ) {
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
				// if we need more, add it
				if( _servers.size() < _volunteersSought ) {
					_servers.add( (IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS ) );
					
				}
			}
		}
	}

	
}
