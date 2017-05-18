package defaults;

import javax.sound.midi.MidiMessage;

import pluginhost.PluginHost;

public interface MidiIO extends MidiIOCommunicator{

	public void send(MidiMessage message, long timeStamp);

	public void setOutput(MidiIO output);

	public void setInput(MidiIO input);

	public MidiIO getOutput();

	public MidiIO getInput();

	public void disconnectInput();

	public void disconnectOutput();

	public boolean hasOutput();

	public boolean hasInput();

	public PluginHost getParent();

	public int getId();

	public void addMidiListener(MidiListener listener);

	public void removeMidiListener(MidiListener listener);

	public void setPluginHost(PluginHost host);
}
