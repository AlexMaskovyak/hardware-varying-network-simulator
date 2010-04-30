package computation.algorithms.clientSpecifiesNonRedundant;

import network.routing.IAddress;
import messages.StorageDeviceMessage;
import simulation.event.IDEvent;
import simulation.event.IDEvent.IMessage;
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
					
					_clientAddress = (IAddress)aMessage.getValue( AlgorithmMessage.CLIENT_ADDRESS );
					int currentIndex = (Integer)aMessage.getValue( AlgorithmMessage.INDEX );
					
					
					sendEvent( 
						getStateHolder().getComputer().getHarddrive(),
						new StorageDeviceMessage(
							StorageDeviceMessage.TYPE.RETRIEVE, StorageDeviceMessage.DEVICE_TYPE.HARDDRIVE, currentIndex, currentIndex, null ) );
					break;
			}
		} else if( message instanceof StorageDeviceMessage ) {
			StorageDeviceMessage sdMessage = (StorageDeviceMessage)message;
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
					
					break;
			
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
