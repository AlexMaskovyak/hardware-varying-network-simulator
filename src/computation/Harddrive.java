package computation;

import network.IData;
import simulation.AbstractSimulatable;
import simulation.ISimulatable;

public class Harddrive extends AbstractSimulatable implements IHardware, ISimulatable {

	protected int _capacity;
	protected int _speed;
	
	public Harddrive() { super(); }
	
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
}
