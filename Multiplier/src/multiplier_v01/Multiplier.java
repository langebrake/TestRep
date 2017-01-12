package multiplier_v01;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiIO;
import defaults.MidiListener;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class Multiplier extends Plugin implements MidiListener {

	public static Plugin getInstance(PluginHost host) {
		return new Multiplier(host);
	}

	public Multiplier(PluginHost host) {
		super(host, "Multiplier", 1, 1, 1, -1);
	}

	@Override
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		for (MidiIO m : this.getPluginHost().getOutputs()) {
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
	public Plugin clone(PluginHost newHost) {
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
