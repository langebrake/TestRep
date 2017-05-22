package combineportsplugin_v01;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;

public class Combine extends Plugin implements MidiListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6649262714856401683L;

	public Combine(PluginHostCommunicator host) {
		super(host, "Converge Ports", 1, -1, 1, 1);
	}

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new Combine(host);
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		this.getPluginHost().getOutput(0).send(msg, timestamp);

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
		this.reload();

	}

	@Override
	public void load() {
		this.reload();

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
		Combine tmp = new Combine(newHost);
		tmp.reload();
		return tmp;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		reload();
	}

	private void reload() {
		for (MidiIOCommunicator m : this.getPluginHost().getInputs()) {
			m.removeMidiListener(this);
			m.addMidiListener(this);
		}
	}

}
