package defaultplugin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.*;
import dev.pluginevents.NewInputRequestEvent;
import dev.pluginevents.NewOutputRequestEvent;

public class MidiThrough extends Plugin implements MidiListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1918610428962791553L;
	private transient HashMap<MidiIOCommunicator, MidiIOCommunicator> ioMap;

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new MidiThrough(host);
	}

	public MidiThrough(PluginHostCommunicator host) {
		super(host, "Midi Through", 1, -1, 1, -1);
	}


	@Override
	public void notify(HostEvent e) {
			if (e.getClass() == NewInputEvent.class) {
					NewOutputRequestEvent event = new NewOutputRequestEvent();
					this.getPluginHost().notify(event);
					if (event.io != null) {
						this.ioMap.put(((NewInputEvent) e).getNewInput(), event.io);
						((NewInputEvent) e).getNewInput().addMidiListener(this);
					}
			} else if (e.getClass() == NewOutputEvent.class) {
					NewInputRequestEvent event = new NewInputRequestEvent();
					this.getPluginHost().notify(event);
					if (event.io != null) {
						this.ioMap.put(event.io, ((NewOutputEvent) e).getNewOutput());
						(event.io).addMidiListener(this);
					}
				}
			
	}

	@Override
	public void load() {
		this.initPluging();
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
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		ioMap.get(source).send(msg, timestamp);
	}



	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPluging();
	}

	private void initPluging() {
		this.ioMap = new HashMap<MidiIOCommunicator, MidiIOCommunicator>();
		int i = 0;
		for (MidiIOCommunicator m : this.getPluginHost().getInputs()) {
			m.addMidiListener(this);
			ioMap.put(m, this.getPluginHost().getOutput(i++));
		}
	}

	@Override
	public Plugin clone(PluginHostCommunicator host) {
		MidiThrough dfp = new MidiThrough(host);
		dfp.initPluging();
		return dfp;
	}

	@Override
	public JComponent getMinimizedView() {
		return null;
	}

	@Override
	public JComponent getFullView() {
		return null;
	}

}
