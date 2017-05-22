package multiplier_v01;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;

public class Multiplier extends Plugin implements MidiListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6913180239963039230L;

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new Multiplier(host);
	}

	public Multiplier(PluginHostCommunicator host) {
		super(host, "Multiplier", 1, 1, 1, -1);
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		for (MidiIOCommunicator m : this.getPluginHost().getOutputs()) {
			m.send(msg, timestamp);
		}
	}

	@Override
	public JComponent getMinimizedView() {
		return null;
	}

	@Override
	public JComponent getFullView() {
		return null;
	}

	@Override
	public void notify(HostEvent e) {
	}

	@Override
	public void load() {
		this.initPlugin();
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean reOpen() {
		return true;
	}

	@Override
	public Plugin clone(PluginHostCommunicator newHost) {
		Multiplier tmp = new Multiplier(newHost);
		tmp.load();
		return tmp;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	private void initPlugin() {
		this.getPluginHost().getInput(0).addMidiListener(this);
	}
}
