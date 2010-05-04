package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.hardware.Harddrive;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_Read 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields
	
	/** primary server's address. */
	protected IAddress _primaryServerAddress;
	/** current index we are distributing.*/
	protected int _currentIndex;
	/** ending index to which to distribute data. */
	protected int _endIndex;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param primaryServerAddress to which to send volunteer addresses.
	 */
	public State_Client_Read( 
			IAddress primaryServerAddress, 
			int startIndex, 
			int endIndex ) {
		_primaryServerAddress = primaryServerAddress;
		_currentIndex = startIndex;
		_endIndex = endIndex;
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
			
				// the server has stored and is ready to do read requests
				case SERVER_INDICATES_READ_READY:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "REMOTE", 0, 0, 0, 1, 0, 0) );
					
					
					// kick start the read process
					sendEvent( 
						getStateHolder(), 
						new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_DO_READ ) );
					
					break;
				// request data
				case CLIENT_DO_READ:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "REMOTE", 0, 0, 1, 0, 0, 0) );
					
					// request current index
					AlgorithmMessage request = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_DATA );
					request.setValue( AlgorithmMessage.INDEX, _currentIndex++ );
					request.setValue( AlgorithmMessage.CLIENT_ADDRESS, getStateHolder().getComputer().getAddress() );
					
					sendMessageDownStack( 
						request, 
						_primaryServerAddress );
					
					// more work to do?  schedule it
					if( _currentIndex <= _endIndex ) {
						sendEvent( 
							getStateHolder(),
							new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_DO_READ ) );
					}
					
					break;
					
				// the server responds with information
				case SERVER_RESPONDS_WITH_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "REMOTE", 0, 1, 0, 0, 1, 0));
					
					IData data = (IData)aMessage.getValue( AlgorithmMessage.DATA );
					int index = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					
					// this needn't be done in the traditional way through an 
					// event since this is only here to demonstrate correctness.
					Harddrive harddrive = getStateHolder().getComputer().getHarddrive();
					IData baseLine = harddrive.getIndex( index );
					if( baseLine.equals( data) ) {
						harddrive.deleteIndex( index );
						//System.out.println( "We got good data! " + data + " left: " + harddrive.getSize() );
					} else {
						System.out.printf( "We received bad data! Expected: %s. Received %s.\n", baseLine, data );
					}
					
					// if we've deleted everything, we've received all data
					// and it matched baseline.
					// again, this is not an event since this is more for
					// simulation than actual algorithmic operation
					if( harddrive.getSize() == 0 ) {
						getStateHolder().getSimulator().stop();
						Thread.currentThread().interrupt();
					}
					
					
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
		return String.format( "State_Client_Read" );
	}
}
