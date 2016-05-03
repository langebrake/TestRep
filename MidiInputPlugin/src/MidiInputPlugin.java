import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Scanner;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JComponent;
import javax.swing.JFrame;

import midiengine.MidiEngine;
import defaults.DefaultView;
import defaults.MidiIO;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class MidiInputPlugin extends Plugin implements Receiver, Serializable{

	private static final int MAXINPUTS = 0;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "MidiInputPlugin";
	private transient MidiDevice inputdevice;
	private transient Transmitter inputtransmitter;
	private String midiDeviceName;
	private MidiIO output;
	
	public static MidiInputPlugin getInstance(PluginHost host){
		return new MidiInputPlugin(host);
	}
	
	public MidiInputPlugin(PluginHost host) {
		super(host);
	}

	@Override
	public JComponent getMinimizedView() {
		return new DefaultView("MIDIPINPUT");
	}

	@Override
	public JFrame getFullView() {
		JFrame frame = new JFrame("MIDIINPUT");
		
		frame.add(new DefaultView("MIDIINPUT"));
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
		MidiEngine engine = this.getPluginHost().getEngine();
		LinkedList<MidiDevice> inputs = engine.getInputDevices();
		for(int i = 0; i<inputs.size();i++){
			System.out.println(i+": "+inputs.get(i).getDeviceInfo());
		}
		
		try{
			Scanner s = new Scanner(System.in);
			System.out.println("Input Device Index: ");
			int i = Integer.parseInt(s.nextLine());
			inputdevice = inputs.get(i);
			inputdevice.open();
			this.midiDeviceName = inputdevice.getDeviceInfo().getName();
			inputtransmitter = inputdevice.getTransmitter();
			
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputtransmitter == null) {
			System.err.println("No Input Transmitter obtained!");
		} else{
			inputtransmitter.setReceiver(this);
			
			this.output = this.getPluginHost().getOuput(0);
		}

		
	}

	@Override
	public void close() {
		if(inputdevice != null)
		inputdevice.close();
		
	}

	@Override
	public String getDisplayName() {
		return this.midiDeviceName;
	}

	@Override
	public void setDisplayName() {
		
	}


	@Override
	public void send(MidiMessage message, long timeStamp) {
		this.output.send(message, timeStamp);
		
	}
	
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.inputdevice = this.getPluginHost().getEngine().getInputDevice(this.midiDeviceName);
		try {
			this.inputdevice.open();
			this.inputtransmitter = this.inputdevice.getTransmitter();
			inputtransmitter.setReceiver(this);
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void reOpen() {
		if(this.inputdevice != null)
			try {
				this.inputdevice.open();
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}


}
