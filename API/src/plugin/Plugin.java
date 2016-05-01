package plugin;

import java.io.Serializable;

import guiinterface.SizeableComponent;

import javax.swing.JComponent;
import javax.swing.JFrame;

import plugin.events.*;
import plugin.exceptions.*;
import pluginhost.events.*;
import pluginhost.PluginHost;


public abstract class Plugin implements Serializable{
	
	private PluginHost host;
	
	public Plugin(PluginHost host){
		this.host = host;
	}
	
	public static Plugin getInstance(PluginHost host){
		return null;
	}
	/**
	 * This method returns the plugins name
	 * @return static String as name
	 */
	public abstract String getPluginName();
	public abstract JComponent getMinimizedView();
	public abstract JFrame getFullView();

	public abstract String getDisplayName();
	public abstract void setDisplayName();
	public PluginHost getPluginHost(){
		return this.host;
	}
	public abstract int getMaxInputs();
	public abstract int getMaxOutputs();
	public abstract int getMinInputs();
	public abstract int getMinOutputs();
	public abstract void notify(HostEvent e);
	public abstract void load();
	public abstract void close();

	public abstract void reOpen();
	
}
