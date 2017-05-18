package pluginhost;

import java.util.LinkedList;

import midiengine.MidiEngine;
import plugin.events.NewOutputRequestEvent;
import plugin.events.PluginEvent;
import defaults.MidiIO;
import defaults.MidiIOCommunicator;

public interface PluginHostCommunicator {
	
	public MidiIOCommunicator getInput(int i);
	
	public LinkedList<MidiIOCommunicator> getInputs();
	
	public MidiIOCommunicator getOutput(int i);
	
	public LinkedList<MidiIOCommunicator> getOutputs();

	public int getInputCount();

	public int getOutputCount();

	public void notify(PluginEvent e);

	public MidiEngine getEngine();

}
