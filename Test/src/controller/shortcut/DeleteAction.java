package controller.shortcut;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveShape;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.history.UserAction;
import controller.history.actions.UserDeleteAction;
import controller.interactivepane.InteractiveController;

public class DeleteAction extends AbstractAction {
	private InteractiveController controller;

	public DeleteAction(InteractiveController controller) {
		super("delete");
		super.putValue(SHORT_DESCRIPTION, "delete selected components");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserAction delete = new UserDeleteAction(controller, controller.getPane().getComponentSelection(),
				controller.getPane().getShapeSelection());
		controller.executeAction(delete);
		controller.clearSelection();
		// TODO: Cables need to get disconnected upon module or cable deletion!
		// (both in Pane and Graph)

	}

}
