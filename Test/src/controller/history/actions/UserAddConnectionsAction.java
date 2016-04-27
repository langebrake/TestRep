package controller.history.actions;

import gui.interactivepane.CablePoint;
import controller.history.UserAction;
import controller.history.UserActionManager;

public class UserAddConnectionsAction extends UserAction {
	private CablePoint source,dest;
	public UserAddConnectionsAction(UserActionManager m, CablePoint source, CablePoint dest) {
		super(m);
		this.source=source;
		this.dest=dest;
	}

	@Override
	public void undo() {
		

	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
