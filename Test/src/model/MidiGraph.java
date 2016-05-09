package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import model.graph.Module;

public class MidiGraph implements Serializable,Iterable<Module>{
	private HashSet<Module> nodes;
	
	public MidiGraph(){
		
	}
	
	public void add(Module m){
		this.nodes.add(m);
	}
	
	public void remove(Module m){
		this.nodes.remove(m);
	}

	@Override
	public Iterator<Module> iterator() {
		return nodes.iterator();
	}
	

	
}
