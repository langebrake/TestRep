package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.InteractiveController;
import controller.history.UserAction;
import controller.history.actions.UserDeleteAction;

public class RedoAction extends AbstractAction {

	private InteractiveController controller;
	public RedoAction(InteractiveController controller){
		super("delete");
		super.putValue(SHORT_DESCRIPTION,"delete selected components");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_DELETE);
		this.controller = controller;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.getActionManager().redo();
		
	}

}
