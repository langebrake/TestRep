package forcechannel_v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;
import engine.MidiUtilities;

public class ForceChannel extends Plugin implements ActionListener, MidiListener {
	private transient MinView view;
	int selectedChannel;

	public ForceChannel(PluginHostCommunicator host) {
		super(host, "Force Channel", 1, 1, 1, 1);
	}

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new ForceChannel(host);
	}

	@Override
	public JComponent getMinimizedView() {
		return this.view;
	}

	@Override
	public JComponent getFullView() {
		return null;
	}

	@Override
	public void notify(HostEvent e) {

	}

	private void initPlugin() {
		this.view = new MinView(this);
		getPluginHost().getInput(0).addMidiListener(this);
	}

	@Override
	public void load() {
		this.selectedChannel = -1;
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
		ForceChannel f = new ForceChannel(newHost);
		f.selectedChannel = selectedChannel;
		f.initPlugin();
		return f;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		if (MidiUtilities.getStatus(msg) != 0b1111 && selectedChannel != -1) {
			int data1 = msg.getMessage()[1];
			int status = msg.getMessage()[0] & 0xF0;
			int data2 = 0;
			if (msg.getLength() > 2) {
				data2 = msg.getMessage()[2];
			}
			msg = new ShortMessage();
			try {

				((ShortMessage) msg).setMessage(status, selectedChannel, data1, data2);
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getPluginHost().getOutput(0).send(msg, timestamp);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof JComboBox) {
			if (e.getActionCommand().equals("CHANNEL")) {
				this.selectedChannel = (((JComboBox) o).getSelectedIndex()) - 1;

			}
		}

	}
}
