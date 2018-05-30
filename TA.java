import java.util.concurrent.Semaphore;


public class TA implements Runnable {

	//ici on a des variable similaires à celle dans l'étudiant
	
	private Semaphore sitLineTA;
	private Semaphore TAdispo;
	
	private WakeupSem wakeupTA;
	final int sleepThreadTA = 4000;
	
	private Thread runningThreadTA;

	public TA(WakeupSem wake, Semaphore sitWait, Semaphore dispo2) {
		wakeupTA = wake;
		sitLineTA = sitWait;
		runningThreadTA = Thread.currentThread();
		TAdispo = dispo2;
	}

	
	public void run() {
		while (true) {
			try {
				System.out.println("Pas d'étudiant --> TA dort");
				wakeupTA.releaseTA(); 
				System.out.println("Un étudiant réveille le TA");

				runningThreadTA.sleep(sleepThreadTA);
				
				
				if (sitLineTA.availablePermits() != 3) {
					do {
						runningThreadTA.sleep(sleepThreadTA);
						sitLineTA.release();
					} while (sitLineTA.availablePermits() != 3);
				}
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
}
