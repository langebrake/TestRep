package defaultplugin;
import guiinterface.SizeableComponent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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



public class DefaultPlugin extends Plugin implements MidiListener{
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "DummyPlugin";
	private String msg = "DummyPlugin";
	private DefaultView view;
	private HashMap<MidiIO,MidiIO> ioMap;
	
	public static Plugin getInstance(PluginHost host){
		return new DefaultPlugin(host);
	}
	public DefaultPlugin(PluginHost host){
		super(host);
		this.ioMap = new HashMap<MidiIO,MidiIO>();
		
	}
	private boolean block;
	@Override
	public void notify(HostEvent e) {
		if(!block){
			block = true;
			if(e.getClass() == NewInputEvent.class){
				if(!ioMap.containsKey(((NewInputEvent)e).getNewInput())){
					NewOutputRequestEvent event = new NewOutputRequestEvent();
					this.getPluginHost().notify(event);
					if(event.io != null){
						this.ioMap.put(((NewInputEvent)e).getNewInput(), event.io);
						((NewInputEvent)e).getNewInput().addMidiListener(this);
					}
				}
			} else if(e.getClass() == NewOutputEvent.class){
				if(!ioMap.containsValue(((NewOutputEvent)e).getNewOutput())){
					NewInputRequestEvent event = new NewInputRequestEvent();
					this.getPluginHost().notify(event);
					if(event.io != null){
						this.ioMap.put(event.io,((NewOutputEvent)e).getNewOutput());
						(event.io).addMidiListener(this);
					}
				}
			}
		}
		block = false;
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
		this.ioMap.put(host.getInput(0), host.getOuput(0));
		host.getInput(0).addMidiListener(this);
		
		
		
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
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		ioMap.get(source).send(msg, timestamp);
		
	}
	@Override
	public void reOpen() {
		// TODO Auto-generated method stub
		
	}
	

}
