package midinotezoneplugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiIO;
import defaults.MidiListener;
import engine.MidiUtilities;
import plugin.Plugin;
import plugin.events.NewOutputRequestEvent;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;
import pluginhost.events.NewOutputEvent;

public class MidiNoteZone extends Plugin implements MidiListener,ActionListener {
	protected LinkedList<Zone> zones;
	private transient MidiNoteZoneView view;
	public MidiNoteZone(PluginHost host) {
		super(host, "MIDI Zone", 1, 1, 0, -1);
	}
	
	protected void addZone(){
		NewOutputRequestEvent e = new NewOutputRequestEvent();
		this.getPluginHost().notify(e);
		Zone z = new Zone(this,e.io.getId());
		zones.add(z);
		z.init();
		view.updateView();
	}
	
	protected void removeZome(Zone zone){
		zones.remove(zone);
		int i = 0;
		for(Zone z:zones){
			z.setIndex(i++);
		}
	}
	
	protected int indexOfZone(Zone zone){
		return zones.indexOf(zone);
	}
	
	public static Plugin getInstance(PluginHost host){
		return new MidiNoteZone(host);
	}

	@Override
	public JComponent getMinimizedView() {
		return null;
	}

	@Override
	public JComponent getFullView() {
		return view;
	}

	@Override
	public void notify(HostEvent e) {
		if(e.getClass() == NewOutputEvent.class){
			Zone z = new Zone(this,((NewOutputEvent)e).getNewOutput().getId());
			zones.add(z);
			z.init();
			view.updateView();
		}
	}

	@Override
	public void load() {
		this.zones = new LinkedList<Zone>();
		this.initPlugin();
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
		MidiNoteZone tmp = new MidiNoteZone(newHost);
		LinkedList<Zone> newList = new LinkedList<Zone>();
		for(Zone z:zones){
			newList.add(z.clone());
		}
		tmp.initPlugin();
		return tmp;
	}

	@Override
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		if(MidiUtilities.getStatus(msg)!= MidiUtilities.NOTE_ON &&
			MidiUtilities.getStatus(msg)!= MidiUtilities.NOTE_OFF ){
			for(MidiIO m:getPluginHost().getOutputs()){
				m.send(msg, timestamp);
			}
		}
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.initPlugin();
	}
	
	private void initPlugin(){
		this.getPluginHost().getInput(0).addMidiListener(this);
		for(Zone z:zones){
			z.init();
		}
		this.view = new MidiNoteZoneView(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		addZone();
	}

}
