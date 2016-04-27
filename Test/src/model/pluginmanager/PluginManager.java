package model.pluginmanager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JMenu;
import javax.swing.filechooser.FileFilter;

import midiengine.MidiEngine;
import model.graph.Module;
import plugin.Plugin;
import pluginhost.PluginHost;
import controller.Controller;
//import defaultplugin.DefaultPlugin;
import engine.Engine;

public class PluginManager {
	
	private Controller controller;
	private static LinkedList<PluginHierarchyElement> plugins;
	
	/**
	 * adds 
	 * @param menu
	 */
	public static LinkedList<PluginHierarchyElement> getPluginList(){
		return plugins;
	}
	
	private static void loadPlugins(File dir,LinkedList<PluginHierarchyElement> appendList) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		for(File file: dir.listFiles()){
			if(file.isDirectory()) {
				Subgroup s = new Subgroup(file.getName());
				appendList.add(s);
				loadPlugins(file,s);
			}else if(file.getName().toLowerCase().endsWith(".jar")){
				String path = file.getPath();
				JarFile jar = new JarFile(path);
				Enumeration<JarEntry> en = jar.entries();
				
				URL[] urls = { new URL("jar:file:" + path +"!/") };
				URLClassLoader cl = URLClassLoader.newInstance(urls);
				
				while (en.hasMoreElements()) {
				    JarEntry je = en.nextElement();
				    if(je.isDirectory() || !je.getName().endsWith(".class")){
				        continue;
				    }
				    
				    String className = je.getName().substring(0,je.getName().length()-6);
				    className = className.replace('/', '.');
				    //System.out.println(className);
				    Class<?> c = cl.loadClass(className);
				    if(c.getSuperclass() == Plugin.class){
				    	Method m = c.getMethod("getInstance",PluginHost.class);
				    	appendList.add(new Loadable(m,jar.getManifest().getMainAttributes()));
				    	
				    }else{
				    	
				    }
				}
				jar.close();
				
			}
			
			
			
		
		
		}
	}
	public static void loadPlugins() throws Exception{
		plugins = new LinkedList<PluginHierarchyElement>();
		File f = new File("./plugin");
		loadPlugins(f,plugins);
		for(PluginHierarchyElement m:plugins){
			System.out.println(m.getName());
			if(m.isLoadable()){
				PluginHost module = new Module(Engine.load());
				Plugin p = ((Loadable)m).getInstance(module);
			}
			
		}
	}
}
