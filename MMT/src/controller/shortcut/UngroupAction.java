package controller.shortcut;

import gui.interactivepane.InteractiveModule;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.history.actions.UserAddGroupAction;
import controller.history.actions.UserUngroupAction;
import controller.interactivepane.InteractiveController;

public class UngroupAction extends AbstractAction {
	private InteractiveModule group;
	InteractiveController controller;

	public UngroupAction(InteractiveController c, InteractiveModule group) {
		super("ungroup");
		super.putValue(SHORT_DESCRIPTION, "ungroup selected group");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_U);
		this.controller = c;
		this.group = group;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		UserUngroupAction uag = new UserUngroupAction(controller, group);
		controller.executeAction(uag);

	}

}
