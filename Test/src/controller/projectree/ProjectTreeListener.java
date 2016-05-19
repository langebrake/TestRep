package controller.projectree;

import gui.interactivepane.InteractiveModule;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import controller.maincontrol.Project;

public class ProjectTreeListener implements TreeSelectionListener {
	private Project project;
	
	public ProjectTreeListener(Project project){
		this.project = project;
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		TreePath[] paths = arg0.getPaths();
		for(TreePath path:paths){
			InteractiveModule mod = (InteractiveModule)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
			if(arg0.isAddedPath(path)){
				if(!mod.isSelected())
					mod.getController().getPane().setComponentSelected(mod, true);;
			}else{
				if(mod.isSelected())
					mod.getController().getPane().setComponentSelected(mod, false);;
			}
		}
	}

}
