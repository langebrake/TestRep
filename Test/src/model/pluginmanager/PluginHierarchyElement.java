package model.pluginmanager;

public interface PluginHierarchyElement {
	public boolean isSubgroup();
	public boolean isLoadable();
	public String getName();
}
