package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.InteractiveController;
import controller.history.UserAction;
import controller.history.actions.UserDeleteAction;

public class UndoAction extends AbstractAction {

	private InteractiveController controller;
	public UndoAction(InteractiveController controller){
		super("undo");
		super.putValue(SHORT_DESCRIPTION,"undo last change");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_U);
		this.controller = controller;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.getActionManager().undo();
		
	}

}
