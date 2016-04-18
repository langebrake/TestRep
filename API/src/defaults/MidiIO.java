package defaults;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import pluginhost.PluginHost;


public class MidiIO implements Transmitter,Receiver{
/**
 * Connecting MidiIO:
 */
	private PluginHost host;
	private Receiver output;
	private Transmitter input;
	
	public MidiIO(PluginHost host){
		this.host=host;
	}
	@Override
	public void send(MidiMessage message, long timeStamp) {
		if(this.hasOutput()){
			this.output.send(message, timeStamp);
		}
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Receiver getReceiver() {
		// TODO Auto-generated method stub
		return this.output;
	}
	
	@Override
	public void setReceiver(Receiver r){
		this.setOutput(r);
	}
	
	/**
	 * setting the receiver will set this instance as transmitter of the receiver
	 */
	public void setOutput(Receiver r) {
		if(r == this){
			throw new IllegalArgumentException("Connceted output is loop");
		}
		this.output = r;
		if (r instanceof MidiIO){
			if(((MidiIO) r).input != this){
				((MidiIO) r).setInput(this);
			}
		}
	}
	
	/**
	 * Setting the transmitter will set this instance as receiver of the transmitter
	 * @param t
	 */
	public void setInput(Transmitter t){
		if(t == this){
			throw new IllegalArgumentException("Connectet input is loop");
		}
		this.input = t;
		if (t instanceof MidiIO){
			if(((MidiIO) t).output != this){
				((MidiIO) t).setReceiver(this);
			}
		}
	}
	
	public Receiver getOutput(){
		return this.output;
	}
	
	public Transmitter getInput(){
		return this.input;
	}
	
	/**
	 * Disconnecting this input will disconnect the inputs output!
	 */
	public void disconnectInput(){
		if(this.hasInput()){
			Transmitter tmp = this.input;
			this.input = null;
			if(tmp instanceof MidiIO){
				((MidiIO) tmp).disconnectOutput();
			}
		}
	}
	
	/**
	 * Disconnecting this output will disconnect the outputs input!
	 */
	public void disconnectOutput(){
		if(this.hasOutput()){
			Receiver tmp = this.output;
			this.output = null;
			if(tmp instanceof MidiIO){
				((MidiIO) tmp).disconnectInput();
			}
		}
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
}
