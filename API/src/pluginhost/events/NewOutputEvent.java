package pluginhost.events;

import defaults.MidiIOThrough;

public class NewOutputEvent extends HostEvent {
	private MidiIOThrough newOutput;

	public NewOutputEvent(MidiIOThrough newInput) {
		this.newOutput = newInput;
	}

	public MidiIOThrough getNewOutput() {
		return this.newOutput;
	}
}
