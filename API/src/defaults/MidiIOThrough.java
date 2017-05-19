package defaults;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import dev.pluginevents.PluginMidiProcessingError;
import engine.Stringer;
import pluginhost.PluginHost;

public class MidiIOThrough implements MidiIO, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1332699547375999233L;
	/**
	 * Connecting MidiIO:
	 */
	// TODO: host transient? maybe less disk space, but longer loading times
	private transient PluginHost host;
	private MidiIO output;
	private MidiIO input;
	private transient LinkedList<MidiListener> listeners;

	public static PluginHost waiterHost;

	public MidiIOThrough(PluginHost host) {
		this.host = host;
		this.input = null;
		this.output = null;
		this.listeners = new LinkedList<MidiListener>();
	}

	public void send(MidiMessage message, long timeStamp) {
		try {
			if (this.hasOutput()) {
				this.output.send(message, timeStamp);
			}
			for (MidiListener l : listeners) {
				l.listen(this, message, timeStamp);
			}
		} catch (Exception e) {
			this.host.notifyPluginStateChangedListener(new PluginMidiProcessingError(e, this.host));
		}

	}

	/**
	 * setting the receiver will set this instance as transmitter of the
	 * receiver
	 */
	public void setOutput(MidiIO output) {
		if (output == this) {
			throw new IllegalArgumentException("Connceted output is loop");
		}
		this.output = output;

	}

	/**
	 * Setting the transmitter will set this instance as receiver of the
	 * transmitter
	 * 
	 * @param t
	 */
	public void setInput(MidiIO input) {
		
		if (input == this) {
			throw new IllegalArgumentException("Connectet input is loop");
		}
		this.input = input;
	}

	public MidiIO getOutput() {
		return this.output;
	}

	public MidiIO getInput() {
		return this.input;
	}

	/**
	 * Disconnecting this input will disconnect the inputs output!
	 */
	public void disconnectInput() {
		this.input = null;
	}

	/**
	 * Disconnecting this output will disconnect the outputs input!
	 */
	public void disconnectOutput() {
		this.output = null;
	}

	public boolean hasOutput() {
		return this.output != null;
	}

	public boolean hasInput() {
		return this.input != null;
	}

	public PluginHost getParent() {
		return this.host;
	}

	public int getId() {
		return this.host.getMidiIOId(this);
	}

	@Override
	public void addMidiListener(MidiListener listener) {
		if (!listeners.contains(listener))
			this.listeners.add(listener);

	}

	@Override
	public void removeMidiListener(MidiListener listener) {
		this.listeners.remove(listener);
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		this.listeners = new LinkedList<MidiListener>();
		in.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		String stringer = Stringer.getString();
		out.defaultWriteObject();
		Stringer.minimize();
	}

	@Override
	public void setPluginHost(PluginHost host) {
		this.host = host;

	}

}
