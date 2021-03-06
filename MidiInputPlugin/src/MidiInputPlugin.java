import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import defaults.DefaultView;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;

public class MidiInputPlugin extends Plugin implements Serializable, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8386550048559209829L;
	private static final int MAXINPUTS = 0;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "MidiInputPlugin";
	private String midiDeviceName;
	private transient LinkedList<MidiDevice> devices;
	private transient MidiIOCommunicator output;
	private transient static ConcurrentHashMap<String, InputReceiver> inputMap;

	static {
		inputMap = new ConcurrentHashMap<String, InputReceiver>();
	}

	public static MidiInputPlugin getInstance(PluginHostCommunicator host) {
		return new MidiInputPlugin(host);
	}

	public MidiInputPlugin(PluginHostCommunicator host) {
		super(host, NAME, MININPUTS, MAXINPUTS, MINOUTPUTS, MAXOUTPUTS);
	}

	@Override
	public JComponent getMinimizedView() {
		return new MiniView(this.getPluginHost().getEngine().getInputDevices(), this, this.midiDeviceName);
	}

	@Override
	public JComponent getFullView() {
		return new DefaultView("MIDIINPUT");
	}

	@Override
	public void notify(HostEvent e) {

	}

	@Override
	public void load() {
		this.output = this.getPluginHost().getOutput(0);
		this.devices = this.getPluginHost().getEngine().getInputDevices();

	}

	private void setInputID(int id) throws MidiUnavailableException {
		if (this.midiDeviceName != null && inputMap.containsKey(midiDeviceName)) {
			inputMap.get(midiDeviceName).unregister(output);
		}

		this.midiDeviceName = devices.get(id).getDeviceInfo().getName();

		if (!inputMap.containsKey(midiDeviceName)) {
			InputReceiver firstReceiver = new InputReceiver(devices.get(id));
			inputMap.put(midiDeviceName, firstReceiver);
			inputMap.get(midiDeviceName).register(output);
		} else {
			inputMap.get(midiDeviceName).register(this.output);
		}

	}

	@Override
	public boolean close() {
		if (midiDeviceName != null && inputMap.containsKey(midiDeviceName)) {
			inputMap.get(midiDeviceName).unregister(output);
		}
		return true;

	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	private void initPlugin() {
		this.devices = this.getPluginHost().getEngine().getInputDevices();
		this.output = this.getPluginHost().getOutput(0);
		if (this.midiDeviceName != null && inputMap.containsKey(this.midiDeviceName)) {
			try {
				inputMap.get(midiDeviceName).register(output);
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean reOpen() {
		System.out.println("REOPENED");
		try {
			if (this.midiDeviceName != null && inputMap.get(this.midiDeviceName) != null) {
				inputMap.get(this.midiDeviceName).register(output);
			}
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}

	private static class InputReceiver implements Receiver, Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1281819210326570843L;
		private LinkedList<MidiIOCommunicator> outputs;
		private MidiDevice inputDevice;
		private Transmitter inputTransmitter;

		public InputReceiver(MidiDevice m) throws MidiUnavailableException {
			this.outputs = new LinkedList<MidiIOCommunicator>();
			this.inputDevice = m;
		}

		@Override
		public void close() {
			this.inputDevice.close();
			this.inputTransmitter.close();
		}

		public void open() throws MidiUnavailableException {
			if (!inputDevice.isOpen()) {
				this.inputDevice.open();
				this.inputTransmitter = this.inputDevice.getTransmitter();
				this.inputTransmitter.setReceiver(this);
			}
		}

		@Override
		public void send(MidiMessage arg0, long arg1) {
			for (MidiIOCommunicator m : outputs) {
				m.send(arg0, arg1);
			}
		}

		public void register(MidiIOCommunicator m) throws MidiUnavailableException {
			if (this.outputCount() == 0) {
				this.open();
			}
			if (!outputs.contains(m)) {
				outputs.add(m);
			}
		}

		public void unregister(MidiIOCommunicator m) {
			this.outputs.remove(m);
			if (this.outputCount() <= 0) {
				this.close();
			}
		}

		public int outputCount() {
			return outputs.size();
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof JComboBox) {
			try {
				this.setInputID(((JComboBox) o).getSelectedIndex());
			} catch (MidiUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@Override
	public Plugin clone(PluginHostCommunicator host) {
		MidiInputPlugin mip = new MidiInputPlugin(host);
		mip.midiDeviceName = this.midiDeviceName;
		mip.initPlugin();
		return mip;
	}

}
