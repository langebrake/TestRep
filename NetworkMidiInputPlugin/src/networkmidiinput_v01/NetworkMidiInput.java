package networkmidiinput_v01;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class NetworkMidiInput extends Plugin implements ActionListener {
	private static final int MAXINPUTS = 0;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "Network Input";
	private transient FullView fullView;
	private transient MinView minView;
	int port;
	private transient ServerSocket server;
	private transient Socket client;

	public NetworkMidiInput(PluginHost host) {
		super(host, NAME, MININPUTS, MAXINPUTS, MINOUTPUTS, MAXOUTPUTS);
	}

	public static NetworkMidiInput getInstance(PluginHost host) {
		return new NetworkMidiInput(host);
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
		this.initPlugin();

	}

	private void initPlugin() {
		this.fullView = new FullView(this);
		this.minView = new MinView(this);
		if (this.port != -1) {
			this.openServer();
		}
	};

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean reOpen() {
		return true;
	}

	@Override
	public Plugin clone(PluginHost host) {
		NetworkMidiInput tmp = new NetworkMidiInput(host);
		tmp.load();
		return tmp;
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.initPlugin();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private transient Thread t;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (this.server != null) {
			try {
				this.client.close();
				this.server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		this.port = this.minView.getPort();
		this.openServer();

	}

	private void openServer() {
		t = new Thread() {
			public void run() {
				try {
					NetworkMidiInput.this.server = new ServerSocket(NetworkMidiInput.this.port);
					NetworkMidiInput.this.client = NetworkMidiInput.this.server.accept();
					DataInputStream din = new DataInputStream(NetworkMidiInput.this.client.getInputStream());
					int length;
					while ((length = din.readInt()) > 0) {
						byte[] msg = new byte[length];
						din.readFully(msg, 0, length);
						ShortMessage sendThis = new ShortMessage();
						sendThis.setMessage(msg[0], msg[1], msg[2]);
						long timestamp = din.readLong();
						NetworkMidiInput.this.getPluginHost().getOuput(0).send(sendThis, timestamp);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidMidiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
