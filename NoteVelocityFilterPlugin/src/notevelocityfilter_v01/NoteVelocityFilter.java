package notevelocityfilter_v01;

import javax.swing.JComponent;

import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;

public class NoteVelocityFilter extends Plugin {

	public NoteVelocityFilter(PluginHostCommunicator host, String name, int minInputs, int maxInputs, int minOutputs,
			int maxOutputs) {
		super(host, name, minInputs, maxInputs, minOutputs, maxOutputs);
		// TODO Auto-generated constructor stub
	}
	public static Plugin getInstance(PluginHostCommunicator host){
		return new NoteVelocityFilter(host, "NOT USABLE YET",0,0,0,0);
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
	public Plugin clone(PluginHostCommunicator newHost) {
		// TODO Auto-generated method stub
		return null;
	}

}
