package model.pluginmanager;

import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.swing.JMenu;

import midiengine.MidiEngine;
import model.graph.Module;
import plugin.Plugin;
import pluginhost.PluginHost;
import controller.Controller;
import defaultplugin.DefaultPlugin;
import engine.Engine;

public class PluginManager {
	
	private Controller controller;
	private LinkedList<Plugin> plugins;
	public PluginManager(Controller c){
		this.controller = c;
		this.plugins = new LinkedList<Plugin>();
		System.out.println("const");
		this.loadPlugins();
	}
	
	/**
	 * adds 
	 * @param menu
	 */
	public LinkedList<Plugin> getPluginList(){
		
		return this.plugins;
	}
	
	public void loadPlugins(){
		Class<? extends DefaultPlugin> x = DefaultPlugin.class;
		try{
			Method method = DefaultPlugin.class.getMethod("getInstance",PluginHost.class);
			Plugin p = (Plugin) method.invoke(null,new Module(Engine.load()));
		this.plugins.add(p);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
