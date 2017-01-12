package pluginhost.events;

import defaults.MidiIOThrough;

public class NewInputEvent extends HostEvent {
	private MidiIOThrough newInput;

	public NewInputEvent(MidiIOThrough newInput) {
		this.newInput = newInput;
	}

	public MidiIOThrough getNewInput() {
		return this.newInput;
	}
}
