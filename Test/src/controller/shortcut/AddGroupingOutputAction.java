package controller.shortcut;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import stdlib.grouping.Grouping;

public class AddGroupingOutputAction extends AbstractAction {
	
	private Grouping grouping;
	public AddGroupingOutputAction(Grouping grouping) {
		super("new group output");
		super.putValue(SHORT_DESCRIPTION,"add a group output");
		this.grouping = grouping;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		grouping.addGroupOutput();

	}

}
