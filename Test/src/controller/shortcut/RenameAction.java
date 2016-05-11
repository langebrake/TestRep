package controller.shortcut;

import gui.interactivepane.InteractiveModule;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class RenameAction extends AbstractAction {
	InteractiveModule module;
	public RenameAction(Object m){
		super("rename");
		super.putValue(SHORT_DESCRIPTION,"rename this component");
		super.putValue(MNEMONIC_KEY,KeyEvent.VK_R);
		if(m instanceof InteractiveModule){
			this.module = (InteractiveModule) m;
		}else 
			this.module = null;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(this.module != null){
			JDialog d = new JDialog();
			Object result = JOptionPane.showInputDialog(d, "New name:");
			if(result != null && result instanceof String){
				module.setName((String) result);
			}
		}

	}

}
