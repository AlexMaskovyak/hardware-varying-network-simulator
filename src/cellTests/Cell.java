package cellTests;

/**
 * Cell class abstracts out the producer/consumer synchronization problem.
 * @author Alex Maskovyak (influenced by Axel Schreiner)
 *
 * @param <T>
 */
public class Cell<T> {

	/** value to be passed. */
	private T content;
	
	/** state of content */
	private boolean full = false;
	
	/**
	 * Thread-safe access to content/control execution.	
	 * @return value of content, returns when permitted.
	 * @throws InterruptedException
	 */
	public synchronized T get() throws InterruptedException {
		while( !full ) { 
			try { wait(); }
			catch (InterruptedException e) { throw e;}
		}
		full = false;
		notifyAll();
		T val = content;
		return val;
	}
	
	/**
	 * Thread-safe access to content/control execution.
	 * @param item token to store, returns when permitted.
	 * @throws InterruptedException
	 */
	public synchronized void put(T item) throws InterruptedException {
		while( full ) {
			try { wait(); }
			catch (InterruptedException e) { throw e; }
		}
		full = true;
		notifyAll();
		content = item;
	}
}
