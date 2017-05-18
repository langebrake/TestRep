package controller.history;

import java.io.Serializable;

import controller.interactivepane.InteractiveController;

public abstract class UserAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1481321030288238515L;
	protected InteractiveController controller;

	public UserAction(InteractiveController controller) {
		this.controller = controller;
	}

	public abstract void undo();

	public abstract void execute();
}
