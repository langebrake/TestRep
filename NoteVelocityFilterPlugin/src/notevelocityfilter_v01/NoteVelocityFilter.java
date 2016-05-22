package notevelocityfilter_v01;

import javax.swing.JComponent;

import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class NoteVelocityFilter extends Plugin {

	public NoteVelocityFilter(PluginHost host, String name, int minInputs,
			int maxInputs, int minOutputs, int maxOutputs) {
		super(host, name, minInputs, maxInputs, minOutputs, maxOutputs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent getFullView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notify(HostEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean reOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Plugin clone(PluginHost newHost) {
		// TODO Auto-generated method stub
		return null;
	}

}
