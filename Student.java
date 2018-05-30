import java.util.concurrent.Semaphore;


public class Student implements Runnable{

	// les variables utilis�s: un s�maphore pour identifier la chaise et savoir si elle est libre
	// une variable de la class qui travaille comme semaphore pour r�veiller le TA
	// un temps de 4000 ms pour utiliser sleep() avec le thread
	// la variable the la Thread en court
	private Semaphore sitLine;
	private Semaphore disponible;
	
	private int etudiantN;
	private int time;
	
	private WakeupSem wakeupTA;
	
	final int sleepThread = 4000;
	
	private Thread runningThreadStud;
	
	
	public Student(int program, WakeupSem wake, Semaphore sitWait, Semaphore dispo, int n) {
		time = program;
		wakeupTA = wake;
		sitLine = sitWait;
		disponible = dispo;
		etudiantN = n;
		runningThreadStud = Thread.currentThread();
	}

	
	public void run() {

		
		while (true) {
			try {
				System.out.println("Etudiant " + etudiantN + " est lanc� pour " + time + " seconds");
				runningThreadStud.sleep(time * 1000);
				
				/*
				on utilise la m�thode tryAcquire() pour essayez de prendre la place
				si cela marche �a veut dire que le TA est libre et les �tudiant peuvent
				commencer � travailler
				
				Sinon l'�tudiant devra attendre sur les chaises et dans les dernier cas,
				si les chaises sont pleines il ne pourront pas voir le TA */
				
				if (disponible.tryAcquire()) {
					try {
						wakeupTA.takeTA();
						System.out.println("Etudiant " + etudiantN + " r�veil le TA.");
						System.out.println("Etudiant " + etudiantN + " travail avec le TA.");
						runningThreadStud.sleep(sleepThread);
						
						System.out.println("Etudiant " + etudiantN + " fini avec le TA.");
						System.out.println();
						
					} catch (InterruptedException e) {
						continue;
					} 
					
					finally {
						disponible.release();
					}
					
				} else {
					
					if (sitLine.tryAcquire()) {
						try {
							System.out.println("Etudiant " + etudiantN + " s'assoie � la chaise "
									+ ((3 - sitLine.availablePermits())));
							
							disponible.acquire();
							
							System.out.println("Etudiant " + etudiantN + " travaille avec le TA.");
							runningThreadStud.sleep(sleepThread);
							
							System.out.println("Etudiant " + etudiantN + " fini avec le TA.");
							disponible.release();
							
						} catch (InterruptedException e) {
							continue;
						}
					} else {
						System.out.println("Plus de chaise pour l'�tudiant " + etudiantN);
					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
