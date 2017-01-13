import java.io.Serializable;

/**
 * Reverb-Filter fuer digitalen Hall (work in progress!!!!!!)
 * Nur eine Idee.
 * @author Hendrik Langebrake
 *
 */
public class ReverbFilter implements Serializable {

	public ReverbFilter(int sample_rate, int bufferSize) {
		rev = new int[bufferSize];
	}

	// speichert bekannte Samples
	int[] rev;
	// Zeiger auf Anfang des "zueklischen" Arrays rev
	int rev_pos;

	/**
	 * addiere altes Signal mit decay auf den buffer
	 * 
	 * @param buf
	 */
	public void reverbBuffer(byte[] buf) {
		int tmp;
		double decay = 0.9d;
		for (int i = 0; i < buf.length; i++) {
			tmp = (int) ((buf[i] + rev[rev_pos] * decay));
			buf[i] = (byte) (rev[rev_pos] / 2);
			rev[rev_pos] = tmp;

			rev_pos = (rev_pos + 1) % rev.length;
		}

	}
}
