import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Diese Klasse verwaltet Oszillatoren und weist ihnen Frequenzen zu. Die
 * erzeugten Samples werden an den Audio Output des Systems gesendet
 * 
 * @author Hendrik Langebrake
 */

public class Synth implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1078586632655123967L;
	// legt fest, wie viele toene maximal gleichzeitig gespielt werden duerfen
	private final int MAXPOLY;
	private final int BUFSIZE;
	// legt fest, wie oft pro Sekunde der buffer vom Lautsprecher abgetastet
	// wird
	private int sampleRate;
	// der direkte Draht zum Lautsprecher
	private SourceDataLine sdl = null;
	// gibt an, ob unser Synthesizer laeuft
	private boolean run = false;
	// die Oszillatoren des Synthesizers, die Grundtonfrequenzen nehmen und
	// einen
	// Ton erzeugen der eventuell Obertoene enthealt (je nach Einstellung)
	private ComplexOscillator[] oscillators;
	// Alle Grundtonfrequenzen die gleichzeitig gespielt werden sollen
	private double[] polyFrequencies;
	// Array das sich merkt, welche Frequenz aktuell von einem Oszillator
	// gespielt wird
	private boolean[] playingFrequencies;

	// Filter
	private VibratoFilter vibrato;
	private boolean useVibrato;


	// ARFilter sind immer aktiv, jeder Oszillator bekommt seinen eigenen
	private ARFilter[] ars;

	private DeinFilter deinFilter;
	private boolean useDeinFilter;

	public Synth() {
		// Wenn der Synthesizer erzeugt wird, legen wir folgende Parameter fest
		MAXPOLY = 4;
		BUFSIZE = 1000;
		sampleRate = 44100;
		polyFrequencies = new double[MAXPOLY];
		playingFrequencies = new boolean[MAXPOLY];
		oscillators = new ComplexOscillator[MAXPOLY];

		for (int i = 0; i < oscillators.length; i++) {
			oscillators[i] = createOsc(true);
		}

		vibrato = new VibratoFilter(sampleRate);
		useVibrato = false;
		ars = new ARFilter[MAXPOLY];
		for (int i = 0; i < MAXPOLY; i++) {
			ars[i] = new ARFilter(sampleRate);
		}

		deinFilter = new DeinFilter(sampleRate);
		useDeinFilter = false;
		displayBuffer = new byte[BUFSIZE];

		// um unseren direkten Draht zum Lautsprecher zu bekommen muessen wir
		// ein Format bestimmen
		AudioFormat af = new AudioFormat((float) sampleRate, 8, 1, true, false);

		try {
			// dieses Format kann jetzt aus dem Audiosystem eine direkte
			// Verbindung anfordern
			sdl = AudioSystem.getSourceDataLine(af);
			// wir koennen diesen Zugang jetzt oeffnen
			sdl.open(af, 5000);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

    // eine Kopie des buffers, der angezeigt werden kann
	private byte[] displayBuffer;

	/**
	 * startet einen Thread, der die Oszillatoren oszillieren laesst, Filter
	 * anwendet und das Ergebnis an die Soundkarte schickt
	 */
	public void start() {
		// Wenn der Synthesizer zum ersten Mal gestartet wird....
		if (!run) {
			run = true;

			// ...erzeugt er einen neuen Thread, in dem ohne Pause ein Buffer
			// beschrieben und
			// an den Lautsprecher gesendet wird
			// Dies muss in einem seperaten Thread geschehen, da das Programm
			// gleichzeitig
			// auf Input vom Nutzer horchen soll.
			Thread t = new Thread() {

				public void run() {
					// in diesen Buffer schreiben wir unsere finalen
					// Sampledaten, die dann dem Lautsprecher
					// uebergeben werden
					byte[] buf = new byte[BUFSIZE];

					// Buffer der einzelnen oszillatoren (durch eine Trennung
					// der Buffer
					// koennen wir spater einfache Filtererweiterung pro Stimme
					// erzeugen,
					// zB. wird der AR Filter jeder Stimme einzeln berechnet!)
					byte[][] osc_bufs = new byte[MAXPOLY][BUFSIZE];

					// starte die Verbindung zum Lautsprecher (ab jetzt tastet
					// der Lautsprecher seinen
					// internen Buffer ab, den wir mit unserem fuellen muessen)
					sdl.start();
					while (run) {

						// lasse die Oszillatoren den Buffer vollschreiben
						for (int i = 0; i < oscillators.length; i++) {
							// Bearbeitung nur notwendig wenn ARFilter fuer
							// diesen
							// Oszillator nicht auf quiet steht!
							if (!ars[i].isQuiet()) {
								oscillators[i].oscillateBuffer(osc_bufs[i]);

								// der AR Filter wird jedem Oszillator einzeln
								// zugeordnet
								ars[i].ARBuffer(osc_bufs[i]);
							}

						}

						// kombiniere die einzelnen gefuellten Buffer
						buf = combineBuffers(osc_bufs);

						// Filter auf den Buffer anwenden
						if (useDeinFilter)
							deinFilter.filterBuffer(buf);
						if (useVibrato)
							vibrato.filterBuffer(buf);
						

						// Buffer an Soundkarte senden, die kopiert den Inhalt
						// in ihren
						// internen buffer und laesst den Lautsprecher
						// dementsprechend seine Membran einstellen
						sdl.write(buf, 0, BUFSIZE);

						// generiere einen buffer, der im Fenster angezeigt
						// werden kann
						generateDisplayBuffer(buf);
					}

					// beende die Verbindung, wenn Synthesizer gestoppt wurde
					sdl.stop();
				}
			};

			t.start();

		}
	}

	/**
	 * Kombiniert individuelle Buffer der Oszillatoren
	 * 
	 * @param toCombine
	 * @return
	 */
	private byte[] combineBuffers(byte[][] toCombine) {
		byte[] tmp = new byte[BUFSIZE];
		for (int i = 0; i < MAXPOLY; i++) {
			// ein Buffer muss nur hinzugefuegt werden, wenn er nicht ueberall 0
			// hat
			// dies ist aber der Fall, wenn der AR von Oszillator i quiet ist
			if (!ars[i].isQuiet()) {
				for (int j = 0; j < BUFSIZE; j++) {
					tmp[j] += toCombine[i][j];
				}
			}
		}

		return tmp;
	}

	/**
	 * Kopie des Arbeitsbuffers um eine Korrekte Anzeige der Daten zu
	 * garantieren Die Kopie wird angefertigt, wenn alle Filter angewandt
	 * wurden. Wuerde man einfach den Arbeitsbuffer abfragen koennte es zur
	 * Darstellung unfertiger Daten kommen (weil die Buffer in einem seperaten
	 * Thread beschrieben werden)
	 * 
	 * @param bufToDisplay
	 */
	private void generateDisplayBuffer(byte[] bufToDisplay) {
		for (int i = 0; i < bufToDisplay.length; i++) {
			displayBuffer[i] = bufToDisplay[i];
		}
	}

	/**
	 * stoppt den Synthesizer
	 */
	public void stop() {
		run = false;
	}

	/**
	 * versucht, die uebergebene Grundton-Frequenz von einem oszillator spielen 
     * zu lassen
	 * 
	 * @param frequency
	 */
	public void startFrequency(double frequency) {
		// wenn neue Grundton-Frequenz gestartet werden soll, ueberpruefe ob
		// maximale Anzahl an Grundton-Frequenzen noch nicht erreicht wurde
		// und ob die geforderte Frequenz nicht bereits laeuft
		if (polyCount() < MAXPOLY && !playingFrequency(frequency)) {

			// schreibe Frequenz in das Grundton-Frequenz-Array, an die erste
			// Stelle die gerade nicht von einem Oszillator gespielt wird
			playingFrequencies[polyCount()] = true;
			polyFrequencies[polyCount() - 1] = frequency;

			// aktualisiere die Grundton-Frequenz, die der Oszillator
			// oszillieren soll
			oscillators[polyCount() - 1]
					.setFrequency(polyFrequencies[polyCount() - 1]);

			// starte ARFilter attack fuer den neuen Oszillator
			ars[polyCount() - 1].startAttack();

		}

	}

	/**
	 * ueberprueft, ob die gegebene Frequenz bereits gespielt wird
	 * 
	 * @param frequency
	 * @return
	 */
	private boolean playingFrequency(double frequency) {
		// ueberpruefe ob die uebergebene Grundtonfrequenz bereits im Array
		// liegt (=sie wird bereits gespielt)
		for (int i = 0; i < MAXPOLY; i++) {
			if (polyFrequencies[i] == frequency) {
				return true;
			}
		}

		return false;
	}

	private int polyCount() {
		int i = 0;
		for (int j = 0; j < MAXPOLY; j++) {
			if (playingFrequencies[j])
				i++;
		}
		return i;
	}

	/**
	 * Loescht die uebergebene Frequenz aus den spielenden Grundton-Frequenzen
	 * des Oszillators
	 * 
	 * @param frequency
	 */
	public void stopFrequency(double frequency) {
		// wenn die gegebene Frequenz momentan gespielt wird
		if (playingFrequency(frequency)) {
			int index = 0;

			// finde heraus an welcher Stelle im Frequenzarray sie sich befindet
			for (int i = 0; i < MAXPOLY; i++) {
				if (polyFrequencies[i] == frequency) {
					// und setze die Frequenz im Array auf 0 (wird vom
					// oszillator dann ignoriert)
					polyFrequencies[i] = 0;
					// merke Stelle an der Frequenz gestanden hat
					index = i;
					break;
				}

			}

			// aktualisiere Anzahl momentan gleichzeitig spielender
			// Grundton-Frequenzen
			playingFrequencies[index] = false;

			// start release of ARFilters
			ars[index].startRelease();

		}

	}

	/**
	 * Hilfsmethode um schnell einen Oszillator zu erzeugen
	 * 
	 * @param first
	 * @return
	 */
	private ComplexOscillator createOsc(boolean first) {
		ComplexOscillator c = new ComplexOscillator(sampleRate);
		c.setLoudness((byte) 30);

		if (!first) {
			c.setOverwrite(false);
		}
		return c;
	}

	// Hilfsmethoden um die Filter- und Oszillator- Parameter zu beeinflussen
	public void setVibrato(int length_milliseconds, int depth) {
		if (length_milliseconds == 0 || depth == 0) {
			useVibrato = false;
		} else {
			useVibrato = true;
			vibrato.setLength(length_milliseconds);
			vibrato.setDepth(depth);
		}

	}

	public void setLoudness(byte loudness) {
		for (int i = 0; i < oscillators.length; i++) {
			oscillators[i].setLoudness(loudness);
		}
	}

	public void setOscillator(int start, int count, int increase) {
		for (int i = 0; i < oscillators.length; i++) {
			oscillators[i].setOvertoneStart(start);
			oscillators[i].setOvertoneCount(count);
			oscillators[i].setOvertoneIncrease(increase);
		}

	}

	public void setDeinFilter(byte param1, byte param2) {
			deinFilter.setParam1(param1);
			deinFilter.setParam2(param2);
			useDeinFilter = true;
	}

	public void setAR(int attack_ms, int release_ms) {
		for (int n = 0; n < MAXPOLY; n++) {
			ars[n].setAttack(attack_ms);
			ars[n].setRelease(release_ms);
		}

	}

	public byte[] getDisplayBuffer() {
		return displayBuffer;
	}

}
