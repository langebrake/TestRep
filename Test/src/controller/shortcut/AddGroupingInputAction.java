package controller.shortcut;

import gui.interactivepane.Vector;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import stdlib.grouping.Grouping;

public class AddGroupingInputAction extends AbstractAction {
	private Grouping grouping;
	public AddGroupingInputAction(Grouping grouping) {
		super("new group input");
		super.putValue(SHORT_DESCRIPTION,"add a group input");
		this.grouping = grouping;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		grouping.addGroupInput();

	}

}
