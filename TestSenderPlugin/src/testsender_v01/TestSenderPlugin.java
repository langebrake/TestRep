package testsender_v01;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class TestSenderPlugin extends Plugin implements ActionListener{

	
	private static final int MAXINPUTS = 0;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "Test Sender";
	private transient MinView fullView;
	private transient MinView minView;
	int status,channel, data1,data2;
	
	public static TestSenderPlugin getInstance(PluginHost host){
		return new TestSenderPlugin(host);
	}
	
	public TestSenderPlugin(PluginHost host) {
		super(host,NAME,MININPUTS,MAXINPUTS,MINOUTPUTS,MAXOUTPUTS);
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
		this.status = 0b1001;
		this.channel = 0;
		this.data1 = 60;
		this.data2 = 100;
		this.initPlugin();

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
	public Plugin clone(PluginHost host) {
		TestSenderPlugin t = new TestSenderPlugin(host);
		t.initPlugin();
		return t;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.initPlugin();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}

	
	private void initPlugin(){
		this.minView = new MinView(this);
		this.fullView = new MinView(this);
	}

	public void send(int parseInt) {
		ShortMessage msg = new ShortMessage();
		try {
			msg.setMessage(ShortMessage.NOTE_ON, 0, parseInt, 93);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getPluginHost().getOuput(0).send(msg, -1);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		ShortMessage msg = new ShortMessage();
		
		try {
			status = minView.status();
			channel = minView.channel();
			data1 = minView.data1();
			data2 = minView.data2();
			msg.setMessage(status<<4,channel,data1,data2);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.getPluginHost().getOuput(0).send(msg, -1);
		
	}
}
