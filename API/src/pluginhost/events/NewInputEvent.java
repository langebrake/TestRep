package pluginhost.events;

import defaults.MidiIO;

public class NewInputEvent extends HostEvent {
	private MidiIO newInput;
	
	public NewInputEvent(MidiIO newInput){
		this.newInput = newInput;
	}
	public MidiIO getNewInput(){
		return this.newInput;
	}
}
