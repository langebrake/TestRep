package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import defaults.MidiIO;
import engine.Stringer;
import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveModule;
import model.graph.Module;

public class MidiGraph implements Serializable, Iterable<Module>, Cloneable {
	private transient LinkedList<Module> nodes;

	public MidiGraph() {
		this.nodes = new LinkedList<Module>();
	}

	public void add(Module m) {
		this.nodes.add(m);
	}

	public Module remove(Module m) {
		if (this.nodes.remove(m)) {
			return m;
		} else {
			return null;
		}
	}

	@Override
	public Iterator<Module> iterator() {
		return nodes.iterator();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		String stringer = Stringer.getString();
		System.out.println(stringer + "GRAPH_START");
		out.writeInt(nodes.size());
		for (Module m : nodes) {
			out.writeObject(m);
		}
		System.out.println(stringer + "GRAPH_END");
		Stringer.minimize();
	}

	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		int max = in.readInt();
		nodes = new LinkedList<Module>();
		for (int i = 0; i < max; i++) {
			nodes.add((Module) in.readObject());
		}
	}

	public MidiGraph clone() {
		modMap = new HashMap<Module, Module>();
		MidiGraph mg = new MidiGraph();
		for (Module m : this.nodes) {
			cloneRecursive(mg, m);
		}
		return mg;
	}

	private HashMap<Module, Module> modMap;

	private Module cloneRecursive(MidiGraph cloneIntoThis, Module cloneThis) {
		Module cMod = modMap.get(cloneThis);
		if (cMod == null) {

			cMod = cloneThis.clone();
			modMap.put(cloneThis, cMod);
			cloneIntoThis.add(cMod);
			for (MidiIO mIO : cMod.getOutputs()) {
				if (!mIO.hasOutput()) {
					MidiIO output = cloneThis.getOuput(mIO.getId());
					if (output.hasOutput()) {
						MidiIO otherEnd = output.getOutput();
						// TODO: ClassCast Exception handling(should not happen
						// by design)!
						Module otherParent = (Module) otherEnd.getParent();
						Module otherModule;
						otherModule = modMap.get(otherParent);
						if (otherModule == null) {
							otherModule = cloneRecursive(cloneIntoThis, otherParent);
						}

						otherEnd = otherModule.getInput(otherEnd.getId());
						mIO.setOutput(otherEnd);
						otherEnd.setInput(mIO);
					}
				}
			}
		}

		return cMod;
	}

}
