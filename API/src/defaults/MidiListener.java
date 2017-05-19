package defaults;

import javax.sound.midi.MidiMessage;

import dev.MidiIOCommunicator;

public interface MidiListener {
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp);
}
