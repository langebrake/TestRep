
import guiinterface.SizeableComponent;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.Random;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;
import javax.swing.JPanel;

import defaults.DefaultView;
import defaults.MidiListener;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.*;

public class Unserializable extends Plugin implements MidiListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 268244059974535432L;
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "ThisIsNotSerializable!";
	private static final Random rn = new Random();
	private Color c = new Color(rn.nextInt(255), rn.nextInt(255), rn.nextInt(255));

	public static Plugin getInstance(PluginHostCommunicator host) {
		return new Unserializable(host);
	}

	public Unserializable(PluginHostCommunicator host) {
		super(host, NAME, MININPUTS, MAXINPUTS, MINOUTPUTS, MAXOUTPUTS);

	}


	@Override
	public void notify(HostEvent e) {

	}

	@Override
	public JComponent getMinimizedView() {
		JPanel p = new JPanel();
		p.setBackground(c);
		return p;
	}

	@Override
	public JComponent getFullView() {
		return new DefaultView("You can't save this plugins state!");
	}

	@Override
	public void load() {
		PluginHostCommunicator host = this.getPluginHost();
		host.getInput(0).addMidiListener(this);
		try {
			this.m = Plugin.class.getMethod("getInstance", PluginHostCommunicator.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean close() {
		return true;

	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {

	}

	@Override
	public boolean reOpen() {
		return true;

	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		try {
			this.m = Plugin.class.getMethod("getInstance", PluginHostCommunicator.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Plugin clone(PluginHostCommunicator host) {
		return new Unserializable(host);
	}

}
