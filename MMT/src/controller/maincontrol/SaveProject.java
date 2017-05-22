package controller.maincontrol;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import controller.Controller;

public class SaveProject extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8515672278193284629L;
	private Controller c;
	private JFileChooser fc;

	public SaveProject(Controller c) {
		super("Save Project");
		super.putValue(SHORT_DESCRIPTION, "save the project");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_S);
		this.c = c;
		this.fc = c.getFileChooser();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (c.getCurrentProject() != null) {
			c.saveProject(c.getCurrentProject());
		} else {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showSaveDialog(c.getMainFrame());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (!file.getName().endsWith(".mmp")) {
					file = new File(file.getAbsolutePath() + ".mmp");
				}

				c.saveProject(file);
			}
		}

	}

}
