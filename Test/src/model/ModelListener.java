package model;

import model.graph.Module;

public interface ModelListener {
	public void moduleAdded(Module m);
	public void moduleRemoved(Module m);
	public void moduleRenamed(Module m);
}
