package combineportsplugin_v01;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiIO;
import defaults.MidiListener;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class Combine extends Plugin implements MidiListener {

	public Combine(PluginHost host) {
		super(host, "Converge Ports", 1, -1, 1, 1);
	}
	
	public static Plugin getInstance(PluginHost host){
		return new Combine(host);
	}

	@Override
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		this.getPluginHost().getOuput(0).send(msg, timestamp);
		
	}

	@Override
	public JComponent getMinimizedView() {
		return null;
	}

	@Override
	public JComponent getFullView() {
		return null;
	}

	@Override
	public void notify(HostEvent e) {
		this.reload();
		
	}

	@Override
	public void load() {
		this.reload();
		
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean reOpen() {
		return true;
	}

	@Override
	public Plugin clone(PluginHost newHost) {
		Combine tmp = new Combine(newHost);
		tmp.reload();
		return tmp;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		reload();
	}
	
	private void reload(){
		for(MidiIO m:this.getPluginHost().getInputs()){
			m.removeMidiListener(this);
			m.addMidiListener(this);
		}
	}

}
