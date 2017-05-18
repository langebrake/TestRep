package plugin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.JComponent;

import pluginhost.events.*;
import pluginhost.PluginHostCommunicator;
import pluginhost.PluginHostCommunicator;

public abstract class Plugin implements Serializable, Cloneable {

	private transient PluginHostCommunicator host;
	public static transient Object lock = new Object();
	private final int minInputs, maxInputs, minOutputs, maxOutputs;
	private final String name;

	public Plugin(PluginHostCommunicator host, String name, int minInputs, int maxInputs, int minOutputs, int maxOutputs) {
		this.minInputs = minInputs;
		this.minOutputs = minOutputs;
		this.maxInputs = maxInputs;
		this.maxOutputs = maxOutputs;
		this.name = name;
		this.host = host;
	}

	public static Plugin getInstance(PluginHostCommunicator host) {
		return null;
	}

	public final void setPluginHost(PluginHostCommunicator host) {
		this.host = host;
	}



	public static PluginHostCommunicator waiter = null;

	public final static PluginHostCommunicator waitForHost() {
		PluginHostCommunicator tmp = waiter;
		waiter = null;
		return tmp;
	}

	public final PluginHostCommunicator getPluginHost() {
		return this.host;
	}

	public final int getMaxInputs() {
		return this.maxInputs;
	}

	public final int getMaxOutputs() {
		return this.maxOutputs;
	}

	public final int getMinInputs() {
		return this.minInputs;
	}

	public final int getMinOutputs() {
		return this.minOutputs;
	}

	public final String getPluginName() {
		return this.name;
	}

	public abstract JComponent getMinimizedView();

	public abstract JComponent getFullView();

	public abstract void notify(HostEvent e);

	public abstract void load();

	public abstract boolean close();

	public abstract boolean reOpen();

	public final Plugin clone() {
		return this.clone(Plugin.waitForHost());
	}

	public abstract Plugin clone(PluginHostCommunicator newHost);

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		this.host = Plugin.waitForHost();
		in.defaultReadObject();
	}

}
