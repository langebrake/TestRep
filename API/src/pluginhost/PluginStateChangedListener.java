package pluginhost;

import plugin.events.PluginEvent;
import pluginhost.events.HostEvent;

public interface PluginStateChangedListener {
	public void listen(HostEvent e);
	public void listen(PluginEvent e);
}
