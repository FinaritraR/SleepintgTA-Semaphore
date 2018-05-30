import java.util.Random;
import java.util.concurrent.Semaphore;


public class SleepingTA {

	static int etudiantsTotal = 4;

	public static void main(String[] args) {
		
		//semaphores et nombre aléatoire
		Random randomWait = new Random();
		Semaphore sitWait = new Semaphore(3);
		Semaphore TAdisponible = new Semaphore(1);
		WakeupSem wakeupTA = new WakeupSem();
		
		
		//Nouveau thread TA et étudiant
		for (int i = 0; i < etudiantsTotal; i++) {
			Thread student = new Thread(new Student(randomWait.nextInt(15), wakeupTA, sitWait, TAdisponible, i + 1));
			student.start();
		}

		
		Thread ta = new Thread(new TA(wakeupTA, sitWait, TAdisponible));
		ta.start();
	}
}


// Semaphore pour le signal qui réveil le TA
class WakeupSem {
	private boolean disponible = false;

	// pour travailler avec le TA et le TA devient donc indisponible
	public synchronized void takeTA() {
		this.disponible = true;
		this.notify();
	}

	// pour relâcher le TA et le rendre libre pour qu'il travail avec d'autres étudiants
	public synchronized void releaseTA() throws InterruptedException {
		while (!this.disponible)
			wait();
		this.disponible = false;
	}
}
