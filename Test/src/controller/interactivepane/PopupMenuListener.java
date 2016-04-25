package controller.interactivepane;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveShape;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import controller.pluginmanager.PluginManager;
import controller.shortcut.DeleteAction;

public class PopupMenuListener extends MouseAdapter {
	private static PopupMenuListener instance;
	private InteractiveController controller;
	private PopupMenuListener(InteractiveController c) {
		this.controller = c;
	}

	public static PopupMenuListener getInstance(InteractiveController c) {
		if (instance == null) {
			instance = new PopupMenuListener(c);
		}
		return instance;
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
			addStandardMenu(popup);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private void addStandardMenu(JPopupMenu p){
		JMenuItem item = new JMenuItem(new DeleteAction(this.controller));
		item.setEnabled(controller.getPane().hasSelected());
		p.add(item);
		
		JMenu t = new JMenu("Add");
		//PluginManager.addPluginMenuList(t);
		p.add(t);
	}
}
