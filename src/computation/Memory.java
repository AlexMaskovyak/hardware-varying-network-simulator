package computation;

import java.util.Map;

import messages.AlgorithmResponseMessage;
import messages.HarddriveRequestMessage;
import messages.HarddriveStoreMessage;
import messages.MemoryRequestMessage;
import messages.MemoryStoreMessage;
import network.IData;
import simulation.AbstractSimulatable;
import simulation.DefaultDiscreteScheduledEvent;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;
import simulation.IDiscreteScheduledEvent.IMessage;

public class Memory 
		extends Harddrive 
		implements IHardware, ISimulatable {

	protected int _capacity;
	protected int _speed;
	
	public Memory() { super(); }
	
	public int getCapacity() {
		return _capacity;
	}
	
	public void setCapacity(int capacity) {
		_capacity = capacity;
	}
	
	public int getSpeed() {
		return _speed;
	}
	
	public void setSpeed(int speed) {
		_speed = speed;
	}

	@Override
	public void receive(IData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(IData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEvent(IDiscreteScheduledEvent e) {
		IMessage message = e.getMessage();
		if( message instanceof MemoryRequestMessage ) {
			MemoryRequestMessage mMessage = (MemoryRequestMessage)message;
			int index = mMessage.getSequence();
			System.out.println("harddrive request");
			getSimulator().schedule(
				new DefaultDiscreteScheduledEvent<IMessage>(
					this, 
					e.getSource(), 
					getSimulator().getTime() + .0001, 
					getSimulator(), 
					new AlgorithmResponseMessage(getIndex(index))));
		} else if( message instanceof MemoryStoreMessage ) {
			MemoryStoreMessage mMessage = (MemoryStoreMessage)message;
			int index = mMessage.getIndex();
			IData data = mMessage.getData();
			setIndex( index, data );
			System.out.println("harddrive store");
			
		}
	}

	@Override
	public int getBandwidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setBandwitdh(int bandwidth) {
		// TODO Auto-generated method stub
		
	}
}
