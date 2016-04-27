package defaultplugin;
import guiinterface.SizeableComponent;

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



public class DefaultPlugin extends Plugin implements Receiver, Transmitter{
	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "DummyPlugin";
	private String msg = "DummyPlugin";
	private Receiver rc;
	private DefaultView view;
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
		MidiIO input = host.getInput(0);
		MidiIO output = host.getOuput(0);
		input.setOutput(this);
		this.setReceiver(output);
		Utilities.print();
		
		
	}
	@Override
	public void close() {
		PluginHost host = this.getPluginHost();
		MidiIO input = host.getInput(0);
		MidiIO output = host.getOuput(0);
		input.disconnectOutput();
		
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
	public Receiver getReceiver() {
		return null;
	}
	@Override
	public void setReceiver(Receiver arg0) {
		this.rc = arg0;
		
	}
	@Override
	public void send(MidiMessage arg0, long arg1) {
		this.msg = arg0.getMessage().toString();
		
		this.rc.send(arg0, arg1);
	}
	@Override
	public void setDisplayName() {
	
		
	}


}
