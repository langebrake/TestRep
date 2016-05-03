package defaults;

import javax.sound.midi.MidiMessage;

public interface MidiListener {
	public void listen(MidiIO source, MidiMessage msg, long timestamp);
}
