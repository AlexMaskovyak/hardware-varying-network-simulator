package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.ArrayList;
import java.util.List;

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
public class State_Client_AwaitVolunteers
		extends AbstractState
		implements IState<AbstractAlgorithm> {

/// Fields	

	/** servers we have found. */
	protected List<IAddress> _servers;
	/** server total we seek. */
	protected int _volunteersSought;
	/** volunteers acknowledged.*/
	protected int _volunteers;
	
	
/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_AwaitVolunteers( int servers ) {
		_volunteersSought = servers;
		init();
	}
	
	/** externalize instantiation. */
	protected void init() {
		_servers = new ArrayList<IAddress>();
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
					if( _servers.size() < _volunteersSought ) {
						// add it
						IAddress volunteerAddress = (IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS );
						_servers.add( volunteerAddress );
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "CLIENT_AWAIT_VOLUNTEERS", 0, 0, 0, 1, 0, 0) );
						
						// inform them
						AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_ACCEPTS_VOLUNTEER );
						response.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
						sendMessageDownStack( response, volunteerAddress );
					}

					break;
				case SERVER_ACKNOWLEDGES:
					_volunteers++;
					
					// are we done looking?
					if( _volunteers == _volunteersSought ) {
						updateStateHolder( new State_Client_Distribute( _servers ) );
						
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.START_INDEX, 0 );
						doWork.setValue( AlgorithmMessage.END_INDEX, getStateHolder().getDataAmount() - 1 );
						sendEvent( getStateHolder(), doWork );
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
