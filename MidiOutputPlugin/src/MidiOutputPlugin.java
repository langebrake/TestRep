import java.io.IOException;
import java.io.ObjectInputStream;
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
import defaults.MidiListener;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class MidiOutputPlugin extends Plugin implements MidiListener {

	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 0;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "MidiOutputPlugin";
	private transient MidiDevice outputdevice;
	private transient Receiver outputreceiver;
	private String mididDeviceName;
	
	public static MidiOutputPlugin getInstance(PluginHost host){
		return new MidiOutputPlugin(host);
	}
	
	public MidiOutputPlugin(PluginHost host) {
		super(host);
	}

	@Override
	public JComponent getMinimizedView() {
		return new DefaultView("MIDIOUTPUT");
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
		MidiEngine engine = this.getPluginHost().getEngine();
		LinkedList<MidiDevice> outputs = engine.getOutputDevices();
		for(int i = 0; i<outputs.size();i++){
			System.out.println(i+": "+outputs.get(i).getDeviceInfo());
		}
		
		try{
			System.out.println("Output Device Index: ");
			Scanner s = new Scanner(System.in);
			int i = Integer.parseInt(s.nextLine());
			outputdevice = outputs.get(i);
			outputdevice.open();
			outputreceiver = outputdevice.getReceiver();
			this.mididDeviceName = outputdevice.getDeviceInfo().getName();
			
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(outputreceiver == null) {
			System.err.println("No Input Transmitter obtained!");
		} else{
			this.getPluginHost().getInput(0).addMidiListener(this);
		}
		
		
	}

	@Override
	public boolean close() {
		if(outputdevice != null)
			outputdevice.close();
		
		return true;
	}
	
	@Override
	public boolean reOpen(){
		if(outputdevice != null)
			try {
				outputdevice.open();
				this.outputreceiver = outputdevice.getReceiver();
				
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

	@Override
	public void listen(MidiIO source,MidiMessage msg, long timestamp) {
		if(this.outputdevice.isOpen())
			this.outputreceiver.send(msg, timestamp);
		
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.outputdevice = this.getPluginHost().getEngine().getOutputDevice(mididDeviceName);
		try {
			this.outputdevice.open();
			this.outputreceiver = this.outputdevice.getReceiver();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
