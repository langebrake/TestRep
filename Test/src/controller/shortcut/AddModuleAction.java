package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.history.UserAction;
import controller.history.actions.UserDeleteAction;
import controller.interactivepane.InteractiveController;

public class AddModuleAction extends AbstractAction {

	private InteractiveController controller;
	
	public AddModuleAction(InteractiveController controller, String moduleName){
		super(moduleName);
		super.putValue(SHORT_DESCRIPTION,"add the module " + moduleName);
		this.controller = controller;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		UserAction delete = new UserDeleteAction(controller.getActionManager(), controller.getPane().getComponentSelection(), controller.getPane().getShapeSelection());
		controller.executeAction(delete);
		controller.getPane().clearSelection();
		
	}

}
