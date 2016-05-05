package controller.shortcut;

import gui.interactivepane.InteractiveModule;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

public class RenameAction extends AbstractAction {
	InteractiveModule module;
	public RenameAction(Object m){
		super("rename");
		super.putValue(SHORT_DESCRIPTION,"rename this component");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_R);
		if(m instanceof InteractiveModule){
			this.module = (InteractiveModule) m;
		}
		this.module = null;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.module != null){
			
		}

	}

}
