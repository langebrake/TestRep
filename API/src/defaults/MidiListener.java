package defaults;

import javax.sound.midi.MidiMessage;

public interface MidiListener {
	public void listen(MidiMessage msg, long timestamp);
}
