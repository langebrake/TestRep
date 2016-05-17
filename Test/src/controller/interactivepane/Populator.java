package controller.interactivepane;

import java.util.HashMap;

import pluginhost.PluginHost;
import stdlib.grouping.Grouping;
import defaults.MidiIO;
import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.Vector;
import model.MidiGraph;
import model.graph.Module;

public class Populator {
	private static HashMap<Module, InteractiveModule> modMap = new HashMap<Module, InteractiveModule>();
	private static InteractivePane pane;
	private static InteractiveController c;
	public static InteractivePane populateWith(InteractiveController c, MidiGraph graph){
		pane = c.getPane();
		Populator.c = c;
		for(Module mod:graph){
			
			addRecursive(mod);
		}
		modMap.clear();
		return pane;
	}
	
	private static InteractiveModule addRecursive(Module mod){
		
		InteractiveModule iMod = modMap.get(mod);
		if(iMod == null){
			iMod = new InteractiveModule(mod.origin, mod, c);
			if(mod.getPlugin() instanceof Grouping){
				((Grouping)mod.getPlugin()).setParentController(c);
			}
			modMap.put(mod, iMod);
			pane.add(iMod);
			for(CablePoint p:iMod.getCablePoints(CablePointType.OUTPUT)){
				if(!p.isConnected()){
					MidiIO output = mod.getOuput(p.getIndex());
					if(output.hasOutput()){
						MidiIO otherEnd = output.getOutput();
						//TODO: ClassCast Exception handling(should not happen by design)!
						Module otherParent = (Module) otherEnd.getParent();
						InteractiveModule otherModule;
						otherModule = modMap.get(otherParent);
						if(otherModule == null){
							otherModule = addRecursive(otherParent);
						}
						CablePoint otherEndCp = otherModule.getCablePoint(CablePointType.INPUT,otherEnd.getId());
						InteractiveCable newCable = new InteractiveCable(c);
						newCable.setSource(p);
						newCable.setDestination(otherEndCp);
						p.setCable(newCable);
						otherEndCp.setCable(newCable);
						pane.add(newCable);
						
					}
				}
			}
//			for(MidiIO out:mod.getOutputs()){
//				iMod.getCablePoint(CablePointType.OUTPUT,out.getId());
//			}
		}
		return iMod;
		
	}
}
