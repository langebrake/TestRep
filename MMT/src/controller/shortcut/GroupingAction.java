package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.history.actions.UserAddGroupAction;
import controller.interactivepane.InteractiveController;

public class GroupingAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2320619078265054522L;
	InteractiveController controller;

	public GroupingAction(InteractiveController c) {
		super("group");
		super.putValue(SHORT_DESCRIPTION, "group selected components");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_G);
		this.controller = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		UserAddGroupAction uag = new UserAddGroupAction(controller, controller
				.getPane().getComponentSelection());
		controller.clearSelection();
		controller.executeAction(uag);

	}

}
