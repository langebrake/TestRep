package ignorecc_v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;
import engine.MidiUtilities;

public class IgnoreCC extends Plugin implements MidiListener, ActionListener {
	private transient MinView view;
	byte ignore;

	public IgnoreCC(PluginHostCommunicator host) {
		super(host, "IgnoreCC", 1, 1, 1, 1);
	}

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new IgnoreCC(host);
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		if (MidiUtilities.getStatus(msg) == MidiUtilities.CONTROL_CHANGE && MidiUtilities.getData1(msg) == ignore) {

		} else {
			getPluginHost().getOutput(0).send(msg, timestamp);
		}
	}

	@Override
	public JComponent getMinimizedView() {
		return view;
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
		ignore = -1;
		initPlugin();
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
		IgnoreCC c = new IgnoreCC(newHost);
		c.ignore = ignore;
		c.initPlugin();
		return c;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ignore = view.getIgnore();

	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		initPlugin();
	}

	private void initPlugin() {
		getPluginHost().getInput(0).addMidiListener(this);
		view = new MinView(this);
	}
}
