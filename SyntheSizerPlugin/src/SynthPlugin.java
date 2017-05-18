import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;
import javax.swing.JPanel;

import defaults.MidiIOCommunicator;
import defaults.MidiListener;
import engine.MidiUtilities;
import plugin.Plugin;
import pluginhost.PluginHostCommunicator;
import pluginhost.events.HostEvent;

public class SynthPlugin extends Plugin implements MidiListener {
	transient JPanel contentPane;
	transient SynthPanel synthPanel;

	public static SynthPlugin getInstance(PluginHostCommunicator host) {
		return new SynthPlugin(host);
	}

	public SynthPlugin(PluginHostCommunicator host) {
		super(host, "Additive Synthesizer", 1, 1, 0, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent getFullView() {
		// TODO Auto-generated method stub
		return contentPane;
	}

	@Override
	public void notify(HostEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		initPlugin();

	}

	@Override
	public boolean close() {
		System.out.println("CLOSED");
		return false;
	}

	@Override
	public boolean reOpen() {
		System.out.println("REOPEN");
		return false;
	}

	@Override
	public Plugin clone(PluginHostCommunicator newHost) {
		SynthPlugin p2 = new SynthPlugin(newHost);
		p2.initPlugin();
		return p2;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		if (MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_ON) {
			if (MidiUtilities.getData2(msg) == 0) {
				synthPanel.synth.stopFrequency((float) ((float) ((MidiUtilities.getData1(msg) - 21) % 12 + 1) / 12
						* ((MidiUtilities.getData1(msg) - 21) + 1) / 12) * 27.5);
			} else {
				synthPanel.synth.startFrequency((float) ((float) ((MidiUtilities.getData1(msg) - 21) % 12 + 1) / 12
						* ((MidiUtilities.getData1(msg) - 21) + 1) / 12) * 27.5);
			}

		} else if (MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_OFF) {

		}
	}

	private void initPlugin() {
		this.getPluginHost().getInput(0).addMidiListener(this);
		synthPanel = new SynthPanel();
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		// Synthesizer-Waveform-Darstellung wird auf Panel gefuegt
		contentPane.add(synthPanel, BorderLayout.CENTER);
		// Synthesizer-Control-Panel wird auf Panel gefuegt
		contentPane.add(((SynthPanel) synthPanel).createControlGui(), BorderLayout.SOUTH);

		// Synthesizer wird aktualisiert, sodass korrekte Parameter eingestellt
		// sind
		((SynthPanel) synthPanel).updateSynthSettings();
	}

}
