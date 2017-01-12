package controller.projectree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import stdlib.grouping.Grouping;
import controller.maincontrol.Project;

public class ProjectTreeListener extends MouseAdapter implements TreeSelectionListener {
	private Project project;

	public ProjectTreeListener(Project project) {
		this.project = project;
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		TreePath[] paths = arg0.getPaths();
		for (TreePath path : paths) {
			InteractiveModule mod = (InteractiveModule) ((DefaultMutableTreeNode) path.getLastPathComponent())
					.getUserObject();
			if (arg0.isAddedPath(path)) {
				if (!mod.isSelected())
					mod.getController().getPane().setComponentSelected(mod, true);
				;
			} else {
				if (mod.isSelected())
					mod.getController().getPane().setComponentSelected(mod, false);
				;
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		int selRow = project.getTree().getRowForLocation(e.getX(), e.getY());
		TreePath selPath = project.getTree().getPathForLocation(e.getX(), e.getY());
		if (selRow != -1) {
			if (e.getClickCount() == 1) {
				singleClick(selRow, selPath);
			} else if (e.getClickCount() == 2) {
				doubleClick(selRow, selPath);
			}
		}
	}

	public void singleClick(int row, TreePath path) {

	}

	public void doubleClick(int row, TreePath path) {
		DefaultMutableTreeNode tmp = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (tmp.getUserObject() instanceof InteractiveModule) {
			InteractiveModule mod = (InteractiveModule) tmp.getUserObject();
			mod.getController().focusComponent(mod);
		}
		;
	}

}
