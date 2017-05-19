package dev;

import javax.sound.midi.MidiMessage;

import defaults.MidiListener;

public interface MidiIOCommunicator {

	void addMidiListener(MidiListener listener);

	void removeMidiListener(MidiListener listener);
	
	void send(MidiMessage msg, long timestamp);
	
	boolean hasOutput();
	
	boolean hasInput();
	
	int getId();

}
