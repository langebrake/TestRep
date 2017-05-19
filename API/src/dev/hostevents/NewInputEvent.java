package dev.hostevents;

import dev.MidiIOCommunicator;

public class NewInputEvent extends HostEvent {
	private MidiIOCommunicator newInput;

	public NewInputEvent(MidiIOCommunicator newInput) {
		this.newInput = newInput;
	}

	public MidiIOCommunicator getNewInput() {
		return this.newInput;
	}
}
