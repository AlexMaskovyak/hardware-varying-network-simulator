import java.util.EventListener;
import java.util.EventObject;


public interface ISimulatable extends EventListener {

	public void handleTickEvent(EventObject o);
}
