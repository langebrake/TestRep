package dev.hostevents;

import dev.MidiIOCommunicator;

public class NewOutputEvent extends HostEvent {
	private MidiIOCommunicator newOutput;

	public NewOutputEvent(MidiIOCommunicator newInput) {
		this.newOutput = newInput;
	}

	public MidiIOCommunicator getNewOutput() {
		return this.newOutput;
	}
}
