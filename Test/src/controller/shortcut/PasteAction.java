package controller.shortcut;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;

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
		if(this.controller.getClipboard().hasClipboard()){
			Vector offset = this.controller.getPane()
					.getViewportTranslation()
					.diffVector(this.controller
							.getClipboard()
							.getCopyGridLocation())
							.addVector(new Vector(10,10));
			
			
			LinkedList<InteractiveComponent> tmp = this.controller
					.getClipboard()
					.getClipboardClone(offset, this.controller);
			
			for(InteractiveComponent c: tmp){
				this.controller.add((InteractiveModule) c);
			}
		}
	}

}
