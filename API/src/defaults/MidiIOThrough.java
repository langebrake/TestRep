package defaults;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import pluginhost.PluginHost;


public class MidiIOThrough implements MidiIO,Serializable{
/**
 * Connecting MidiIO:
 */
	private PluginHost host;
	private MidiIO output;
	private MidiIO input;
	private LinkedList<MidiListener> listeners;
	
	public MidiIOThrough(){
		this(null);
	}
	public MidiIOThrough(PluginHost host){
		this.host=host;
		this.listeners = new LinkedList<MidiListener>();
	}

	public void send(MidiMessage message, long timeStamp) {
		if(this.hasOutput()){
			this.output.send(message, timeStamp);
		}
		for(MidiListener l:listeners){
			l.listen(message, timeStamp);
		}
		
		
	}


	
	
	/**
	 * setting the receiver will set this instance as transmitter of the receiver
	 */
	public void setOutput(MidiIO output) {
		if(output == this){
			throw new IllegalArgumentException("Connceted output is loop");
		}
		this.output = output;
		
	}
	
	/**
	 * Setting the transmitter will set this instance as receiver of the transmitter
	 * @param t
	 */
	public void setInput(MidiIO input){
		if(input == this){
			throw new IllegalArgumentException("Connectet input is loop");
		}
		this.input = input;
	}
	
	public MidiIO getOutput(){
		return this.output;
	}
	
	public MidiIO getInput(){
		return this.input;
	}
	
	/**
	 * Disconnecting this input will disconnect the inputs output!
	 */
	public void disconnectInput(){
		this.input = null;
	}
	
	/**
	 * Disconnecting this output will disconnect the outputs input!
	 */
	public void disconnectOutput(){
		this.output = null;
	}
	
	public boolean hasOutput(){
		return this.output!=null;
	}
	
	public boolean hasInput(){
		return this.input!=null;
	}
	
	public PluginHost getParent(){
		return this.host;
	}

	public int getId(){
		return this.host.getMidiIOId(this);
	}

	@Override
	public void addMidiListener(MidiListener listener) {
		this.listeners.add(listener);
		
	}
	
	@Override
	public void removeMidiListener(MidiListener listener){
		this.listeners.remove(listener);
	}
	

}