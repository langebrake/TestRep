package controller.interactivepane;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractiveShape;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

import model.pluginmanager.Loadable;
import model.pluginmanager.PluginHierarchyElement;
import model.pluginmanager.PluginManager;
import model.pluginmanager.Subgroup;
import controller.pluginmanager.PluginAddAction;
import controller.shortcut.AddGroupingInputAction;
import controller.shortcut.AddGroupingOutputAction;
import controller.shortcut.DeleteAction;
import controller.shortcut.GroupingAction;
import controller.shortcut.RenameAction;

public class PopupMenuListener extends MouseAdapter implements Serializable {
	private InteractiveController controller;
	public PopupMenuListener(InteractiveController c) {
		this.controller = c;
	}


	public void mousePressed(MouseEvent e) {
		controller.updateLastMouseLocation(e);
		showPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
		controller.updateLastMouseLocation(e);
		showPopup(e);
	}
	

	private void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			Object source = e.getSource();
				if (source instanceof InteractiveComponent && !((InteractiveComponent) source).isSelected()){
					controller.getPane().clearSelection();
					controller.getPane().setComponentSelected((InteractiveComponent) source, true);
				}
				Point pointOnPane = controller.getLastMousePaneLocation().toPoint();
				for(InteractiveShape s:controller.getPane().getShapes()){
					if(s.contains(pointOnPane) && !s.isSelected()){
						controller.getPane().clearSelection();
						controller.getPane().setShapeSelected(s, true);
					}
				}
			JPopupMenu popup = new JPopupMenu();
			addStandardMenu(popup, e);
			addPluginMenu(popup);
			Separator s;
			s = new JPopupMenu.Separator();
			popup.add(s);
			addGroupingMenu(popup);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void addStandardMenu(JPopupMenu p, MouseEvent e){
		JMenuItem item = new JMenuItem(new DeleteAction(this.controller));
		item.setEnabled(controller.getPane().hasSelected());
		p.add(item);
		Object source = e.getSource();

		item = new JMenuItem(new RenameAction(e.getSource()));
		System.out.println(e.getSource());
		p.add(item);
		item.setEnabled(source instanceof InteractiveModule);


	}
	
	private void addPluginMenu(JPopupMenu p){
		JMenu t = new JMenu("Add");
		LinkedList<PluginHierarchyElement> plugins = PluginManager.getPluginList();
		this.addPluginMenuRecursive(t, plugins);
		p.add(t);
	}
	
	private void addPluginMenuRecursive(JMenu m, LinkedList<PluginHierarchyElement> plugins){
		for(PluginHierarchyElement e:plugins){
			if(e.isLoadable()){
				m.add(new JMenuItem(new PluginAddAction(this.controller, (Loadable) e)));
			} else if (e.isSubgroup()){
				JMenu tmp = new JMenu(e.getName());
				this.addPluginMenuRecursive(tmp, (Subgroup) e);
				m.add(tmp);
			}
		}
	}
	
	private void addGroupingMenu(JPopupMenu p){
		JMenuItem item = new JMenuItem(new GroupingAction(this.controller));
		item.setEnabled(controller.getPane().getComponentSelection().size()!=0);
		p.add(item);
		item = new JMenuItem();
		item.setAction(new AddGroupingInputAction(controller.getGrouping()));
		item.setEnabled(controller.getGrouping() != null);
		p.add(item);
		item = new JMenuItem();
		item.setAction(new AddGroupingOutputAction(controller.getGrouping()));
		item.setEnabled(controller.getGrouping() != null);
		p.add(item);
		
		
	}
	
}
