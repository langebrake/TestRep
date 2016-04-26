package model.pluginmanager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JMenu;

import midiengine.MidiEngine;
import model.graph.Module;
import plugin.Plugin;
import pluginhost.PluginHost;
import controller.Controller;
//import defaultplugin.DefaultPlugin;
import engine.Engine;

public class PluginManager {
	
	private Controller controller;
	private static LinkedList<Plugin> plugins;
	
	/**
	 * adds 
	 * @param menu
	 */
	public static LinkedList<Plugin> getPluginList(){
		return plugins;
	}
	
	public static void loadPlugins() throws Exception{
		plugins = new LinkedList<Plugin>();
		File f = new File("./plugin");
		
		JarFile jar = new JarFile(f.listFiles()[0].getPath());
		Enumeration<JarEntry> en = jar.entries();
		
		URL[] urls = { new URL("jar:file:" + f.listFiles()[0].getPath() +"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		while (en.hasMoreElements()) {
		    JarEntry je = en.nextElement();
		    if(je.isDirectory() || !je.getName().endsWith(".class")){
		        continue;
		    }
		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    //System.out.println(className);
		    Class c = cl.loadClass(className);
		    if(c.getSuperclass() == Plugin.class){
		    	Method m = c.getMethod("getInstance",PluginHost.class);
		    	PluginHost module = new Module(Engine.load());
		    	Plugin p = (Plugin) m.invoke(null, module);
		    	module.setPlugin(p);
		    	p.getFullView().setVisible(true);
		    	System.out.println(p.getPluginName());
		    	System.out.println(module.getOutputCount());
		    	
		    }
		}
	}
}
