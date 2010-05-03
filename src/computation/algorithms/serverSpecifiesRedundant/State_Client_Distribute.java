package computation.algorithms.serverSpecifiesRedundant;

import messages.StorageDeviceMessage;
import messages.StorageDeviceMessage.TYPE;
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
public class State_Client_Distribute 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm> {

/// Fields
	
	/** primary server's address. */
	protected IAddress _primaryServerAddress;
	/** current index we are distributing.*/
	protected int _currentIndex;
	/** ending index to which to distribute data. */
	protected int _endIndex;
	/** total data sent. */
	protected int _dataSent;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param primaryServerAddress to which to send volunteer addresses.
	 */
	public State_Client_Distribute( 
			IAddress primaryServerAddress, 
			int startIndex, 
			int endIndex ) {
		_primaryServerAddress = primaryServerAddress;
		_currentIndex = startIndex;
		_endIndex = endIndex;
		init();
	}
	
	/** externalize instantiation. */
	protected void init() {
		_dataSent = 0;
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
				// the distribution process
				case CLIENT_DO_DISTRIBUTE:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "LOCAL", 0, 0, 0, 1, 0, 0) );
					
					// request index from hd
					sendEvent( 
						getStateHolder().getComputer().getHarddrive(),
						new StorageDeviceMessage( StorageDeviceMessage.TYPE.RETRIEVE, StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, _currentIndex, _currentIndex, null ) );
					
					_currentIndex++;
					
					// schedule more work
					if( _currentIndex <= _endIndex ) {
						
						sendInstantEvent( 
							new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_DO_DISTRIBUTE ) );
					}
					break;
				default: break;
			}
		// our requests have been fulfilled from harddrive
		} else if( message instanceof StorageDeviceMessage ) {
			StorageDeviceMessage stMessage = (StorageDeviceMessage)message;
			switch( stMessage.getType() ) {
			
				// got data requested, send it to a server
				case RESPONSE:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "LOCAL", 1, 0, 1, 1, 0, 1 ) );
					
					// repackage the data and send it to the server
					AlgorithmMessage dataStore = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_DATA_STORE );
					dataStore.setValue( AlgorithmMessage.INDEX, stMessage.getIndex() );
					dataStore.setValue( AlgorithmMessage.DATA, stMessage.getData() );
	
					// send it
					sendMessageDownStack( dataStore, _primaryServerAddress );
	
					System.out.println( "distribute data" + stMessage.getIndex() );
					
					_dataSent++;
					
					// we just sent the last thing
					if( _dataSent == _endIndex + 1 ) {
						System.out.println("done distributing");
						// go to the read state and wait to be able to read
						updateStateHolder( new State_Client_Read( _primaryServerAddress, 0, _endIndex ) );
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
		return String.format( "State_Client_Distribute" );
	}
}
