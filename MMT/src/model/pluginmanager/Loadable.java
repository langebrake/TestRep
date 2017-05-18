package model.pluginmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.jar.Attributes;

import plugin.Plugin;
import pluginhost.PluginHostCommunicator;
import pluginhost.PluginHostCommunicator;

public class Loadable implements PluginHierarchyElement {
	private Method getInstance;
	private Attributes attributes;
	private Class<? extends Plugin> pClass;

	public Loadable(Class<? extends Plugin> pClass, Attributes attributes) {
		this.pClass = pClass;
		Method getInstance = null;
		try {
			getInstance = pClass.getDeclaredMethod("getInstance",
					PluginHostCommunicator.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public String getDescription() {
		return this.attributes.getValue("Description");
	}

	public Plugin getInstance(PluginHostCommunicator h) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return (Plugin) this.getInstance.invoke(null, h);
	}

	public Method getMethod() {
		return this.getInstance;
	}

	public Class<? extends Plugin> getPluginClass() {
		return pClass;
	}
}
