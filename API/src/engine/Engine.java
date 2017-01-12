package engine;

import java.io.Serializable;
import java.util.LinkedList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import midiengine.MidiEngine;

public class Engine implements MidiEngine, Serializable {
	private LinkedList<MidiDevice> inputDevices;
	private LinkedList<MidiDevice> outputDevices;
	private static Engine engine;

	public static Engine load() throws MidiUnavailableException {
		if (engine == null) {
			return new Engine();
		} else {
			return engine;
		}
	}

	private Engine() throws MidiUnavailableException {
		inputDevices = SystemMidiDevices.getInputDevices();
		outputDevices = SystemMidiDevices.getOutputDevices();
	}

	public void refreshMidiDevices() throws MidiUnavailableException {
		inputDevices = SystemMidiDevices.getInputDevices();
		outputDevices = SystemMidiDevices.getOutputDevices();
	}

	public LinkedList<MidiDevice> getInputDevices() {
		return inputDevices;
	}

	public LinkedList<MidiDevice> getOutputDevices() {
		return outputDevices;
	}

	public MidiDevice getInputDevice(String name) {
		for (MidiDevice m : inputDevices) {
			if (m.getDeviceInfo().getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	public MidiDevice getOutputDevice(String name) {
		for (MidiDevice m : outputDevices) {
			if (m.getDeviceInfo().getName().equals(name)) {
				return m;
			}
		}

		return null;
	}

	public MidiDevice getInputDevice(int indexID) {
		return inputDevices.get(indexID);
	}

	public MidiDevice getOutputDevice(int indexID) {
		return outputDevices.get(indexID);
	}

}
