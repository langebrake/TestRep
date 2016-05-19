package analyzerplugin_v01;
import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiIO;
import defaults.MidiListener;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class Analyzer extends Plugin implements MidiListener {
	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "Midi Analyzer";
	private transient MinView fullView;
	private transient MinView minView;
	
	public static Analyzer getInstance(PluginHost host){
		return new Analyzer(host);
	}
	
	public Analyzer(PluginHost host) {
		super(host);
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public JComponent getMinimizedView() {
		return this.minView;
	}

	@Override
	public Component getFullView() {

		return this.fullView;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public void setDisplayName() {
		// TODO Auto-generated method stub
		
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
		this.initPlugin();
		
	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reOpen() {
		return false;
	}

	@Override
	public Plugin clone() {
		Analyzer a = new Analyzer(Plugin.waitForHost());
		a.initPlugin();
		return a;
	}

	@Override
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		this.getPluginHost().getOuput(0).send(msg, timestamp);
		this.minView.updateView(msg);
		this.fullView.updateView(msg);
	}
	
	
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.setPluginHost(Plugin.waitForHost());
		this.initPlugin();
	}
	
	private void initPlugin(){
		this.getPluginHost().getInput(0).addMidiListener(this);
		this.minView = new MinView();
		this.fullView = new MinView();
	}

}
