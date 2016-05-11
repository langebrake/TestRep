package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import model.graph.Module;

public class MidiGraph implements Serializable,Iterable<Module>{
	private transient LinkedList<Module> nodes;
	
	public MidiGraph(){
		this.nodes = new LinkedList<Module>();
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
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeInt(nodes.size());
		for(Module m:nodes){
			out.writeObject(m);
		}
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		int max = in.readInt();
		nodes = new LinkedList<Module>();
		for(int i = 0;i<max;i++){
			nodes.add((Module) in.readObject());
		}
	}

	
}
