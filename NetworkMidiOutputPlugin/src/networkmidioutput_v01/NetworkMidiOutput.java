package networkmidioutput_v01;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.sound.midi.MidiMessage;
import javax.swing.JComponent;

import defaults.MidiIOCommunicator;
import defaults.MidiIOCommunicator;
import defaults.MidiListener;
import plugin.Plugin;
import pluginhost.PluginHostCommunicator;
import pluginhost.PluginHostCommunicator;
import pluginhost.events.HostEvent;

public class NetworkMidiOutput extends Plugin implements MidiListener, ActionListener {

	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 0;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "Network Output";
	private transient FullView fullView;
	private transient MinView minView;
	int port;
	String name;
	private transient Socket client;
	private transient DataOutputStream dout;

	public NetworkMidiOutput(PluginHostCommunicator host) {
		super(host, NAME, MININPUTS, MAXINPUTS, MINOUTPUTS, MAXOUTPUTS);
	}

	public static NetworkMidiOutput getInstance(PluginHostCommunicator host) {
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
		this.port = -1;
		this.name = "";
		this.initPlugin();
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
	public Plugin clone(PluginHostCommunicator host) {
		return null;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	private void initPlugin() {
		this.fullView = new FullView();
		this.minView = new MinView(this);
		this.getPluginHost().getInput(0).addMidiListener(this);
		createClient();

	}

	private void createClient() {
		if (name != "" && port != -1) {
			try {
				this.client = new Socket(name, port);
				this.dout = new DataOutputStream(client.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		if (client != null && dout != null && !client.isClosed()) {
			try {
				dout.writeInt(msg.getLength());
				dout.write(msg.getMessage());
				dout.writeLong(timestamp);
				dout.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.name = minView.getServerName();
		this.port = minView.getServerPort();
		if (this.client != null && !this.client.isClosed() && this.dout != null) {
			try {
				dout.close();
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		this.initPlugin();

	}

}
