package controller.interactivepane;

import defaults.MidiIO;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.Vector;
import model.MidiGraph;
import model.graph.Module;

public class Populator {
	
	public static InteractivePane populateWith(InteractiveController c, MidiGraph graph){
		InteractivePane pane = c.getPane();
		for(Module m:graph){
			pane.add(new InteractiveModule(new Vector(0,0), m, c));
			for(MidiIO out:m.getOutputs()){
				
			}
		}
		
		
		
		return null;
		
	}
}
