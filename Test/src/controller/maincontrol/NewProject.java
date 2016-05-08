package controller.maincontrol;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import controller.Controller;

public class NewProject extends AbstractAction {
	private Controller c;
	private JFileChooser fc;
	public NewProject(Controller c){
		super("New Project");
		super.putValue(SHORT_DESCRIPTION,"create a new project");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_N);
		this.c = c;
		this.fc = c.getFileChooser();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		c.newProject();

	}


}
