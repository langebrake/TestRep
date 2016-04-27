package controller.history.actions;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveShape;

import java.util.LinkedList;

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
		}
		for(InteractiveShape c:shapes){
			controller.getPane().add(c);
			
		}
	}

	@Override
	public void execute() {
		//TODO: implement Disconnecting etc.
		for(InteractiveComponent c: components){
			controller.getPane().remove(c);
		}
		for(InteractiveShape c:shapes){
			controller.getPane().remove(c);
			
		}

	}

}
