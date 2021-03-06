package transposenote_v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;
import engine.MidiUtilities;

public class TransposeNote extends Plugin implements MidiListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -699402223542942581L;
	private transient MinView minView;
	byte transpose;

	public TransposeNote(PluginHostCommunicator host) {
		super(host, "Transpose", 1, 1, 1, 1);
	}

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new TransposeNote(host);
	}

	@Override
	public JComponent getMinimizedView() {
		return this.minView;
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
		this.transpose = 0;
		this.initPlugin();

	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
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
		TransposeNote tmp = new TransposeNote(newHost);
		tmp.transpose = transpose;
		tmp.initPlugin();
		return tmp;
	}

	private void initPlugin() {
		this.getPluginHost().getInput(0).addMidiListener(this);
		this.minView = new MinView(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.transpose = this.minView.getTranspose();

	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		if (MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_ON
				|| MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_OFF) {
			if (MidiUtilities.getData1(msg) + transpose >= 0 && MidiUtilities.getData1(msg) + transpose <= 127) {
				try {
					getPluginHost()
							.getOutput(0).send(
									new ShortMessage(MidiUtilities.getStatus(msg) << 4, MidiUtilities.getChannel(msg),
											MidiUtilities.getData1(msg) + transpose, MidiUtilities.getData2(msg)),
									timestamp);
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
			} else {
				getPluginHost().getOutput(0).send(msg, timestamp);
			}
		} else {
			getPluginHost().getOutput(0).send(msg, timestamp);
		}
	}
}