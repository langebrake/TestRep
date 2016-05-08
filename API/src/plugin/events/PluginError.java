package plugin.events;

public class PluginError extends PluginEvent {
	private String msg;
	public PluginError(String msg){
		this.msg = msg;
	}
	
	public String getMessage(){
		return this.msg;
	}
}
