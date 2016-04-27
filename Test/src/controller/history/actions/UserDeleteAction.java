package controller.history.actions;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointHost;
import gui.interactivepane.CablePointPanel;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
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
	public UserDeleteAction(UserActionManager manager, LinkedList<InteractiveComponent> components, LinkedList<InteractiveShape> shapes){
		super(manager);
		this.components = components;
		this.shapes = shapes;
	}
	
	@Override
	public void undo() {
		//Implement Connecting etc. of modules
		for(InteractiveComponent c: components){
			controller.getPane().add(c);
			LinkedList<CablePoint> cablePoints = getRecursive(c,CablePoint.class);
			for(CablePoint cablePoint: cablePoints){
				if(cablePoint.isConnected()){
					InteractiveCable tmpCable = cablePoint.getCable();
					for(CablePoint cableResurrect: tmpCable.getCablePoints()){
						if(cableResurrect == cablePoint){
							continue; //don't remove Reference on components own cablepoint, otherwise undo is impossible!
						}
						cableResurrect.setCable(tmpCable);
					}
					
					if(!controller.getPane().getShapes().contains(tmpCable)){
						controller.getPane().add(tmpCable);
					}
				}
				
			}
		}
		
		for(InteractiveShape c:shapes){
			//TODO: Cable Connections restored on both View and Model!!
			if(c instanceof InteractiveCable){
				((InteractiveCable) c).getSource().setCable((InteractiveCable) c);
				((InteractiveCable) c).getDestination().setCable((InteractiveCable) c);
			}
			
			
			controller.getPane().add(c);
		}

	}

	@Override
	public void execute() {
		
		for(InteractiveComponent c: components){
			LinkedList<CablePoint> cablePoints = getRecursive(c,CablePoint.class);
				for(CablePoint cablePoint: cablePoints){	
					
					if(cablePoint.isConnected()){
						InteractiveCable tmpCable = cablePoint.getCable();
						controller.getPane().remove(tmpCable);
						for(CablePoint cableErase: tmpCable.getCablePoints()){
							if(cableErase == cablePoint){
								continue; //don't remove Reference on components own cablepoint, otherwise undo is impossible!
							}
							cableErase.disconnect();
						}
						this.shapes.remove(tmpCable);
					}
					
				}
			controller.getPane().remove(c);
		}
		for(InteractiveShape c:shapes){
			//TODO: implement Disconnecting etc. on View and Model!
			if(c instanceof InteractiveCable){
				((InteractiveCable) c).getSource().disconnect();
				((InteractiveCable) c).getDestination().disconnect();
			}
			controller.getPane().remove(c);
			
		}

	}
	
	private<T> LinkedList<T> getRecursive(JComponent c, Class<?> contained){
		LinkedList<T> tmp = new LinkedList();
		
		for(Component component:c.getComponents()){
			if(component instanceof JComponent){
				if(contained.isInstance(component)){
					tmp.add( (T)component);
				}
				tmp.addAll(getRecursive((JComponent) component,contained));
			}
		}
		return tmp;
	}

}
