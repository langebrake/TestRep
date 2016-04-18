import java.util.LinkedList;

import javax.swing.JComponent;

import plugin.Plugin;
import pluginhost.events.*;
import pluginhost.PluginHost;
import defaults.DefaultView;
import defaults.MidiIO;



public class DefaultPlugin extends Plugin{
	private static final int MAXINPUTS = 1;
	private static final int MAXOUTPUTS = 1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "DummyPlugin";
	public static Plugin getInstance(PluginHost host){
		return new DefaultPlugin(host);
	}
	public DefaultPlugin(PluginHost host){
		super(host);
	}
	@Override
	public void notify(HostEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return new DefaultView(NAME);
	}
	@Override
	public JComponent getFullView() {
		// TODO Auto-generated method stub
		return new DefaultView(NAME);
	}
	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return NAME;
	}
	@Override
	public int getMaxInputs() {
		// TODO Auto-generated method stub
		return MAXINPUTS;
	}
	@Override
	public int getMaxOutputs() {
		// TODO Auto-generated method stub
		return MAXOUTPUTS;
	}
	@Override
	public void load() {
		PluginHost host = this.getPluginHost();
		MidiIO input = host.getInput(0);
		MidiIO output = host.getOuput(0);
		input.setOutput(output);
		
	}
	@Override
	public void close() {
		PluginHost host = this.getPluginHost();
		MidiIO input = host.getInput(0);
		MidiIO output = host.getOuput(0);
		input.disconnectOutput();
		
	}
	@Override
	public int getMinInputs() {
		// TODO Auto-generated method stub
		return MININPUTS;
	}
	@Override
	public int getMinOutputs() {
		// TODO Auto-generated method stub
		return MINOUTPUTS;
	}
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return NAME;
	}


}
