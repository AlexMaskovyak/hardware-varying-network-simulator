package computation.algorithms.serverSpecifiesRedundant;

import java.util.Random;

import messages.StorageDeviceMessage;
import network.routing.IAddress;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.algorithms.serverSpecifiesRedundant.AbstractState;
import computation.algorithms.serverSpecifiesRedundant.AlgorithmMessage;
import computation.algorithms.serverSpecifiesRedundant.State_NullRole;
import computation.algorithms.serverSpecifiesRedundant.State_Server_Primary_AwaitStorage;
import computation.hardware.Cache;
import computation.hardware.Harddrive;
import computation.state.IState;

/**
 * Volunteered State where the server is waiting for confirmation of status.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Secondary_Service 
		extends AbstractState<ServerSpecifiesRedundantAlgorithm>
		implements IState<ServerSpecifiesRedundantAlgorithm>  {	

/// Fields
	
	/** address of the client node. */
	protected IAddress _clientAddress;
	/** random number generation. */
	protected Random _rng;
	
	
/// Construction
	
	/** Constructor. */
	public State_Server_Secondary_Service() {}
	
	
/// IState

	/*
	 * (non-Javadoc)
	 * @see computation.algorithms.clientSpecifiesNonRedundant.AbstractState#handleEventDelegate(simulation.event.IDEvent)
	 */
	@Override
	public void handleEventDelegate(IDEvent event) {
		IMessage message = event.getMessage();
		if( message instanceof AlgorithmMessage ) {
			AlgorithmMessage aMessage = (AlgorithmMessage)message;
			switch( aMessage.getType() ) {
			
				// case where the client requests a range of data
				case CLIENT_REQUESTS_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SECONDARY_SERVICE", 0, 0, 1, 1, 0, 0) );
					
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
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVICE_FILL_CACHE", 0, 0, 1, 1, 0, 0) );
					
					int freespace = (Integer)aMessage.getValue( AlgorithmMessage.AMOUNT );
					
					Harddrive hd = getStateHolder().getComputer().getHarddrive();
					Cache cache = getStateHolder().getComputer().getCache();
					
					// ask hd to store something into cache
					getStateHolder().sendEventAsProxy(
							cache,
							hd,
							new StorageDeviceMessage( 
								StorageDeviceMessage.TYPE.RETRIEVE, 
								StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, 
								hd.getFilledIndex(),
								-1,
								null ) );
					
					if( freespace > 0 ) {
						aMessage.setValue( AlgorithmMessage.AMOUNT, freespace - 1 );
						sendEvent( getStateHolder(), aMessage );
					}
					
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
							
							System.out.println( "send data to client");
							
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
							
							AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
							doWork.setValue( AlgorithmMessage.AMOUNT, 1 );
							sendEvent( getStateHolder(), doWork );
							
							System.out.println( "send data to client");
							
							
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
		return String.format( "State_Client_Volunteered" );
	}
}
