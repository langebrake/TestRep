package testsender_v01;
import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import javax.swing.JComponent;

import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;


public class TestSenderPlugin extends Plugin {

	
	private static final int MAXINPUTS = 0;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "Test Sender";
	private transient MinView fullView;
	private transient MinView minView;
	
	public static TestSenderPlugin getInstance(PluginHost host){
		return new TestSenderPlugin(host);
	}
	
	public TestSenderPlugin(PluginHost host) {
		super(host);
	}

	@Override
	public String getPluginName() {
		return NAME;
	}

	@Override
	public JComponent getMinimizedView() {
		return this.minView;
	}

	@Override
	public Component getFullView() {
		return this.fullView;
	}

	@Override
	public String getDisplayName() {
		return NAME;
	}

	@Override
	public void setDisplayName() {

	}

	@Override
	public int getMaxInputs() {
		return MAXINPUTS;
	}

	@Override
	public int getMaxOutputs() {
		return MAXOUTPUTS;
	}

	@Override
	public int getMinInputs() {
		return MININPUTS;
	}

	@Override
	public int getMinOutputs() {
		return MINOUTPUTS;
	}

	@Override
	public void notify(HostEvent e) {
	}

	@Override
	public void load() {
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
	public Plugin clone() {
		TestSenderPlugin t = new TestSenderPlugin(Plugin.waitForHost());
		t.initPlugin();
		return t;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.setPluginHost(Plugin.waitForHost());
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
}
