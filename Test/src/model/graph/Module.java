package model.graph;

import gui.interactivepane.Vector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.sound.midi.MidiUnavailableException;

import midiengine.MidiEngine;
import defaults.MidiIOThrough;
import plugin.Plugin;
import plugin.events.PluginEvent;
import pluginhost.events.*;
import pluginhost.PluginHost;
import pluginhost.exceptions.*;

public class Module extends PluginHost {

	public Vector origin;

	public Module() throws MidiUnavailableException {
		super();
		origin = new Vector();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

	public Module clone() {
		Module tmp = (Module) super.clone();
		tmp.origin = this.origin;
		return tmp;
	}
}
