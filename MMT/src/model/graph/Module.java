package model.graph;

import gui.interactivepane.Vector;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiUnavailableException;

import pluginhost.PluginHost;

public class Module extends PluginHost {

	/**
	 * 
	 */
	private static final long serialVersionUID = 707303639669192953L;
	public Vector origin;

	public Module() throws MidiUnavailableException {
		super();
		origin = new Vector();
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
	}

	public Module clone() {
		Module tmp = (Module) super.clone();
		tmp.origin = this.origin;
		return tmp;
	}
}
