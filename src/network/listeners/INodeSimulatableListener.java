package network.listeners;

import simulation.simulatable.listeners.ISimulatableEvent;
import simulation.simulatable.listeners.ISimulatableListener;

public interface INodeSimulatableListener extends ISimulatableListener {

	public void sendUpdate(NodeSimulatableEvent e);
}
