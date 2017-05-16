package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.history.UserAction;
import controller.history.actions.UserDeleteAction;
import controller.interactivepane.InteractiveController;

public class CutAction extends AbstractAction {

	private InteractiveController controller;

	public CutAction(InteractiveController controller) {
		super("cut");
		super.putValue(SHORT_DESCRIPTION, "cut selected components");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_X);
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.controller.getClipboard().setClipboard(
				controller.getPane().getComponentSelection());
		controller.getClipboard().setCopyLocation(
				controller.getPane().getViewportTranslation());
		UserAction delete = new UserDeleteAction(controller, controller
				.getPane().getComponentSelection(), controller.getPane()
				.getShapeSelection());
		controller.executeAction(delete);
		controller.clearSelection();

	}

}
