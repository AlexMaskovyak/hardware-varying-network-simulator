package computation.algorithms.clientSpecifiesNonRedundant;

import java.util.Random;

import network.routing.IAddress;
import messages.StorageDeviceMessage;
import simulation.event.DEvent;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
import computation.IData;
import computation.algorithms.AbstractAlgorithm;
import computation.algorithms.listeners.AlgorithmEvent;
import computation.hardware.Cache;
import computation.hardware.Harddrive;
import computation.state.IState;

/**
 * Performs all data storage/retrieval services in response to a client.
 * @author Alex Maskovyak
 *
 */
public class State_Server_Service 
		extends AbstractState
		implements IState<AbstractAlgorithm> {
	
/// Fields
	
	/** address of the client node. */
	protected IAddress _clientAddress;
	/** random number generation. */
	protected Random _rng;
	/***/
	protected int _serviced;
	/** */
	public boolean _cacheRefillEnabled;
	
/// Construction
	
	/** Constructor. */
	public State_Server_Service() {
		init();
	}
	
	/** externalize instantiaton. */
	protected void init() {
		_rng = new Random();
		_serviced = 0;
		_cacheRefillEnabled = false;
	}

	
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
				case CLIENT_REQUESTS_DATA_STORE:
					System.out.println( "Server volunteered got datastore! O.o" );
					break;
				// case where the client requests a range of data
				case CLIENT_REQUESTS_DATA:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SERVICE", 0, 0, 1, 1, 0, 0) );
					
					// get their address
					_clientAddress = (IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS );
					int currentIndex = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					
					//System.out.printf( "Client requests %d from %s.\n", currentIndex, getStateHolder().getComputer().getAddress() );
					
					// send a request to our harddrive
					sendEvent( 
						getStateHolder().getComputer().getCache(),
						new StorageDeviceMessage(
							StorageDeviceMessage.TYPE.RETRIEVE_AND_REMOVE, StorageDeviceMessage.DEVICE_TYPE.CACHE, currentIndex, currentIndex, null ) );
					break;
				// general work case where we make sure that the cache is updated
				case DO_WORK:
					getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SERVICE_FILL_CACHE", 0, 0, 1, 1, 0, 0) );
					
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
			// store data for the client
				case RESPONSE: 
					switch( sdMessage.getDeviceType() ) {
						// harddrive serviced it
						case HARDDRIVE:
							if( sdMessage.getData() != null ) {
								getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SERVICE", 1, 0, 0, 1, 0, 1) );
								
								// send the response
								AlgorithmMessage response = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_RESPONDS_WITH_DATA );
								response.setValue( AlgorithmMessage.INDEX, sdMessage.getIndex() );
								response.setValue( AlgorithmMessage.DATA, sdMessage.getData() );
								response.setValue( AlgorithmMessage.SERVER_ADDRESS, getStateHolder().getComputer().getAddress() );
								
								sendEvent( 
									getStateHolder().getComputer().getCache(),
									new StorageDeviceMessage(
										StorageDeviceMessage.TYPE.DELETE, 
										StorageDeviceMessage.DEVICE_TYPE.CACHE, 
										sdMessage.getIndex(), 
										sdMessage.getIndex(), 
										null ) );
									
								sendMessageDownStack( response, _clientAddress );
							}
							// remove it from the cache so it doesn't take up space
							//StorageDeviceMessage deleteCache = new StorageDeviceMessage( StorageDeviceMessage.TYPE.DELETE, StorageDeviceMessage.DEVICE_TYPE.CACHE, sdMessage.getIndex(), sdMessage.getIndex(), null );
							//sendEvent(
							//	getStateHolder().getComputer().getCache(),
							//	deleteCache );
							
							break;
						// cache services it
						case CACHE:
							getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SERVICE", 0, 0, 0, 1, 0, 0) );
							
							IData data = sdMessage.getData();
							Integer index = sdMessage.getIndex();
							
							// cache miss send request to hd
							if( data == null ) {
								getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SERVICE_CACHE_MISS", 0, 0, 1, 0, 0, 0) );
								// send a request to our harddrive
								sendEvent( 
									getStateHolder().getComputer().getHarddrive(),
									new StorageDeviceMessage(
										StorageDeviceMessage.TYPE.RETRIEVE_AND_REMOVE, StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, index, index, null ) );
								
								return;
							}
							
							getStateHolder().notifyListeners( new AlgorithmEvent( getStateHolder(), event.getEventTime(), "SERVER_SERVICE_CACHE_HIT", 1, 0, 0, 0, 0, 1) );
							
							// cache hit
							AlgorithmMessage responseFromCache = new AlgorithmMessage( AlgorithmMessage.TYPE.SERVER_RESPONDS_WITH_DATA );
							responseFromCache.setValue( AlgorithmMessage.INDEX, sdMessage.getIndex() );
							responseFromCache.setValue( AlgorithmMessage.DATA, sdMessage.getData() );
							responseFromCache.setValue( AlgorithmMessage.SERVER_ADDRESS, getStateHolder().getComputer().getAddress() );
							sendMessageDownStack( responseFromCache, _clientAddress );
							
							if( _cacheRefillEnabled) {
								AlgorithmMessage doWork = new AlgorithmMessage( AlgorithmMessage.TYPE.DO_WORK );
								doWork.setValue( AlgorithmMessage.AMOUNT, 1 );
								//getStateHolder().sendEvent( getStateHolder(), doWork, .00000001, DEvent.INTERNAL );
								sendEvent( getStateHolder(), doWork );
							}
							
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
