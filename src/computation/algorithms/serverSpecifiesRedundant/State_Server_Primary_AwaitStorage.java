package computation.algorithms.serverSpecifiesRedundant;

import java.util.List;

import messages.StorageDeviceMessage;
import network.routing.IAddress;
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
public class State_Server_Primary_AwaitStorage 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm>  {


/// Fields
	
	/** server groups, data slice groups. */
	protected List<List<IAddress>> _serverGroups;
	/** total size of the data */
	protected int _dataSize;
	/** total number of data slices (servergroups) */
	protected int _dataSlices;
	/** total amount of data each slice handles. */
	protected int _dataPerSlice;
	/** amount of data stored successfully. */
	protected int _serversDone;
	/** total data redundancy. */
	protected int _redundancy;
	/** address of the client. */
	protected IAddress _clientAddress;
	/** starting index of the data. */
	protected int _startIndex;
	/** ending index of the data. */
	protected int _endIndex;
	/** total data we have stored. */
	protected int _dataStored;
	/** the total amount the last slice must store. */
	protected int _dataForLastSlice;
	
/// Construction
	
	/**
	 * Default constructor.
	 * @param serverGroups containing information.
	 */
	public State_Server_Primary_AwaitStorage( 
			List<List<IAddress>> serverGroups,
			IAddress clientAddress,
			int redundancy,
			int startIndex,
			int endIndex ) {
		_serverGroups = serverGroups;
		_clientAddress = clientAddress;
		_startIndex = startIndex;
		_endIndex = endIndex;
		_redundancy = redundancy;
		_dataSize = ( endIndex - startIndex ) + 1;
		_dataSlices = _serverGroups.size();
		_dataPerSlice = ( _dataSize / _dataSlices );
		_dataForLastSlice = ( _dataPerSlice ) + ( _dataSize - ( _dataSlices * _dataPerSlice ));
		_serversDone = 0;
		_dataStored = 0;
	}
	
	
	
	/**
	 * Obtains the index of the servergroup that handles the data associated 
	 * with that index.
	 * @param dataIndex associated with the data.
	 * @return index of the servergroup which lists addresses which are to 
	 * receive the data.
	 */
	/*public int getServerGroupIndex( int dataIndex ) {
		return dataIndex / _dataPerSlice ;
	}*/
	public int getServerGroupIndex( int dataIndex ) {
		int index = dataIndex / _dataPerSlice;
		return ( index == _dataSlices ) ? index - 1 : index;
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
			int dataIndex;
			IData data;
			
			AlgorithmMessage aMessage = (AlgorithmMessage)message;
			switch( aMessage.getType() ) {
				// nope, you missed your chance, maybe some other time
				case SERVER_VOLUNTEERS:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_STORAGE", 0, 0, 1, 1, 0, 0) );
					
					// reject volunteers since we already have enough
					sendMessageDownStack( 
						new AlgorithmMessage( 
							AlgorithmMessage.TYPE.CLIENT_REJECTS_VOLUNTEER), 
							(IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS ) );
					break;
			
				// storing data
				case CLIENT_REQUESTS_DATA_STORE:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_STORAGE", 0, 0, 0, 1, 0, 0) );
					
					dataIndex = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					data = (IData)aMessage.getValue( AlgorithmMessage.DATA );
					
					//System.out.println( "serverGroups: " + _serverGroups.size() );
					List<IAddress> serverAddresses = _serverGroups.get( getServerGroupIndex( dataIndex ) );
					for( IAddress serverAddress : serverAddresses ) {
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.SERVER_ADDRESS, serverAddress );
						doWork.setValue( AlgorithmMessage.DATA, data );
						doWork.setValue( AlgorithmMessage.INDEX, dataIndex );
						sendInstantEvent( doWork );
					}
					
					break;
				case DO_WORK:
					dataIndex = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					data = (IData)aMessage.getValue( AlgorithmMessage.DATA );
					IAddress serverAddress = (IAddress)aMessage.getValue( AlgorithmMessage.SERVER_ADDRESS );
					
					// is this for us?
					if( serverAddress.equals( getStateHolder().getComputer().getAddress() ) ) {
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_STORAGE", 0, 1, 0, 0, 1, 0) );
						sendEvent(
							getStateHolder().getComputer().getHarddrive(),
							new StorageDeviceMessage( 
								StorageDeviceMessage.TYPE.STORE, 
								StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE,
								dataIndex,
								dataIndex,
								data ) );
						sendEvent(
								getStateHolder().getComputer().getCache(),
								new StorageDeviceMessage( 
									StorageDeviceMessage.TYPE.STORE, 
									StorageDeviceMessage.DEVICE_TYPE.CACHE,
									dataIndex,
									dataIndex,
									data ) );
						_dataStored++;
						if( _dataStored == _dataPerSlice ) {
							sendInstantEvent( new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_INDICATES_READ_READY ) );
						}
					// tell the server to store it
					} else {
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_STORAGE", 0, 1, 1, 0, 0, 0) );
						AlgorithmMessage dataStore = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_DATA_STORE  );
						dataStore.setValue( AlgorithmMessage.SERVER_ADDRESS, serverAddress );
						dataStore.setValue( AlgorithmMessage.DATA, data );
						dataStore.setValue( AlgorithmMessage.INDEX, dataIndex );
						
						sendMessageDownStack(
							dataStore,
							serverAddress );
					}
					
					
					break;
					
				case SERVER_INDICATES_READ_READY:
					_serversDone++;
					// one less than the total number of servers since we are done as well
					if( _serversDone == _dataSlices * _redundancy ) {
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_PRIMARY_AWAIT_STORAGE", 0, 0, 1, 0, 0, 0) );
						sendMessageDownStack( 
							new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_INDICATES_READ_READY ),
							_clientAddress );
						updateStateHolder( 
							new State_Server_Primary_Service(
								_serverGroups, 
								_startIndex, 
								_endIndex ) );
						
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.AMOUNT, getStateHolder().getComputer().getCache().freeSpace() );
						sendEvent( getStateHolder(), doWork, DEvent.INTERNAL );
						
					}
				default: break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Server_AwaitStorage" );
	}
}
