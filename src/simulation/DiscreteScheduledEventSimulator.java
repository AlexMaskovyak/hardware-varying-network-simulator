package simulation;

import java.util.PriorityQueue;

public class DiscreteScheduledEventSimulator extends Simulator implements ISimulator {

	protected PriorityQueue<IDiscreteScheduledEvent> _queue;
	
	public DiscreteScheduledEventSimulator() {
		super();
	}
	
	public void init() {
		super.init();
		_queue = new PriorityQueue<IDiscreteScheduledEvent>();
	}
	
	public void schedule(IDiscreteScheduledEvent event, int time) {
		//_queue.
	}
}
