package computation.hardware;

import messages.StorageDeviceMessage;
import simulation.event.IDEvent;
import simulation.simulatable.PerformanceRestrictedSimulatable;

public class Cache 
		extends Harddrive 
		implements IHardware {
	
	
	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#handleEventDelegate(simulation.event.IDiscreteScheduledEvent)
	 */
	@Override
	protected void handleEventDelegate( IDEvent e ) {
		
		StorageDeviceMessage message = (StorageDeviceMessage)e.getMessage();
		switch( message.getType() ) {
			case RETRIEVE:
				sendEvent( 
					e.getSource(), 
					new StorageDeviceMessage( StorageDeviceMessage.TYPE.RESPONSE, StorageDeviceMessage.DEVICE_TYPE.CACHE, message.getIndex(), message.getRequestId(), getIndex( message.getIndex() ) ) );
				break;
			case RETRIEVE_AND_REMOVE:
				sendEvent( 
						e.getSource(), 
						new StorageDeviceMessage( StorageDeviceMessage.TYPE.RESPONSE, StorageDeviceMessage.DEVICE_TYPE.CACHE, message.getIndex(), message.getRequestId(), getIndex( message.getIndex() ) ) );
				deleteIndex( message.getIndex() );
				break;
				// this case allows for cache and harddrives to communicate directly
				// with one another
			case RESPONSE:
				storeData( message.getIndex(), message.getData() );
				break;				
			case STORE:
				storeData( message.getIndex(), message.getData() );
				break;
			case FREE_SPACE:
				StorageDeviceMessage response = 
					new StorageDeviceMessage( StorageDeviceMessage.TYPE.FREE_SPACE, StorageDeviceMessage.DEVICE_TYPE.CACHE, -1, -1, null );
				response.setFreeSpace( getCapacity() - getSize() );
				sendEvent( e.getSource(), response );
	
				break;
			case DELETE:
				deleteIndex( message.getIndex() );
				break;
			default: break;
		}
	}
	
// PublicCloneable 

	/*
	 * (non-Javadoc)
	 * @see simulation.simulatable.PerformanceRestrictedSimulatable#createNew()
	 */
	@Override
	protected PerformanceRestrictedSimulatable createNew() {
		Cache result = new Cache();
		result.setCapacity( this.getCapacity() );
		return result;
	}
}
