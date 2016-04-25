package controller.shortcut;

import gui.InteractiveComponent;
import gui.InteractiveShape;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.InteractiveController;
import controller.history.UserAction;
import controller.history.actions.UserDeleteAction;

public class DeleteAction extends AbstractAction {
	private InteractiveController controller;
	public DeleteAction(InteractiveController controller){
		super("delete");
		super.putValue(SHORT_DESCRIPTION,"delete selected components");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_D);
		this.controller = controller;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserAction delete = new UserDeleteAction(controller.getActionManager(), controller.getPane().getComponentSelection(), controller.getPane().getShapeSelection());
		controller.executeAction(delete);
		controller.getPane().clearSelection();
		
	}
	
}
