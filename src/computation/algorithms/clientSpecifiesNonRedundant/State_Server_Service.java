package computation.algorithms.clientSpecifiesNonRedundant;

import network.routing.IAddress;
import messages.StorageDeviceMessage;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.state.IState;
import computation.state.IStateHolder;

/**
 * Performs all data storage/retrieval services in response to a client.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Service 
		extends AbstractState
		implements IState<AbstractAlgorithm> {
	
	protected IAddress _clientAddress;
	
	@Override
	public void handleEventDelegate(IDEvent event) {
		IMessage message = event.getMessage();
		if( message instanceof AlgorithmMessage ) {
			AlgorithmMessage aMessage = (AlgorithmMessage)message;
			switch( aMessage.getType() ) {
			
				// case where the client requests a range of data
				case CLIENT_REQUESTS_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVICE", 0, 0, 1, 1, 0, 0) );
					
					// get their address
					_clientAddress = (IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS );
					int currentIndex = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					
					// send a request to our harddrive
					sendEvent( 
						getStateHolder().getComputer().getCache(),
						new StorageDeviceMessage(
							StorageDeviceMessage.TYPE.RETRIEVE, StorageDeviceMessage.DEVICE_TYPE.CACHE, currentIndex, currentIndex, null ) );
					break;
				// general work case where we make sure that the cache is updated
				case DO_WORK:
					// ask cache how many resources it has
					
					break;
			}
		} else if( message instanceof StorageDeviceMessage ) {
			StorageDeviceMessage sdMessage = (StorageDeviceMessage)message;
			switch( sdMessage.getType() ) {
			
				case RESPONSE: 
					switch( sdMessage.getDeviceType() ) {
						// harddrive serviced it
						case HARDDRIVE:
							getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVICE", 0, 0, 0, 1, 0, 1) );
							
							
							AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_RESPONDS_WITH_DATA );
							response.setValue( AlgorithmMessage.INDEX, sdMessage.getIndex() );
							response.setValue( AlgorithmMessage.DATA, sdMessage.getData() );
							
							sendMessageDownStack( response, _clientAddress );
							break;
							
						case CACHE:
							getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVICE", 0, 0, 0, 1, 0, 1) );
							
							IData data = sdMessage.getData();
							Integer index = sdMessage.getIndex();
							
							// cache miss send request to hd
							if( data == null ) {
								// send a request to our harddrive
								sendEvent( 
									getStateHolder().getComputer().getHarddrive(),
									new StorageDeviceMessage(
										StorageDeviceMessage.TYPE.RETRIEVE, StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, index, index, null ) );
								
								return;
							}
							
							// cache hit
							AlgorithmMessage responseFromCache = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_RESPONDS_WITH_DATA );
							responseFromCache.setValue( AlgorithmMessage.INDEX, sdMessage.getIndex() );
							responseFromCache.setValue( AlgorithmMessage.DATA, sdMessage.getData() );
							
							sendMessageDownStack( responseFromCache, _clientAddress );
							
							break;
					
					}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format( "State_Server_Service" );
	}
}
