package controller.maincontrol;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.Controller;

public class NewProject extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3735335265108911453L;
	private Controller c;

	public NewProject(Controller c) {
		super("New Project");
		super.putValue(SHORT_DESCRIPTION, "create a new project");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_N);
		this.c = c;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		c.newProject();

	}

}
