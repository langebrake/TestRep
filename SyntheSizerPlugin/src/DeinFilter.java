import java.io.Serializable;

/**
 * Simpler Noise-Filter, der einen Buffer mit Noise addiert
 * 
 * @author Hendrik Langebrake
 */

public class DeinFilter implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7409818717669285969L;
	// Diese Parameter kommen im Wertebereich 0 bis 100 und koennen von dir
	// genutzt werden
	private byte param1;
	private byte param2;

	private int sample_rate;
	// eine Phase, falls du sie brauchst
	@SuppressWarnings("unused")
	private double phase;
	// und delta, mit dem die Phase je Sample erhoeht wird
	private double phase_delta;

	public DeinFilter(int sampleRate) {
		this.sample_rate = sampleRate;
		this.phase = 0;
		this.phase_delta = Math.PI * 2 / sample_rate;
	}

	/**
	 * filter den Buffer mit deinem Algorithmus/deiner Funktion
	 * 
	 * @param buf
	 */
	public void filterBuffer(byte[] buf) {

		// TODO: hier koennte dein Algorithmus/deine Funktion stehen!
		// viel Spass beim Ausprobieren!
		// Ideen: Reverb, Maximizer, Limiter, Low/HighPass Filter

		for (int i = 0; i < buf.length; i += (Math.random() * param2) + 1) {
			// momentan tut diese Schleife nichts anders, als akustisches
			// Rauschen zu erzeugen
			buf[i] += (float) buf[i] / 100 * (1 - 2 * Math.random()) * param1;

			phase += phase_delta;
		}
	}

	public void setParam1(byte param1) {
		this.param1 = param1;
	}

	public void setParam2(byte param2) {
		this.param2 = param2;
	}

}
