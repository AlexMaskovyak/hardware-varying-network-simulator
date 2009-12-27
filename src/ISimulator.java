import java.util.EventObject;


public interface ISimulator {
	
	public void addListener(ISimulatable simulatable);
	
	public void removeListener(ISimulatable simulatable);
	
	public void fireEvent(EventObject o);
	
	public void simulate(int time);
	
	public void start();
	
	public void step();
	
	public void pause();
	
	public void stop();
	
}
