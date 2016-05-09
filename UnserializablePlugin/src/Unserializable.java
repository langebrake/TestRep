
import guiinterface.SizeableComponent;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import plugin.Plugin;
import plugin.events.NewInputRequestEvent;
import plugin.events.NewOutputRequestEvent;
import pluginhost.events.*;
import pluginhost.PluginHost;
import defaults.DefaultView;
import defaults.MidiIO;
import defaults.MidiIOThrough;
import defaults.MidiListener;



public class Unserializable extends Plugin implements MidiListener{
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "ThisIsNotSerializable!";
	private String msg = "DummyPlugin";
	private DefaultView view;
	private Method m = null;
	
	
	public static Plugin getInstance(PluginHost host){
		return new Unserializable(host);
	}
	public Unserializable(PluginHost host){
		super(host);
	
		
	}
	private boolean block;
	
	
	@Override
	public void notify(HostEvent e) {
	
	}
	@Override
	public JComponent getMinimizedView() {
		return view;
	}
	@Override
	public Component getFullView() {
		return new DefaultView("You can't save this plugins state!");
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
		host.getInput(0).addMidiListener(this);
		try {
			this.m   = Plugin.class.getMethod("getInstance", PluginHost.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	@Override
	public boolean close() {
		PluginHost host = this.getPluginHost();
		MidiIOThrough input = host.getInput(0);
		MidiIOThrough output = host.getOuput(0);
		input.disconnectOutput();
		output.disconnectInput();
		return true;
		
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
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		
	}
	@Override
	public boolean reOpen() {
		return true;
		
	}
	
	

}
