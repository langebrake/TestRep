package controller.history.actions;

import gui.InteractiveComponent;
import gui.InteractiveShape;

import java.util.LinkedList;

import controller.InteractiveController;
import controller.history.UserAction;
import controller.history.UserActionManager;

public class UserDeleteAction implements UserAction {
	final LinkedList<InteractiveComponent> components;
	final LinkedList<InteractiveShape> shapes;
	private UserActionManager manager;
	private InteractiveController controller;
	public UserDeleteAction(UserActionManager manager, LinkedList<InteractiveComponent> components, LinkedList<InteractiveShape> shapes){
		this.components = components;
		this.shapes = shapes;
		this.manager = manager;
		this.controller = manager.getController();
	}
	
	@Override
	public void undo() {
		for(InteractiveComponent c: components){
			controller.getPane().add(c);
		}
		for(InteractiveShape c:shapes){
			controller.getPane().add(c);
			
		}
	}

	@Override
	public void execute() {
		for(InteractiveComponent c: components){
			controller.getPane().remove(c);
		}
		for(InteractiveShape c:shapes){
			controller.getPane().remove(c);
			
		}

	}

}
