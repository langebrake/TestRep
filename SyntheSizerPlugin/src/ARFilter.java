import java.io.Serializable;

/**
 * Ein Filter, der Attack und Release auf einem Sample-Buffer linear realisiert
 * 
 * @author Hendrik Langebrake
 *
 */
public class ARFilter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8629821990728320954L;

	// benoetigte Variablen
	private int sample_rate;

	// so viele samples soll die Attack dauern
	private int attack_samples;
	// aktueller Faktor, mit dem ein Sample multipliziert wird
	// (am Anfang 0, am Ende der Attackphase 1)
	private float attack_factor;
	// so viele Samples soll das Release dauern
	private int release_samples;
	// aktueller Faktor mit dem Sample waehrend Release multipliziert wird
	// (1 am Anfang, 0 am Ende der Releasephase)
	private float release_factor;

	// gibt an, ob buffer gerade lautlos sein sollte
	private boolean isQuiet;

	// gibt an, welche Phase aktiv sein sollte
	private boolean attackActive;
	private boolean releaseActive;

	public ARFilter(int sample_rate) {
		this.sample_rate = sample_rate;
	}

	/**
	 * setze Attack-Phasendauer auf angegebene Millisekundenzeit
	 * 
	 * @param milliseconds
	 */
	public void setAttack(int milliseconds) {
		if (milliseconds < 0) {
			throw new IllegalArgumentException();
		}
		// berechne, wie viele Samples die Attackphase dann dauert
		attack_samples = (int) ((float) milliseconds / 1000 * (float) sample_rate);

	}

	/*
	 * setze Release-Phasendauer auf angegebene Millisekundenzeit
	 */
	public void setRelease(int milliseconds) {
		if (milliseconds < 0) {
			throw new IllegalArgumentException();
		} else {
			// berechne die Sampleanzahl, die fuer Release-Dauer bearbeitet
			// werden
			// muss
			release_samples = (int) ((float) milliseconds / 1000 * (float) sample_rate);
		}
	}

	// zaehler, wie viele Samples in der jeweiligen Phase bereits bearbeitet
	// wurden
	private int attack_counter;
	private int release_counter;

	/**
	 * wende Attack/Release auf den Buffer an
	 * 
	 * @param buf
	 */
	public void ARBuffer(byte[] buf) {

		float factor = 0;
		if (attack_counter > attack_samples)
			attackActive = false;
		if (release_counter > release_samples) {
			releaseActive = false;
		}

		if (attackActive) {
			isQuiet = false;
			float attack_delta = 1 / (float) attack_samples;
			for (int i = 0; i < buf.length; i++) {
				attack_factor += attack_delta;
				factor = attack_factor;
				attack_counter++;
				if (attack_counter > attack_samples || factor > 1) {
					attackActive = false;
					factor = 1;
					break;
				}
				buf[i] *= factor;
			}

		} else if (!attackActive && releaseActive) {
			float release_delta = 1 / (float) release_samples;
			for (int i = 0; i < buf.length; i++) {
				release_factor -= release_delta;
				factor = release_factor;
				release_counter++;
				if (release_counter >= release_samples || factor < 0) {
					buf[i] = 0;
					releaseActive = false;
					isQuiet = true;
				}
				buf[i] *= factor;
			}

		} else if (isQuiet) {
			for (int i = 0; i < buf.length; i++) {
				buf[i] = 0;
			}
		}

	}

	/**
	 * starte neue Attack-Phase
	 */
	public void startAttack() {
		attack_counter = 0;
		attack_factor = 0;
		releaseActive = false;
		attackActive = true;
		isQuiet = false;

	}

	/**
	 * starte neue Release-Phase
	 */
	public void startRelease() {
		release_counter = 0;
		release_factor = 1;
		releaseActive = true;

	}

	/**
	 * sollte Buffer momenten lautlos sein?
	 * 
	 * @return
	 */
	public boolean isQuiet() {
		return isQuiet;
	}
}
