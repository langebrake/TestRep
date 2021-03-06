package midioutput_v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import defaults.DefaultView;
import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;
import engine.MidiUtilities;

public class MidiOutputPlugin extends Plugin implements ActionListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8429357505259810533L;
	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 0;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "MidiOutputPlugin";
	private transient static ConcurrentHashMap<String, OutputTransmitter> outputMap;
	private String midiDeviceName;
	private transient MidiIOCommunicator input;
	protected transient LinkedList<MidiDevice> devices;
	private int selectedChannel;

	static {
		outputMap = new ConcurrentHashMap<String, OutputTransmitter>();
	}

	public static MidiOutputPlugin getInstance(PluginHostCommunicator host) {
		return new MidiOutputPlugin(host);
	}

	public MidiOutputPlugin(PluginHostCommunicator host) {
		super(host, NAME, MININPUTS, MAXINPUTS, MINOUTPUTS, MAXOUTPUTS);
	}

	@Override
	public JComponent getMinimizedView() {
		return new MiniView(this.getPluginHost().getEngine().getOutputDevices(), this, this.midiDeviceName,
				this.selectedChannel);
	}

	@Override
	public JComponent getFullView() {
		return new DefaultView("MIDIOUTPUT");
	}

	@Override
	public void notify(HostEvent e) {

	}

	@Override
	public void load() {
		this.input = this.getPluginHost().getInput(0);
		this.devices = this.getPluginHost().getEngine().getOutputDevices();
		this.selectedChannel = -1;
	}

	private void setOutputID(int id) throws MidiUnavailableException {
		if (this.midiDeviceName != null && outputMap.containsKey(midiDeviceName)) {
			outputMap.get(midiDeviceName).unregister(input);
		}

		this.midiDeviceName = devices.get(id).getDeviceInfo().getName();

		if (!outputMap.containsKey(midiDeviceName)) {
			OutputTransmitter lastTransmitter = new OutputTransmitter(devices.get(id));
			outputMap.put(midiDeviceName, lastTransmitter);
			outputMap.get(midiDeviceName).register(input);
		} else {
			outputMap.get(midiDeviceName).register(this.input);
		}

	}

	@Override
	public boolean close() {
		if (midiDeviceName != null && outputMap.containsKey(midiDeviceName)) {
			outputMap.get(midiDeviceName).unregister(input);
		}
		return true;
	}

	@Override
	public boolean reOpen() {
		try {
			if (this.midiDeviceName != null && outputMap.get(this.midiDeviceName) != null) {
				outputMap.get(this.midiDeviceName).register(input);
			}
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	private void initPlugin() {
		this.devices = this.getPluginHost().getEngine().getOutputDevices();
		this.input = this.getPluginHost().getInput(0);
		if (this.midiDeviceName != null && outputMap.containsKey(this.midiDeviceName)) {
			try {
				outputMap.get(midiDeviceName).register(input);
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class OutputTransmitter implements MidiListener {
		private LinkedList<MidiIOCommunicator> inputs;
		private MidiDevice outputDevice;
		private Receiver outputReceiver;

		public OutputTransmitter(MidiDevice m) {
			this.inputs = new LinkedList<MidiIOCommunicator>();
			this.outputDevice = m;
		}

		public void close() {
			this.outputDevice.close();
			if (outputReceiver != null) {
				this.outputReceiver.close();
			}
			this.outputReceiver = null;
		}

		public void open() throws MidiUnavailableException {
			if (!outputDevice.isOpen()) {
				this.outputDevice.open();
				this.outputReceiver = this.outputDevice.getReceiver();
			}
		}

		public int inputCount() {
			return this.inputs.size();
		}

		public void register(MidiIOCommunicator m) throws MidiUnavailableException {
			if (this.inputCount() <= 0) {
				this.open();
			}
			if (!this.inputs.contains(m)) {
				m.addMidiListener(this);
				this.inputs.add(m);
			}
		}

		public void unregister(MidiIOCommunicator m) {
			this.inputs.remove(m);
			m.removeMidiListener(this);
			if (this.inputCount() <= 0) {
				this.close();
			}
		}

		@Override
		public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
			if (outputReceiver != null) {
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
				outputReceiver.send(msg, timestamp);
			}
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object o = arg0.getSource();
		if (o instanceof JComboBox) {
			if (arg0.getActionCommand().equals("DEVICE")) {
				try {
					this.setOutputID(((JComboBox) o).getSelectedIndex());
				} catch (MidiUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (arg0.getActionCommand().equals("CHANNEL")) {
				this.selectedChannel = (((JComboBox) o).getSelectedIndex()) - 1;

			}
		}

	}

	@Override
	public Plugin clone(PluginHostCommunicator host) {
		MidiOutputPlugin mop = new MidiOutputPlugin(host);
		mop.midiDeviceName = this.midiDeviceName;
		mop.initPlugin();
		return mop;
	}
}
