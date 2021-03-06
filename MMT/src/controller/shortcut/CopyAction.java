package controller.shortcut;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import controller.interactivepane.InteractiveController;

public class CopyAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -196858309965533925L;
	private InteractiveController controller;

	public CopyAction(InteractiveController c) {
		super("copy");
		super.putValue(SHORT_DESCRIPTION, "copy selected components");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_C);
		this.controller = c;

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(controller.getPane().getComponentSelection().size()!=0){
		this.controller.getClipboard().setClipboard(
				controller.getPane().getComponentSelection());
		controller.getClipboard().setCopyLocation(
				controller.getPane().getViewportTranslation());
		}
		}

}
