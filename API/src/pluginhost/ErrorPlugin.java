package pluginhost;

import javax.swing.JComponent;

import defaults.DefaultView;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.HostEvent;

public class ErrorPlugin extends Plugin{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8803876988642035075L;

	private String missing;
	private int ins,outs;
	public ErrorPlugin(PluginHostCommunicator host, String missing,int ins, int outs) {
		super(host, "Missing Plugin", ins, ins, outs, outs);
		this.ins = ins;
		this.outs = outs;
		this.missing = missing;
		// TODO Auto-generated constructor stub
	}
	public static Plugin getInstance(PluginHostCommunicator host){
		return new ErrorPlugin(host,"unknown error",1,1);
	};
	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent getFullView() {
		// TODO Auto-generated method stub
		return new DefaultView(missing);
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
		return true;
	}

	@Override
	public boolean reOpen() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Plugin clone(PluginHostCommunicator newHost) {
		// TODO Auto-generated method stub
		return new ErrorPlugin(newHost,missing,ins,outs);
	}

}
