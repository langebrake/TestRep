package controller.shortcut;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;

import controller.history.actions.UserPasteAction;
import controller.interactivepane.InteractiveController;

public class PasteAction extends AbstractAction {

	private InteractiveController controller;
	private Vector pasteGridLocation;
	
	public PasteAction(InteractiveController c){
		super("paste");
		super.putValue(SHORT_DESCRIPTION,"paste clipboard");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_V);
		this.controller = c;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		Vector offset = this.controller.getPane()
				.getViewportTranslation()
				.diffVector(this.controller
						.getClipboard()
						.getCopyGridLocation())
						.addVector(new Vector(10,10));
		
		this.controller.executeAction(new UserPasteAction(this.controller,
				this.controller.getClipboard().getClipboard(),
				this.controller.getClipboard().getCableClipboard(),
				offset));
	}

}
