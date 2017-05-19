package pluginhost;

import dev.hostevents.HostEvent;
import dev.pluginevents.PluginEvent;

public interface PluginStateChangedListener {
	public void listen(HostEvent e);

	public void listen(PluginEvent e);
}
