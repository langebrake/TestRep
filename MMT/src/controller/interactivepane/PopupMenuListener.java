package controller.interactivepane;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractiveShape;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.LinkedList;


import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

import stdlib.grouping.Grouping;
import model.pluginmanager.Loadable;
import model.pluginmanager.PluginHierarchyElement;
import model.pluginmanager.PluginManager;
import model.pluginmanager.Subgroup;
import controller.pluginmanager.PluginAddAction;
import controller.shortcut.CopyAction;
import controller.shortcut.CutAction;
import controller.shortcut.DeleteAction;
import controller.shortcut.GroupingAction;
import controller.shortcut.PasteAction;
import controller.shortcut.RenameAction;
import controller.shortcut.UngroupAction;

public class PopupMenuListener extends MouseAdapter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6145601547304347741L;
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
			if (source instanceof InteractiveComponent
					&& !((InteractiveComponent) source).isSelected()) {
				controller.clearSelection();
				controller.selectComponent((InteractiveComponent) source, true);
			}
			Point pointOnPane = controller.getLastMousePaneLocation().toPoint();
			for (InteractiveShape s : controller.getPane().getShapes()) {
				if (s.contains(pointOnPane) && !s.isSelected()) {
					controller.clearSelection();
					controller.getPane().setShapeSelected(s, true);
				}
			}
			JPopupMenu popup = new JPopupMenu();
			addStandardMenu(popup, e);
			addPluginMenu(popup);
			Separator s;
			s = new JPopupMenu.Separator();
			popup.add(s);
			addGroupingMenu(popup, e);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	private void addStandardMenu(JPopupMenu p, MouseEvent e) {
		JMenuItem item = new JMenuItem(new CopyAction(this.controller));
		item.setEnabled(this.controller.getPane().getComponentSelection()
				.size() != 0);
		p.add(item);

		item = new JMenuItem(new CutAction(this.controller));
		item.setEnabled(this.controller.getPane().getComponentSelection()
				.size() != 0);
		p.add(item);

		item = new JMenuItem(new PasteAction(this.controller));
		item.setEnabled(this.controller.getClipboard().hasClipboard());
		p.add(item);

		item = new JMenuItem(new DeleteAction(this.controller));
		item.setEnabled(controller.getPane().hasSelected());
		p.add(item);
		Object source = e.getSource();

		item = new JMenuItem(new RenameAction(e.getSource()));
		p.add(item);
		item.setEnabled(source instanceof InteractiveModule);

	}

	private void addPluginMenu(JPopupMenu p) {
		JMenu t = new JMenu("Add");
		LinkedList<PluginHierarchyElement> plugins = PluginManager
				.getPluginList();
		this.addPluginMenuRecursive(t, plugins);
		p.add(t);
	}

	private void addPluginMenuRecursive(JMenu m,
			LinkedList<PluginHierarchyElement> plugins) {
		for (PluginHierarchyElement e : plugins) {
			if (e.isLoadable()) {
				m.add(new JMenuItem(new PluginAddAction(this.controller,
						(Loadable) e)));
			} else if (e.isSubgroup()) {
				JMenu tmp = new JMenu(e.getName());
				this.addPluginMenuRecursive(tmp, (Subgroup) e);
				m.add(tmp);
			}
		}
	}

	private void addGroupingMenu(JPopupMenu p, MouseEvent e) {
		JMenuItem item = new JMenuItem(new GroupingAction(this.controller));
		item.setEnabled(controller.getPane().getComponentSelection().size() != 0);
		p.add(item);
		if (e.getSource() instanceof InteractiveModule
				&& ((InteractiveModule) e.getSource()).getModule().getPlugin() instanceof Grouping) {
			item = new JMenuItem(new UngroupAction(this.controller,
					(InteractiveModule) e.getSource()));
			item.setEnabled(true);
		} else {
			item = new JMenuItem(new UngroupAction(this.controller, null));
			item.setEnabled(false);
		}

		p.add(item);

	}

}
