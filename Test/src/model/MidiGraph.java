package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import model.graph.Module;

public class MidiGraph implements Serializable,Iterable<Module>{
	private HashSet<Module> nodes;
	
	public MidiGraph(){
		this.nodes = new HashSet<Module>();
	}
	
	public void add(Module m){
		this.nodes.add(m);
	}
	
	public Module remove(Module m){
		if(this.nodes.remove(m)){
			return m;
		} else {
			return null;
		}
	}

	@Override
	public Iterator<Module> iterator() {
		return nodes.iterator();
	}
	

	
}
