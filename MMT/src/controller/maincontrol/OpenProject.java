package controller.maincontrol;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import controller.Controller;

public class OpenProject extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4368071067699767854L;
	private Controller c;
	private JFileChooser fc;

	public OpenProject(Controller c) {
		super("Open Project");
		super.putValue(SHORT_DESCRIPTION, "open a project");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		this.c = c;
		this.fc = c.getFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(c.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			if (!file.getName().endsWith(".mmp")) {
				file = new File(file.getAbsolutePath() + ".mmp");
			}
			c.loadProject(file);

		}

	}

}
