package controller.history;

import controller.interactivepane.InteractiveController;

public abstract class UserAction {
	protected UserActionManager manager;
	protected InteractiveController controller;
	public UserAction(UserActionManager m){
		this.manager = m;
		this.controller = m.getController();
	}
	public abstract void undo();

	public abstract void execute();
}
