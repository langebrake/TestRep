package controller.history;

import java.io.Serializable;

import controller.interactivepane.InteractiveController;

public abstract class UserAction implements Serializable {
	protected UserActionManager manager;
	protected InteractiveController controller;
	public UserAction(InteractiveController controller){
		this.controller = controller;
		this.manager = controller.getActionManager();
	}
	public abstract void undo();

	public abstract void execute();
}
