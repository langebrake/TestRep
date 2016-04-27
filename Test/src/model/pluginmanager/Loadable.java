package model.pluginmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import plugin.Plugin;
import pluginhost.PluginHost;

public class Loadable implements PluginHierarchyElement {
	private Method getInstance;
	private Attributes attributes;
	
	public Loadable(Method getInstance, Attributes attributes){
		this.getInstance = getInstance;
		this.attributes = attributes;
	}
	@Override
	public boolean isSubgroup() {
		return false;
	}

	@Override
	public boolean isLoadable() {
		return true;
	}

	@Override
	public String getName() {
		return this.attributes.getValue("Name");
	}
	public String getDescription(){
		return this.attributes.getValue("Description");
	}
	public Plugin getInstance(PluginHost h) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (Plugin) this.getInstance.invoke(null, h);
	}
}
