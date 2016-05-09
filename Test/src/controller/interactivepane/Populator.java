package controller.interactivepane;

import java.util.HashMap;

import defaults.MidiIO;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.Vector;
import model.MidiGraph;
import model.graph.Module;

public class Populator {
	private static HashMap<Module, InteractiveModule> modMap = new HashMap<Module, InteractiveModule>();
	
	public static InteractivePane populateWith(InteractiveController c, MidiGraph graph){
		InteractivePane pane = c.getPane();
		for(Module mod:graph){
			InteractiveModule iMod = modMap.get(mod);
			if(iMod == null){
				iMod = new InteractiveModule(new Vector(0,0), mod, c);
				modMap.put(mod, iMod);
				pane.add(iMod);
			}
			
			for(MidiIO out:mod.getOutputs()){
				
			}
		}
		
		
		
		return pane;
		
	}
}
