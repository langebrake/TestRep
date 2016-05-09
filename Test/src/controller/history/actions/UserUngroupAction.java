package controller.history.actions;

import java.util.LinkedList;

import stdlib.grouping.Grouping;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import controller.history.UserAction;
import controller.interactivepane.InteractiveController;

public class UserUngroupAction extends UserAction {
	private Grouping grouping;
	private InteractiveModule groupModule;
	LinkedList<InteractiveComponent> groupThis;
	public UserUngroupAction(InteractiveController controller, InteractiveModule grouping) {
		super(controller);
		this.groupModule = grouping;
		this.grouping = (Grouping) grouping.getModule().getPlugin();
		groupThis = this.grouping.getContent();
		
	}

	@Override
	public void undo() {
			groupModule.reopen();
			this.grouping.group(groupModule, groupThis);
			controller.getPane().add(groupModule);

	}

	@Override
	public void execute() {
			this.grouping.ungroup(groupModule);
			groupModule.close();
			controller.getPane().remove(groupModule);
			

	}

}
