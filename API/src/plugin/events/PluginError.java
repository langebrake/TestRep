package plugin.events;

import pluginhost.PluginHost;

public class PluginError extends PluginEvent {
	private String msg;
	private PluginHost sourceHost;
	public PluginError(String msg, PluginHost sourceHost){
		this.msg = msg;
		this.sourceHost = sourceHost;
	}
	
	public String getMessage(){
		return this.msg;
	}
	
	public PluginHost getSourceHost(){
		return this.sourceHost;
	}
}
