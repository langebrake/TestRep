package defaults;

import javax.sound.midi.MidiMessage;

public interface MidiIOCommunicator {

	void send(MidiMessage msg, long timestamp);

	void addMidiListener(MidiListener listener);

	boolean hasOutput();
	
	boolean hasInput();
	
	int getId();

	void removeMidiListener(MidiListener listener);

}
