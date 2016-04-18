package midiengine;

import java.util.LinkedList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

public interface MidiEngine {
	
	public void refreshMidiDevices() throws MidiUnavailableException;
	
	public LinkedList<MidiDevice> getInputDevices();
	
	public LinkedList<MidiDevice> getOutputDevices();
	
	public MidiDevice getInputDevice(int indexID);
	
	public MidiDevice getOutputDevice(int indexID);
	
}
