package defaultplugin;

import guiinterface.SizeableComponent;

import java.awt.Component;
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
import pluginhost.PluginHostCommunicator;
import defaults.DefaultView;
import defaults.MidiIOCommunicator;
import defaults.MidiIOThrough;
import defaults.MidiListener;

public class DefaultPlugin extends Plugin implements MidiListener {
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "DummyPlugin";
	private String msg = "DummyPlugin";
	private DefaultView view;
	private transient HashMap<MidiIOCommunicator, MidiIOCommunicator> ioMap;

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new DefaultPlugin(host);
	}

	public DefaultPlugin(PluginHostCommunicator host) {
		super(host, NAME, MININPUTS, MAXINPUTS, MINOUTPUTS, MAXOUTPUTS);
		this.ioMap = new HashMap<MidiIOCommunicator, MidiIOCommunicator>();

	}

	private boolean block;

	@Override
	public void notify(HostEvent e) {
		if (!block) {
			block = true;
			if (e.getClass() == NewInputEvent.class) {
				if (!ioMap.containsKey(((NewInputEvent) e).getNewInput())) {
					NewOutputRequestEvent event = new NewOutputRequestEvent();
					this.getPluginHost().notify(event);
					if (event.io != null) {
						this.ioMap.put(((NewInputEvent) e).getNewInput(), event.io);
						((NewInputEvent) e).getNewInput().addMidiListener(this);
					}
				}
			} else if (e.getClass() == NewOutputEvent.class) {
				if (!ioMap.containsValue(((NewOutputEvent) e).getNewOutput())) {
					NewInputRequestEvent event = new NewInputRequestEvent();
					this.getPluginHost().notify(event);
					if (event.io != null) {
						this.ioMap.put(event.io, ((NewOutputEvent) e).getNewOutput());
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
	public JComponent getFullView() {
		return new DefaultView("DEFAULT PLUGIN INTERFACE");
	}

	@Override
	public void load() {
		this.initPluging();
	}

	@Override
	public boolean close() {
		PluginHostCommunicator host = this.getPluginHost();
		MidiIOCommunicator input = host.getInput(0);
		MidiIOCommunicator output = host.getOutput(0);

		return true;

	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {

		ioMap.get(source).send(msg, timestamp);

	}

	@Override
	public boolean reOpen() {
		return true;

	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPluging();
	}

	private void initPluging() {
		this.view = new DefaultView(msg);
		this.ioMap = new HashMap<MidiIOCommunicator, MidiIOCommunicator>();
		int i = 0;
		for (MidiIOCommunicator m : this.getPluginHost().getInputs()) {
			m.addMidiListener(this);
			ioMap.put(m, this.getPluginHost().getOutput(i++));
		}
	}

	@Override
	public Plugin clone(PluginHostCommunicator host) {
		DefaultPlugin dfp = new DefaultPlugin(host);
		dfp.initPluging();

		return dfp;
	}

}
