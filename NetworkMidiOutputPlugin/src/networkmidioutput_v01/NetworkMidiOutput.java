package networkmidioutput_v01;

import java.awt.Component;
import java.net.Socket;

import javax.swing.JComponent;

import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class NetworkMidiOutput extends Plugin {

	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 0;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "Network Output";
	private transient FullView fullView;
	private transient MinView minView;
	private int port;
	private String name;
	private transient Socket client;
	
	public NetworkMidiOutput(PluginHost host) {
		super(host,NAME,MININPUTS,MAXINPUTS,MINOUTPUTS,MAXOUTPUTS);
	}

	public static NetworkMidiOutput getInstance(PluginHost host){
		return new NetworkMidiOutput(host);
	}
	
	@Override
	public JComponent getMinimizedView() {
		return this.minView;
	}

	@Override
	public JComponent getFullView() {
		return this.fullView;
	}

	@Override
	public void notify(HostEvent e) {

	}

	@Override
	public void load() {

	}

	@Override
	public boolean close() {
		return false;
	}

	@Override
	public boolean reOpen() {
		return false;
	}

	@Override
	public Plugin clone(PluginHost host) {
		return null;
	}

}
