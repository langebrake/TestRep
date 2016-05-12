package controller.history.actions;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.Vector;

import java.util.HashMap;
import java.util.LinkedList;

import model.MidiGraph;
import model.graph.Module;
import controller.history.UserAction;
import controller.interactivepane.InteractiveController;
import controller.interactivepane.Populator;
import defaults.MidiIO;

public class UserPasteAction extends UserAction {
	
	private LinkedList<InteractiveComponent> components;
	private LinkedList<InteractiveCable> cables;
	private Vector offset;
	private LinkedList<InteractiveComponent> clipboard;
	private LinkedList<InteractiveCable> cableClipboard;
	public UserPasteAction(InteractiveController controller, LinkedList<InteractiveComponent> clipboard, LinkedList<InteractiveCable> cableClipboard, Vector offset) {
		super(controller); 
		this.clipboard = clipboard;
		this.components = new LinkedList<InteractiveComponent>();
		cables = new LinkedList<InteractiveCable>();
		this.offset = offset;
		this.cableClipboard = cableClipboard;
		this.populate();
		
		
	}

	@Override
	public void undo() {
		for(InteractiveCable c:cables){
			controller.getPane().remove(c);
		}
		for(InteractiveComponent c:components){
			c.close();
			controller.remove(c);
		}

	}
	
	private boolean firstTime = true;

	@Override
	public void execute() {
		for(InteractiveComponent c:components){
			if(firstTime){
				firstTime = false;
			} else {
				c.reopen();
			}
			controller.add(c);
		}
		for(InteractiveCable c:cables){
			controller.getPane().add(c);
		}
	}
	
	private HashMap<InteractiveModule, InteractiveModule> modMap = new HashMap<InteractiveModule, InteractiveModule>();
	
	private  void populate(){
		for(InteractiveComponent mod:clipboard){
			if(mod instanceof InteractiveModule){
				InteractiveModule cMod = ((InteractiveModule) mod).cloneTo(offset, controller);
				modMap.put((InteractiveModule) mod, cMod);
				this.components.add(cMod);
			}
		};
		for(InteractiveCable cable:this.cableClipboard){
			
			CablePoint source = cable.getSource();
			CablePoint dest = cable.getDestination();
			InteractiveModule sourceMapMod = modMap.get(source.getHost());
			InteractiveModule destMapMod = modMap.get(dest.getHost());
			
			System.out.println("OOOOKKKKK"+source);
			System.out.println("OOOOKKKKK"+dest);
			System.out.println("OOOOKKKKK"+sourceMapMod);
			System.out.println("OOOOKKKKK"+destMapMod);
			CablePoint cSource = sourceMapMod.getCablePoint(source.getType(), source.getIndex());
			CablePoint cDest = destMapMod.getCablePoint(dest.getType(), dest.getIndex());
			InteractiveCable cCable = new InteractiveCable(cSource, cDest, controller);
			cSource.setCable(cCable);
			cDest.setCable(cCable);
			this.cables.add(cCable);
		};
	}
	
	private InteractiveModule addRecursive(InteractiveModule mod){
		
		InteractiveModule cMod = modMap.get(mod);
		if(cMod == null){
			cMod = mod.cloneTo(offset, controller);
			
			modMap.put(mod, cMod);
			this.components.add(cMod);
			
			for(InteractiveCable c:this.cableClipboard){
				
				
				
			}
			
			for(CablePoint p:cMod.getCablePoints(CablePointType.OUTPUT)){
				if(!p.isConnected()){
					MidiIO output = mod.getModule().getOuput(p.getIndex());
					System.out.println(mod.getName()+" -- "+mod.getModule().getOuput(p.getIndex()).getInput());
					if(output.hasOutput()){
						
						MidiIO otherEnd = output.getOutput();
						//TODO: ClassCast Exception handling(should not happen by design)!
						Module otherParent = (Module) otherEnd.getParent();
						InteractiveModule otherModule = findByModule(otherParent);
						if(otherModule != null){
							InteractiveModule othercModule;
							othercModule = modMap.get(otherModule);
							if(othercModule == null){
								othercModule = addRecursive(otherModule);
							}
							CablePoint otherEndCp = othercModule.getCablePoint(CablePointType.INPUT,otherEnd.getId());
							InteractiveCable newCable = new InteractiveCable(controller);
							newCable.setSource(p);
							newCable.setDestination(otherEndCp);
							p.setCable(newCable);
							otherEndCp.setCable(newCable);
							cables.add(newCable);
						}
					}
				}
			}
		}
		return cMod;
	}
	
	private InteractiveModule findByModule(Module m){
		for(InteractiveComponent c:clipboard){
			if(c instanceof InteractiveModule){
				if(((InteractiveModule) c).getModule() == m){
					return (InteractiveModule) c;
				}
			}
		}
		return null;
	}

}
