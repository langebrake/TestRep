package defaultplugin;
import guiinterface.SizeableComponent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import plugin.Plugin;
import pluginhost.events.*;
import pluginhost.PluginHost;
import defaults.DefaultView;
import defaults.MidiIO;
import defaults.MidiIOThrough;
import defaults.MidiListener;



public class DefaultPlugin extends Plugin implements MidiListener{
	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "DummyPlugin";
	private String msg = "DummyPlugin";
	private DefaultView view;
	private MidiIO input,output;
	
	public static Plugin getInstance(PluginHost host){
		return new DefaultPlugin(host);
	}
	public DefaultPlugin(PluginHost host){
		super(host);
	}
	@Override
	public void notify(HostEvent e) {
		
	}
	@Override
	public JComponent getMinimizedView() {
		return view;
	}
	@Override
	public JFrame getFullView() {
		JFrame frame = new JFrame(this.NAME);
		
		frame.add(new DefaultView("DEFAULT PLUGIN INTERFACE"));
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
	public void load() {
		this.view = new DefaultView(msg);
		PluginHost host = this.getPluginHost();
		input = host.getInput(0);
		output = host.getOuput(0);
		input.addMidiListener(this);
		
		
		
	}
	@Override
	public void close() {
		PluginHost host = this.getPluginHost();
		MidiIOThrough input = host.getInput(0);
		MidiIOThrough output = host.getOuput(0);
		input.disconnectOutput();
		output.disconnectInput();
		
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
	public String getDisplayName() {
		return NAME;
	}
	
	@Override
	public void setDisplayName() {
	
		
	}
	@Override
	public void listen(MidiMessage msg, long timestamp) {
		output.send(msg, timestamp);
		
	}
	@Override
	public void reOpen() {
		// TODO Auto-generated method stub
		
	}
	

}
