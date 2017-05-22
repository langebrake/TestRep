import java.io.Serializable;

/**
 * Oszillator, der ein uebergebenes Array mit Sample-Daten gemaess einer
 * Frequenz fuellt
 * 
 * @author Hendrik Langebrake
 *
 */

public class ComplexOscillator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5474566263101978965L;
	// wir oszillieren verschiedene sinus funktionen, die sollen alle
	// ueber der gleichen phase laufen (sin(phase*....))
	private double phase;
	// die Phase wird fuer jeden Buffereintrag um phase_delta erhoeht
	private double phase_delta;
	// die Abtastrate pro Sekunde, die der Lautsprecher den Buffer abtastet
	private int sample_rate;
	// ein Skalar um die Lautstaerke einzustellen
	private byte loudness;
	// Grundtonfrequenz, die oszilliert werden sollen
	// (eventuell wird eine obertonreihe mitoszilliert)
	private double frequency;
	// gibt an, ob der uebergebene Buffer initial geloescht werden soll
	private boolean overwrite;

	// verwaltet, welche Obertoene zur Grundfrequenz addiert werden sollen
	private int overtoneStart;
	private int overtoneCount;
	private int overtoneIncrease;

	public ComplexOscillator(int sample_rate) {
		// diesem Konstrukter muss nur die Samplerate uebergeben werden
		// er ruft dann intern den zweiten Konstruktor mit default-werten auf
		this(0, sample_rate, true, (byte) 128);
	}

	public ComplexOscillator(double frequency, int sample_rate,
			boolean overwrite, byte loudness) {
		setFrequency(frequency);
		setSampleRate(sample_rate);
		resetPhase();
		setOverwrite(overwrite);
		setLoudness(loudness);

	}

	/**
	 * nimmt einen buffer und schreibt Lautsprecherauslenkungen fuer die
	 * Grundfrequenz und eventuelle Obertoene hinein
	 * 
	 * @param buffer
	 */
	public void oscillateBuffer(byte[] buffer) {

		// Der Sample-Buffer muss komplett gefuellt werden
		for (int i = 0; i < buffer.length; ++i) {

			// eventuell Buffer loeschen, wenn so gefordert ist oder wenn
			// Frequenz null
			if (overwrite || frequency == 0) {
				buffer[i] = 0;
			}

			// Wenn Grundtonfrequenz nicht null ist (andernfalls muessten wir
			// nichts tun)
			if (frequency != 0) {

				// Grundtonfrequenz erzeugen mit Sinus
				double sinAddition = (Math.sin(phase * frequency));

				// Obertoene unseres Synthesizers fuer diesen Ton erzeugen
				for (int j = overtoneStart + 1; j <= overtoneCount
						* overtoneIncrease + overtoneStart; j += overtoneIncrease) {
					// auf bisherige frequenzen aufaddiert wird:
					// sinus der aktuellen phase mal grundfrequenz mal einer
					// ganzen Zahl,
					// die die Nummer des Obertones wiederspiegelt
					// Diese j-te Obertonfrequenz wird mit 1/j als Skalar
					// multipliziert.
					// Andere Skalare sind moeglich, echte Instrumente haben
					// tatsaechlich charakteristische
					// Skalar-Verteilungen der Obertoene
					sinAddition += Math.sin(j * phase * frequency) * 1
							/ (double) (j);
				}

				// Ein Versuch einen Overflow des Oszillators an sich zu
				// vermeiden:
				// wenn der Wert zu gross fuer byte wird wird er einfach
				// "geclipped"
				if (buffer[i] + sinAddition * loudness > Byte.MAX_VALUE)
					buffer[i] = Byte.MAX_VALUE;
				else if (buffer[i] + sinAddition * loudness < Byte.MIN_VALUE)
					buffer[i] = Byte.MIN_VALUE;
				else
					buffer[i] += (byte) (sinAddition * loudness);
			}

			// die Phase, nach der Sinus berechnet werden soll wird fortgesetzt,
			// da an dieser
			// Stelle alle Frequenzen fuer das erste Sample im Buffer berechnet
			// wurden
			phase += phase_delta;

			// Dank der mathematisch bekannten Eigenschaften von Sinus koennen
			// wir
			// eine Phasenverschiebung vornehmen die verhindert,
			// dass die Phase zu gross und somit Berechnungen zu lange und
			// ungenau werden
			if (phase * frequency > 2 * Math.PI && frequency != 0) {
				phase -= (double) 2 * Math.PI / frequency;
			}
		}
	}

	// nur ein paar get/set methoden fuer die Parameter (werden nicht alle
	// benoetigt)
	public void setFrequency(double double1) {
		this.frequency = double1;
	}

	public void setLoudness(byte loudness) {
		this.loudness = loudness;
	}

	public void setSampleRate(int sample_rate) {
		this.sample_rate = sample_rate;
		this.phase_delta = Math.PI * 2.0 / sample_rate;
	}

	public void resetPhase() {
		phase = 0;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public double getFrequency() {
		return frequency;
	}

	public boolean getOverwrite() {
		return overwrite;
	}

	public byte getLoudness() {
		return loudness;
	}

	public int getSampleRate() {
		return sample_rate;
	}

	public void setPhase(double phase) {
		this.phase = phase;

	}

	public double getPhase() {
		return phase;
	}

	public void setOvertoneStart(int start) {
		overtoneStart = start;

	}

	public void setOvertoneCount(int count) {
		overtoneCount = count;

	}

	public void setOvertoneIncrease(int increase) {
		overtoneIncrease = increase;

	}

}
