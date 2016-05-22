package transposenote_v01;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import defaults.MidiIO;
import defaults.MidiListener;
import engine.MidiUtilities;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class TransposeNote extends Plugin implements MidiListener, ActionListener {
	private transient MinView minView;
	byte transpose;
	public TransposeNote(PluginHost host) {
		super(host, "Transpose", 1, 1, 1, 1);
	}

	public static Plugin getInstance(PluginHost host){
		return new TransposeNote(host);
	}
	@Override
	public JComponent getMinimizedView() {
		return this.minView;
	}

	@Override
	public JComponent getFullView() {
		return null;
	}

	@Override
	public void notify(HostEvent e) {

	}

	@Override
	public void load() {
		this.transpose = 0;
		this.initPlugin();

	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
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
		TransposeNote tmp = new TransposeNote(newHost);
		tmp.transpose = transpose;
		tmp.initPlugin();
		return tmp;
	}
	
	private void initPlugin(){
		this.getPluginHost().getInput(0).addMidiListener(this);
		this.minView = new MinView(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.transpose = this.minView.getTranspose();
		
	}

	@Override
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		if(MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_ON || 
				MidiUtilities.getStatus(msg) == MidiUtilities.NOTE_OFF){
			if(MidiUtilities.getData1(msg) + transpose >=0 && MidiUtilities.getData1(msg) + transpose <=127){
				try {
					getPluginHost().getOuput(0).send(
							new ShortMessage(MidiUtilities.getStatus(msg)<<4,
									MidiUtilities.getChannel(msg),
									MidiUtilities.getData1(msg)+transpose,
									MidiUtilities.getData2(msg))
									, timestamp);
				} catch (InvalidMidiDataException e) {
					e.printStackTrace();
				}
			}else{
				getPluginHost().getOuput(0).send(msg, timestamp);
			}
		} else {
			getPluginHost().getOuput(0).send(msg,timestamp);
		}
	}
}