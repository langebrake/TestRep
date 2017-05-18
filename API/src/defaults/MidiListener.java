package defaults;

import javax.sound.midi.MidiMessage;

public interface MidiListener {
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp);
}
