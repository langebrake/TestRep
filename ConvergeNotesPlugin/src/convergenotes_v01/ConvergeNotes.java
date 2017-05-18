package convergenotes_v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import defaults.MidiIOCommunicator;
import defaults.MidiListener;
import engine.MidiUtilities;
import plugin.Plugin;
import pluginhost.PluginHostCommunicator;
import pluginhost.events.HostEvent;

public class ConvergeNotes extends Plugin implements MidiListener, ActionListener {
	private transient MinView view;
	public byte note;

	public ConvergeNotes(PluginHostCommunicator host) {
		super(host, "Converge Notes", 1, 1, 1, 1);
	}

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new ConvergeNotes(host);
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
		note = 60;
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
		ConvergeNotes tmp = new ConvergeNotes(newHost);
		tmp.initPlugin();
		return tmp;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	private void initPlugin() {
		getPluginHost().getInput(0).addMidiListener(this);
		this.view = new MinView(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.note = view.getNote();

	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		if (MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_OFF
				|| MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_ON) {
			try {
				getPluginHost().getOutput(0).send(new ShortMessage(msg.getMessage()[0], note, msg.getMessage()[2]),
						timestamp);
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			getPluginHost().getOutput(0).send(msg, timestamp);
		}

	}

}
