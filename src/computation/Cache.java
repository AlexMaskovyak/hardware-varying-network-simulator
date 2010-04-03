package computation;

import network.IData;
import simulation.AbstractSimulatable;
import simulation.IDiscreteScheduledEvent;
import simulation.ISimulatable;

public class Cache extends AbstractSimulatable implements IHardware {
	
	protected int _capacity;
	protected int _speed;
	
	public Cache() { super(); }
	
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
		// TODO Auto-generated method stub
		
	}
}
