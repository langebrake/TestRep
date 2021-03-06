package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.interactivepane.InteractiveController;

public class RedoAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7010572065431886123L;
	private InteractiveController controller;

	public RedoAction(InteractiveController controller) {
		super("redo");
		super.putValue(SHORT_DESCRIPTION, "redos the last undone action");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_D);
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.getActionManager().redo();

	}

}
