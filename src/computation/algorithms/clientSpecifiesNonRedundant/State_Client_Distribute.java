package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.Iterator;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import messages.StorageDeviceMessage;
import network.routing.IAddress;

import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;

/**
 * The default role before receiving indication about client or server status.
 * @author Alex Maskovyak
 *
 */
public class State_Client_Distribute
		extends AbstractState 
		implements IState<AbstractAlgorithm> {

/// Fields	

	/** servers we have found. */
	protected List<IAddress> _servers;
	/** iterator for servers. */
	protected Iterator<IAddress> _iterator;
	/** ending index for data. */
	protected int _endIndex;
	
/// Construction
	
	/**
	 * Servers to seek.
	 * @param servers we need to obtain.
	 */
	public State_Client_Distribute( List<IAddress> servers ) {
		_servers = servers;
		_iterator = servers.iterator();
		_endIndex = -1;
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
			
				// nope, you missed your chance, maybe some other time
				case CLIENT_ACCEPTS_VOLUNTEER:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SETUP", 0, 0, 1, 1, 0, 0) );
					
					sendMessageDownStack( 
						new AlgorithmMessage( 
							AlgorithmMessage.TYPE.CLIENT_REJECTS_VOLUNTEER), 
							(IAddress)aMessage.getValue( AlgorithmMessage.VOLUNTEER_ADDRESS ) );
					break;
				// this is our call to retrieve from local
				case DO_WORK:
					// request information from harddrive
					int currentIndex = (Integer)aMessage.getValue( AlgorithmMessage.START_INDEX );
					int endIndex = (Integer)aMessage.getValue( AlgorithmMessage.END_INDEX );
					_endIndex = endIndex;
					
					// request data
					sendEvent( getStateHolder().getComputer().getHarddrive(), 
						new StorageDeviceMessage( StorageDeviceMessage.TYPE.RETRIEVE, StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, currentIndex++, -1, null ) );
					
					// more data to send, schedule it
					if( currentIndex <= endIndex ) {
						getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "LOCAL", 0, 0, 1, 0, 0, 0) );
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.START_INDEX, currentIndex );
						doWork.setValue( AlgorithmMessage.END_INDEX, endIndex );
						sendEvent( getStateHolder(), doWork );
						return;
					}
					
					break;
			}
			// our requests have been fulfilled from harddrive
		} else if( message instanceof StorageDeviceMessage ) {
			StorageDeviceMessage stMessage = (StorageDeviceMessage)message;
			switch( stMessage.getType() ) {
			
				// got data requested, send it to a server
				case RESPONSE:
					AlgorithmMessage dataStore = new AlgorithmMessage( AlgorithmMessage.TYPE.CLIENT_REQUESTS_DATA_STORE);
					dataStore.setValue( AlgorithmMessage.INDEX, stMessage.getIndex() );
					dataStore.setValue( AlgorithmMessage.DATA, stMessage.getData() );
	
					// round robin server select
					if( !_iterator.hasNext() ) {
						_iterator = _servers.iterator();
					}
					
					// send it
					sendMessageDownStack( dataStore, _iterator.next() );

					System.out.println( "distribute data" + stMessage.getIndex() );
					
					// we just sent the last thing
					if( stMessage.getIndex() == _endIndex ) {
						System.out.println("done distributing");
						updateStateHolder( new State_Client_ConfirmServerReady( _servers ) );
						AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
						doWork.setValue( AlgorithmMessage.START_INDEX, 0 );
						doWork.setValue( AlgorithmMessage.END_INDEX, _servers.size() - 1 );
						sendEvent( getStateHolder(), doWork );
					}
					break;
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
