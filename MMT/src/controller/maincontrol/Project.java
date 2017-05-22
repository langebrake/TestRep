package controller.maincontrol;

import java.awt.BorderLayout;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import stdlib.grouping.Grouping;
import controller.clipboard.Clipboard;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;
import controller.projectree.ProjectTreeListener;
import gui.interactivepane.InteractiveModule;

public class Project implements Serializable, PropertyChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 495749326805314763L;
	private transient Container contentPane;
	private LinkedList<InteractiveController> interactivePanes;
	private transient Clipboard clipboard;
	private transient UserActionManager actionManager;
	private transient JSplitPane leftCenterSplit;
	private transient JTree tree;
	private transient DefaultMutableTreeNode root;

	public Project() {
		init();
		createContentPane();

	}

	private void init() {
		this.clipboard = new Clipboard();
		this.clipboard.setProject(this);
		this.actionManager = new UserActionManager();
		this.actionManager.setProject(this);
		this.selectedNodes = new LinkedList<TreePath>();

	}

	private transient JTabbedPane tabbedPane;

	private void createContentPane() {
		this.contentPane = new JPanel();
		this.contentPane.setLayout(new BorderLayout());

		leftCenterSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		tabbedPane = new JTabbedPane();
		if (interactivePanes == null) {
			InteractiveController controller = new InteractiveController();
			controller.setUserActionManager(actionManager);
			controller.setClipboard(this.clipboard);
			controller.setProject(this);
			interactivePanes = new LinkedList<InteractiveController>();
			interactivePanes.add(controller);
		}
		tabbedPane.addTab("Main", interactivePanes.get(0).getPane());
		leftCenterSplit.setBottomComponent(tabbedPane);
		leftCenterSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, this
				);

		updateTree();

		this.contentPane.add(leftCenterSplit, BorderLayout.CENTER);
	}

	public void registerTab(InteractiveController c) {

		if (!tabbedPane.isAncestorOf(c.getPane()))
			tabbedPane.add(c.getTitle(), c.getPane());
		tabbedPane.setSelectedComponent(c.getPane());

	}

	public void unregisterTab(InteractiveModule module) {
		if (module.getModule().getPlugin() instanceof Grouping) {
			Grouping g = (Grouping) module.getModule().getPlugin();
			tabbedPane.remove(g.getController().getPane());
		}
	}

	public Container getContentPane() {
		return this.contentPane;
	}

	public void close() {
		for (InteractiveController c : interactivePanes) {
			c.close();
		}
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in)
			throws ClassNotFoundException, IOException {
		init();
		InteractiveController.clipboardControl = this.clipboard;
		InteractiveController.managerControl = this.actionManager;
		InteractiveController.projectControl = this;
		in.defaultReadObject();

		this.createContentPane();
		this.reUpdate();
	}

	public void updateTree() {
		LinkedList<TreePath> expanded = new LinkedList<TreePath>();
		if (tree != null) {

			Enumeration<TreePath> e = tree.getExpandedDescendants(new TreePath(
					root.getPath()));
			if (e != null)
				while (e.hasMoreElements()) {
					expanded.add(e.nextElement());
				}
		}
		root = interactivePanes.get(0).treeView();
		TreeSorter.sortTree(root);
		tree = new JTree(root);

		ProjectTreeListener ptl = new ProjectTreeListener(this);
		tree.addTreeSelectionListener(ptl);
		tree.addMouseListener(ptl);
		JScrollPane treePane = new JScrollPane(tree);
		leftCenterSplit.setTopComponent(treePane);
		leftCenterSplit.revalidate();
		if (tree != null) {
			for (TreePath tp : expanded) {
				tree.expandPath(tp);
			}
		}
		tree.setSelectionPaths((TreePath[]) selectedNodes
				.toArray(new TreePath[selectedNodes.size()]));

	}

	public JTree getTree() {
		return this.tree;
	}

	private transient LinkedList<TreePath> selectedNodes;

	public void setTreeNodeSelection(TreePath treePath, boolean selection) {
		if (selection) {
			selectedNodes.add(treePath);
		} else {
			selectedNodes.remove(treePath);
		}
		tree.setSelectionPaths((TreePath[]) selectedNodes
				.toArray(new TreePath[selectedNodes.size()]));
	}

	public void clearTreeSelection() {
		selectedNodes.clear();
		tree.clearSelection();

	}

	public void changeTabTitle(InteractiveController interactiveController) {
		int index = tabbedPane
				.indexOfComponent(interactiveController.getPane());
		if (index >= 0) {
			tabbedPane.setTitleAt(index, interactiveController.getTitle());

		}

	}

	public void reUpdate() {
		for(InteractiveController c: this.interactivePanes){
			c.getPane().updateView();
		}
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		this.reUpdate();
		
	}

}
