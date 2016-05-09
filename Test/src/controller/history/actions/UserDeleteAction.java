package controller.history.actions;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointHost;
import gui.interactivepane.CablePointPanel;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.InteractiveShape;

import java.awt.Component;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JComponent;

import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;

public class UserDeleteAction extends UserAction {
	final LinkedList<InteractiveComponent> components;
	final LinkedList<InteractiveShape> shapes;
	
	public UserDeleteAction(InteractiveController sourceController, LinkedList<InteractiveComponent> components, LinkedList<InteractiveShape> shapes){
		super(sourceController);
		this.components = components;
		this.shapes = shapes;
	}
	
	@Override
	public void undo() {
		//Implement Connecting etc. of modules
		for(InteractiveComponent c: components){
			if(c.reopen()){
				
				LinkedList<CablePointHost> cablePointHosts = getRecursive(c,CablePointHost.class);
				for(CablePointHost cablePointHost: cablePointHosts){
					for(CablePoint cablePoint:cablePointHost.getCablePoints()){
					if(cablePoint.isConnected()){
						InteractiveCable tmpCable = cablePoint.getCable();
						for(CablePoint cableResurrect: tmpCable.getCablePoints()){
							if(cableResurrect == cablePoint){
								cableResurrect.tmpDisconnect(false);
								continue;
							}
							//cableResurrect.getHost().forceExistence(cableResurrect);
							cableResurrect.setCable(tmpCable);
							
						}
							if(!c.getController().getPane().getShapes().contains(tmpCable)){
								c.getController().getPane().add(tmpCable);
						}
					}
				}
				}
				if(!c.getController().getPane().isAncestorOf(c))
					c.getController().getPane().add(c);
				
				if(c instanceof InteractiveModule)
					c.getController().getGraph().add(((InteractiveModule) c).getModule());
			}
		}
		
		for(InteractiveShape c:shapes){
			//TODO: Cable Connections restored on both View and Model!!
			if(c instanceof InteractiveCable){
				((InteractiveCable) c).getSource().setCable((InteractiveCable) c);
				((InteractiveCable) c).getDestination().setCable((InteractiveCable) c);
			}
			
			
			c.getController().getPane().add(c);
		}

	}

	@Override
	public void execute() {
		for(InteractiveComponent c: components){
			if(c.close()){
				LinkedList<CablePointHost> cablePointHosts = getRecursive(c,CablePointHost.class);
					for(CablePointHost cablePointHost: cablePointHosts){	
						for(CablePoint cablePoint:cablePointHost.getCablePoints()){
							if(cablePoint.isConnected()){
							
							InteractiveCable tmpCable = cablePoint.getCable();
							
								c.getController().getPane().remove(tmpCable);
							for(CablePoint cableErase: tmpCable.getCablePoints()){
								if(cableErase == cablePoint){
									cableErase.tmpDisconnect(true);
									continue; //don't remove Reference on components own cablepoint, otherwise undo is impossible!
								}
								cableErase.disconnect();
							}
							this.shapes.remove(tmpCable);
						}
						
					}
				}
				c.getController().getPane().remove(c);
				if(c instanceof InteractiveModule){
					c.getController().getGraph().remove(((InteractiveModule) c).getModule());
				}
			}
		}
		for(InteractiveShape c:shapes){
			//TODO: implement Disconnecting etc. on View and Model!
			if(c instanceof InteractiveCable){
				((InteractiveCable) c).getSource().disconnect();
				((InteractiveCable) c).getDestination().disconnect();
			}
			
			c.getController().getPane().remove(c);
			
		}

	}
	
	private<T> LinkedList<T> getRecursive(JComponent c, Class<?> contained){
		LinkedList<T> tmp = new LinkedList();
		if(contained.isInstance(c)){
			tmp.add( (T)c);
		}
		for(Component component:c.getComponents()){
			if(component instanceof JComponent){
				
				tmp.addAll(getRecursive((JComponent) component,contained));
			}
		}
		return tmp;
	}

}
