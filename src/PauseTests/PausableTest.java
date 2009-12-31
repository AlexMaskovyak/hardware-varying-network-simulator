package PauseTests;

public class PausableTest implements Runnable {

	public static Object lock = new Object();
	public static boolean condition = false;
	
	public void run() {
		while(true) {
			synchronized(lock) {
				try {
					while(condition) { lock.wait(); }
					condition = true;
					lock.notifyAll();
					System.out.println("Pausable.");
				} catch( Exception e ) { e.printStackTrace(); break; }
			}
		}
		System.out.println("what?");
	}
	
	public static void main(String... args) throws Exception {
		PausableTest p = new PausableTest();
		Thread t = new Thread(p);
		t.start();
		while(true) {
			synchronized(lock) {
				while(!condition) { lock.wait(); }
				condition = false;
				lock.notifyAll();
				System.out.println("Main.");
			}
		}
	}
	
}
