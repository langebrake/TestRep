package plugin.events;

import pluginhost.PluginHost;

public class PluginError extends PluginEvent {
	private PluginHost sourceHost;
	private Exception exception;
	public PluginError(Exception exception, PluginHost sourceHost){
		this.sourceHost = sourceHost;
		this.exception = exception;
	}
	public PluginHost getSourceHost(){
		return this.sourceHost;
	}
	
	public Exception getException(){
		return this.exception;
	}
}
