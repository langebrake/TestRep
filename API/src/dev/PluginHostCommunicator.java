package dev;

import java.util.LinkedList;

import dev.pluginevents.PluginEvent;
import midiengine.MidiEngine;

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
