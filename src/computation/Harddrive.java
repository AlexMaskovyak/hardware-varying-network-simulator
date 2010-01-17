package computation;

import java.util.Iterator;

import network.IData;
import simulation.AbstractSimulatable;
import simulation.ISimulatable;

public class Harddrive<T extends IData> extends AbstractSimulatable implements IHardware, ISimulatable, Iterable<T> {

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

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
