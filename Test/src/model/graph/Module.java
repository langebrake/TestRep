package model.graph;

import midiengine.MidiEngine;
import defaults.MidiIO;
import plugin.Plugin;
import plugin.events.PluginEvent;
import pluginhost.events.*;
import pluginhost.PluginHost;
import pluginhost.exceptions.*;

public class Module extends PluginHost {

	public Module(MidiEngine e) {
		super(e);
		// TODO Auto-generated constructor stub
	}
	
	public void ConnectNewInput(MidiIO toConnect) throws PluginMaxInputsExceededException{
		super.connectNewInput(toConnect);
		
		}

	@Override
	public void notify(PluginEvent e) {
		// TODO Auto-generated method stub
		
	}

}
