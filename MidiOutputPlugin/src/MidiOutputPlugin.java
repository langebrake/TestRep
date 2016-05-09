import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;

import midiengine.MidiEngine;
import defaults.DefaultView;
import defaults.MidiIO;
import defaults.MidiIOThrough;
import defaults.MidiListener;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class MidiOutputPlugin extends Plugin implements ActionListener, Serializable {

	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 0;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "MidiOutputPlugin";
	private transient static ConcurrentHashMap<String,OutputTransmitter> outputMap;
	private String midiDeviceName;
	private transient MidiIO input;
	protected transient LinkedList<MidiDevice> devices;
	
	static{
		outputMap = new ConcurrentHashMap<String,OutputTransmitter>();
	}
	
	public static MidiOutputPlugin getInstance(PluginHost host){
		return new MidiOutputPlugin(host);
	}
	
	public MidiOutputPlugin(PluginHost host) {
		super(host);
	}

	@Override
	public JComponent getMinimizedView() {
		return new MiniView(this.getPluginHost().getEngine().getOutputDevices(),this,this.midiDeviceName);
	}

	@Override
	public JFrame getFullView() {
		JFrame frame = new JFrame("MIDIOUTPUT");
		
		frame.add(new DefaultView("MIDIOUTPUT"));
		frame.pack();
		return frame;
	}

	
	public  String getPluginName() {
		return NAME;
	}

	@Override
	public int getMaxInputs() {
		return MAXINPUTS;
	}

	@Override
	public int getMaxOutputs() {
		return MAXOUTPUTS;
	}

	@Override
	public int getMinInputs() {
		return MININPUTS;
	}

	@Override
	public int getMinOutputs() {
		return MINOUTPUTS;
	}

	@Override
	public void notify(HostEvent e) {
		
	}

	@Override
	public void load() {
		this.input = this.getPluginHost().getInput(0);
		this.devices = this.getPluginHost().getEngine().getOutputDevices();
		
	}

	private void setOutputID(int id) throws MidiUnavailableException{
		if(this.midiDeviceName!=null && outputMap.containsKey(midiDeviceName)){
			outputMap.get(midiDeviceName).unregister(input);
		}
		
		this.midiDeviceName = devices.get(id).getDeviceInfo().getName();
		
		if(!outputMap.containsKey(midiDeviceName)){
			OutputTransmitter lastTransmitter = new OutputTransmitter(devices.get(id));
			System.out.println(lastTransmitter.outputDevice.getDeviceInfo());
			outputMap.put(midiDeviceName, lastTransmitter);
			outputMap.get(midiDeviceName).register(input);
		}else {
			outputMap.get(midiDeviceName).register(this.input);
		}
		
	}
	
	@Override
	public boolean close() {
		if(midiDeviceName!=null && outputMap.containsKey(midiDeviceName)){
			outputMap.get(midiDeviceName).unregister(input);
		}
		return true;
	}
	
	@Override
	public boolean reOpen(){
		try {
			if(this.midiDeviceName != null && outputMap.get(this.midiDeviceName) != null){
				outputMap.get(this.midiDeviceName).register(input);
			}
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public String getDisplayName() {
		return NAME;
	}

	@Override
	public void setDisplayName() {
		// TODO Auto-generated method stub
		
	}

	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.setPluginHost(Plugin.waitForHost());
		this.devices = this.getPluginHost().getEngine().getOutputDevices();
		this.input = this.getPluginHost().getInput(0);
		if(this.midiDeviceName != null && outputMap.containsKey(this.midiDeviceName)){
			try {
				outputMap.get(midiDeviceName).register(input);
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class OutputTransmitter implements MidiListener {
		private LinkedList<MidiIO> inputs;
		private MidiDevice outputDevice;
		private Receiver outputReceiver;
		
		public OutputTransmitter(MidiDevice m){
			this.inputs = new LinkedList<MidiIO>();
			this.outputDevice = m;
		}
		
		
		public void close() {
			this.outputDevice.close();
			if(outputReceiver!=null){
				this.outputReceiver.close();
			}
			this.outputReceiver = null;
		}
		
		public void open() throws MidiUnavailableException{
			if(!outputDevice.isOpen()){
				this.outputDevice.open();
				this.outputReceiver = this.outputDevice.getReceiver();
			}
		}

		public int inputCount(){
			return this.inputs.size();
		}
		
		public void register(MidiIO m) throws MidiUnavailableException{
			if(this.inputCount() <= 0){
				this.open();
			}
			if(!this.inputs.contains(m)){
				m.addMidiListener(this);
				this.inputs.add(m);
			}
		}
		
		public void unregister(MidiIO m){
			this.inputs.remove(m);
			m.removeMidiListener(this);
			if(this.inputCount() <= 0){
				this.close();
			}
		}
		
		@Override
		public void listen(MidiIO source, MidiMessage msg, long timestamp) {
			if(outputReceiver != null){
				outputReceiver.send(msg, timestamp);
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object o = arg0.getSource();
		if(o instanceof JComboBox){
			try {
				this.setOutputID(((JComboBox) o).getSelectedIndex());
			} catch (MidiUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}
