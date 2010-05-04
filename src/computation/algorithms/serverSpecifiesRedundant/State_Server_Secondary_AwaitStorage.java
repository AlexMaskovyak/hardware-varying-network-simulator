package computation.algorithms.serverSpecifiesRedundant;

import network.routing.IAddress;
import messages.StorageDeviceMessage;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Secondary_AwaitStorage 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm>  {	
	
/// Fields
	
	/** primary server's address */
	protected IAddress _primaryAddress;
	/** amount of data to be stored. */
	protected int _dataToStoreTotal;
	/** amount of data stored successfully. */
	protected int _dataStored;
	
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param primaryAddress of the server controlling us.
	 */
	public State_Server_Secondary_AwaitStorage( 
			IAddress primaryAddress,
			int dataToStoreTotal ) {
		_primaryAddress = primaryAddress;
		_dataToStoreTotal = dataToStoreTotal;
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
			
				// store data
				case CLIENT_REQUESTS_DATA_STORE:
					ServerSpecifiesRedundantAlgorithm algorithm = getStateHolder();
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SECONDARY_AWAIT_STORAGE", 0, 1, 0, 0, 1, 0) );
					sendEvent( 
						algorithm.getComputer().getHarddrive(), 
						new StorageDeviceMessage( 
							StorageDeviceMessage.TYPE.STORE,
							StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE,
							(Integer)aMessage.getValue(AlgorithmMessage.INDEX),
							-1,
							(IData)aMessage.getValue(AlgorithmMessage.DATA) ) );
					_dataStored++;
					// we've stored everything we need to
					if( _dataStored == _dataToStoreTotal ) {
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SECONDARY_AWAIT_STORAGE", 0, 0, 1, 0, 0, 0) );
						sendMessageDownStack(
							new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_INDICATES_READ_READY ),
							_primaryAddress );
						updateStateHolder( new State_Server_Secondary_Service() );
						
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.AMOUNT, getStateHolder().getComputer().getCache().freeSpace() );
						sendEvent( getStateHolder(), doWork, DEvent.INTERNAL );
						
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
		return String.format( "State_Server_Secondary_AwaitStorage" );
	}
}