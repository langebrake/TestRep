import java.util.LinkedList;
import java.util.Scanner;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;
import javax.swing.JComponent;

import midiengine.MidiEngine;
import defaults.DefaultView;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class MidiInputPlugin extends Plugin{

	private static final int MAXINPUTS = 0;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "MidiInputPlugin";
	private MidiDevice inputdevice;
	private Transmitter inputtransmitter;
	
	public static MidiInputPlugin getInstance(PluginHost host){
		return new MidiInputPlugin(host);
	}
	
	public MidiInputPlugin(PluginHost host) {
		super(host);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return new DefaultView("MIDIPINPUT");
	}

	@Override
	public JComponent getFullView() {
		// TODO Auto-generated method stub
		return new DefaultView("MIDIINPUT");
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public int getMaxInputs() {
		// TODO Auto-generated method stub
		return MAXINPUTS;
	}

	@Override
	public int getMaxOutputs() {
		// TODO Auto-generated method stub
		return MAXOUTPUTS;
	}

	@Override
	public int getMinInputs() {
		// TODO Auto-generated method stub
		return MININPUTS;
	}

	@Override
	public int getMinOutputs() {
		// TODO Auto-generated method stub
		return MINOUTPUTS;
	}

	@Override
	public void notify(HostEvent e) {
		// TODO Auto-generated method stub
		
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
			inputtransmitter = inputdevice.getTransmitter();
			
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputtransmitter == null) {
			System.err.println("No Input Transmitter obtained!");
		} else{
			this.getPluginHost().getOuput(0).setInput(inputtransmitter);
		}

		
	}

	@Override
	public void close() {
		inputdevice.close();
		
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return NAME;
	}


}
