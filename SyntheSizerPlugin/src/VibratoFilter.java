public class VibratoFilter {

	// Vibrato ueber Sinus benotigt wie ein Oszillator eine Phase
	private double phase;
	// und ein delta, mit dem die Phase je Sample erhoeht werden soll
	private double phase_delta;
	private int sample_rate;
	// wie lange soll die Periode des Vibratos sein
	private float length_factor;
	// wie stark soll der Vibrato sein (1 = schwach, >1 = stark)
	private float depth;

	public VibratoFilter(int sample_rate) {
		this.sample_rate = sample_rate;
		this.phase_delta = Math.PI * 2.0 / this.sample_rate;
		phase = 0;
	}

	/**
	 * fuegt einem Buffer Vibrato hinzu
	 * 
	 * @param buf
	 */
	public void filterBuffer(byte[] buf) {
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) (buf[i] * ((Math.sin(phase * length_factor)
					+ (float) depth - 1) / (float) depth));
			phase += phase_delta;
		}
	}

	public void setLength(int milliseconds) {
		length_factor = 1000 / milliseconds;
	}

	public void setDepth(int factor) {
		depth = factor;

	}
}
