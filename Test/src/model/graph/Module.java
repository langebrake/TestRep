package model.graph;

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

	public Module() throws MidiUnavailableException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void ConnectNewInput(MidiIOThrough toConnect) throws PluginMaxInputsExceededException{
		super.connectNewInput(toConnect);
		}

	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

}
