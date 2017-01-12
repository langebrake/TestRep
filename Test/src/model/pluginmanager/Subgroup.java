package model.pluginmanager;

import java.util.LinkedList;

public class Subgroup extends LinkedList<PluginHierarchyElement> implements PluginHierarchyElement {
	private String name;

	public Subgroup(String name) {
		this.name = name;
	}

	@Override
	public boolean isSubgroup() {
		return true;
	}

	@Override
	public boolean isLoadable() {
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
