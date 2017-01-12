package controller.interactivepane;

import engine.Stringer;
import gui.interactivepane.CablePointHost;
import gui.interactivepane.CablePointPanel;
import gui.interactivepane.CablePointSimple;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveCableComponent;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveDisplay;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.InteractiveShape;
import gui.interactivepane.InteractiveShapeComponent;
import gui.interactivepane.Vector;

import java.awt.Component;
import java.awt.Window;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import plugin.Plugin;
import pluginhost.PluginHost;
import stdlib.grouping.Grouping;
import controller.clipboard.Clipboard;
import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.maincontrol.Project;
import controller.shortcut.CopyAction;
import controller.shortcut.CutAction;
import controller.shortcut.DeleteAction;
import controller.shortcut.PasteAction;
import controller.shortcut.RedoAction;
import controller.shortcut.UndoAction;
import model.MidiGraph;
import model.graph.Module;

public class InteractiveController implements MouseInputListener, WindowStateListener, Serializable, Cloneable {
	private transient InteractivePane pane;
	private MidiGraph graph;
	private String title;
	private transient UserActionManager actionManager;
	private transient Vector lastMousePaneLocation, lastMouseGridLocation;
	private transient ModuleListener moduleListener;
	private transient PopupMenuListener popupMenuListener;
	private transient ShapeListener shapeListener;
	private transient CableCreationListener cableCreationListener;
	private transient boolean componentAndViewDrag;
	private transient boolean cableAddProcess;
	private transient CablePointHost cableAddProcessSource;
	private transient Clipboard clipboard;
	private transient Project project;

	public static transient UserActionManager managerControl;
	public static transient Clipboard clipboardControl;
	public static transient Project projectControl;

	public InteractiveController() {
		this(new InteractivePane(), new MidiGraph(), new UserActionManager(), new Clipboard(), null);
	}

	public InteractiveController(InteractivePane pane, MidiGraph graph, UserActionManager actionManager, Clipboard c,
			Project p) {
		this.graph = graph;
		this.actionManager = actionManager;
		this.moduleListener = new ModuleListener(this);
		this.popupMenuListener = new PopupMenuListener(this);
		this.shapeListener = new ShapeListener(this);
		this.cableCreationListener = new CableCreationListener(this);
		tmp = new DefaultMutableTreeNode();
		this.pane = this.initPane(pane);
		this.clipboard = c;
		this.project = p;
		this.title = "untitled";
	}

	public void setProject(Project p) {
		this.project = p;
	}

	public Project getProject() {
		return this.project;
	}

	private InteractivePane initPane(InteractivePane pane) {
		pane.addMouseListener(this);
		pane.addMouseMotionListener(this);
		pane.addMouseWheelListener(this);
		pane.addMouseListener(this.cableCreationListener);
		pane.addMouseMotionListener(this.cableCreationListener);
		// TODO: Shortcut handling should be done by other class
		InputMap inputMap = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = pane.getActionMap();

		// undo/redo shortcuts
		KeyStroke undoCode = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
		inputMap.put(undoCode, "undoPerformed");
		actionMap.put("undoPerformed", new UndoAction(this));
		KeyStroke redoCode = KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
		inputMap.put(redoCode, "redoPerformed");
		actionMap.put("redoPerformed", new RedoAction(this));

		// delete shortcut
		KeyStroke deleteCode = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		inputMap.put(deleteCode, "deletePerformed");
		AbstractAction deleteAction = new DeleteAction(this);
		actionMap.put("deletePerformed", deleteAction);

		// Copy Paste Cut shortcuts
		KeyStroke copyCode = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);
		inputMap.put(copyCode, "copyPerformed");
		actionMap.put("copyPerformed", new CopyAction(this));
		KeyStroke pasteCode = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
		inputMap.put(pasteCode, "pastePerformed");
		actionMap.put("pastePerformed", new PasteAction(this));
		KeyStroke cutCode = KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);
		inputMap.put(cutCode, "cutPerformed");
		actionMap.put("cutPerformed", new CutAction(this));

		// TODO: think about adding Cables as CableComponents instead of paint
		// graphic shapes
		ShapeListener sl = new ShapeListener(this);
		pane.addMouseListener(sl);
		pane.addMouseMotionListener(sl);
		pane.addMouseWheelListener(sl);
		pane.addMouseListener(this.popupMenuListener);

		return pane;
	}

	public MidiGraph getGraph() {
		return this.graph;
	}

	public void add(InteractiveComponent c) {
		this.pane.add(c);
		if (c instanceof InteractiveModule)
			this.graph.add(((InteractiveModule) c).getModule());
	}

	public void remove(InteractiveComponent c) {
		this.pane.remove(c);
		if (c instanceof InteractiveModule)
			this.graph.remove(((InteractiveModule) c).getModule());
	}

	public void setUserActionManager(UserActionManager manager) {
		this.actionManager = manager;
	}

	public void executeAction(UserAction a) {
		this.actionManager.addEvent(a);
		this.actionManager.execute();
	}

	public void undoAction() {
		this.actionManager.undo();
	}

	public void redoAction() {
		this.actionManager.redo();
	}

	public UserActionManager getActionManager() {
		return this.actionManager;
	}

	public void setClipboard(Clipboard c) {
		this.clipboard = c;
	}

	public Clipboard getClipboard() {
		return this.clipboard;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.pane.requestFocus();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.updateLastMouseLocation(arg0);
		if (validInteraction(arg0)) {
			if (!arg0.isShiftDown() && !SwingUtilities.isMiddleMouseButton(arg0)
					&& !SwingUtilities.isRightMouseButton(arg0) && !arg0.isControlDown()) {

				this.clearSelection();
				this.pane.repaint();

			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.pane.commitSelection();

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// prevents buggy zooming actions while moving components
		if (!this.getDragged()) {
			// this procedure is necessary due to a bug in AWT:
			// MouseWheelEvent.getLocationOnScreen() always returns (0,0)
			Vector tmp = new Vector(arg0.getComponent().getLocationOnScreen());
			tmp = tmp.addVector(new Vector(arg0.getPoint()));
			this.lastMousePaneLocation = new Vector(this.pane.getLocationOnScreen()).diffVector(tmp);
			this.lastMouseGridLocation = this.toGridCoordinate(this.lastMousePaneLocation);
		}

		this.pane.zoomViewport(this.lastMousePaneLocation, arg0.getPreciseWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (!this.getCableAddProcess() || SwingUtilities.isMiddleMouseButton(arg0)) {
			if (validInteraction(arg0)) {
				Vector currentMouseGridLocation = this.toGridCoordinate(this.relativeToPane(arg0));
				if (SwingUtilities.isLeftMouseButton(arg0) && (arg0.isControlDown())
						|| SwingUtilities.isMiddleMouseButton(arg0)) {
					this.pane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));

				} else if (SwingUtilities.isLeftMouseButton(arg0)) {
					this.pane.selectionArea(this.lastMousePaneLocation, this.relativeToPane(arg0), true, this);

				}
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setCableAddProcess(boolean set) {
		this.cableAddProcess = set;
	}

	public boolean getCableAddProcess() {
		return this.cableAddProcess;
	}

	public void setDragged(boolean set) {
		this.componentAndViewDrag = set;
	}

	public boolean getDragged() {
		return this.componentAndViewDrag;
	}

	public void updateLastMouseLocation(MouseEvent e) {
		this.lastMousePaneLocation = this.relativeToPane(e);
		this.lastMouseGridLocation = this.toGridCoordinate(lastMousePaneLocation);
	}

	public Vector relativeToPane(MouseEvent e) {
		return (new Vector(this.pane.getLocationOnScreen())).diffVector((new Vector(e.getLocationOnScreen())));
	}

	public Vector toGridCoordinate(Vector e) {
		return this.pane.convertToGridLocation(e);
	}

	public Vector getLastMousePaneLocation() {
		return this.lastMousePaneLocation;
	}

	public Vector getLastMouseGridLocation() {
		return this.lastMouseGridLocation;
	}

	public PopupMenuListener getPopupMenuListener() {
		return this.popupMenuListener;
	}

	public ModuleListener getModuleListener() {
		return this.moduleListener;
	}

	public ShapeListener getShapeListener() {
		return this.shapeListener;
	}

	public CableCreationListener getCableCreationListener() {
		return this.cableCreationListener;
	}

	public InteractivePane getPane() {
		return this.pane;
	}

	private boolean validInteraction(MouseEvent e) {
		return SwingUtilities.isLeftMouseButton(e) && !(e.isControlDown() && e.isShiftDown())
				&& !SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e);
	}

	@Override
	public void windowStateChanged(WindowEvent arg0) {
		this.pane.updateView();
		this.pane.repaint();

	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.moduleListener = new ModuleListener(this);
		this.popupMenuListener = new PopupMenuListener(this);
		this.shapeListener = new ShapeListener(this);
		this.cableCreationListener = new CableCreationListener(this);
		this.cableAddProcess = false;
		this.componentAndViewDrag = false;
		tmp = new DefaultMutableTreeNode();
		if (managerControl != null) {
			this.actionManager = managerControl;
		} else {
			this.actionManager = new UserActionManager();
		}
		this.pane = this.initPane(new InteractivePane());
		if (clipboardControl != null) {
			this.clipboard = clipboardControl;
		} else {
			this.clipboard = new Clipboard();
		}
		if (projectControl != null) {
			this.project = projectControl;
		} else {
			// TODO: Error
		}
		Populator.populateWith(this, this.graph);
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		String stringer = Stringer.getString();

		out.defaultWriteObject();
		Stringer.minimize();
	}

	public void setCableAddProcessSource(CablePointHost source) {
		this.cableAddProcessSource = source;

	}

	public CablePointHost getCableAddProcessSource() {
		return this.cableAddProcessSource;
	}

	public boolean close() {
		for (Component c : this.pane.getComponents()) {
			if (c instanceof InteractiveModule) {
				((InteractiveComponent) c).close();
			}
		}
		return true;
	}

	public boolean reOpen() {
		for (Component c : this.pane.getComponents()) {
			if (c instanceof InteractiveModule) {
				((InteractiveComponent) c).reopen();
			}
		}
		return true;

	}

	public InteractiveController clone() {
		MidiGraph mg = this.graph.clone();
		InteractivePane ip = new InteractivePane();
		InteractiveController ic = new InteractiveController(ip, mg, this.actionManager, this.clipboard, this.project);
		Populator.populateWith(ic, mg);
		return ic;
	}

	private transient DefaultMutableTreeNode tmp;

	public DefaultMutableTreeNode treeView() {
		tmp.removeAllChildren();
		for (Component m : this.pane.getComponents()) {
			if (m instanceof InteractiveModule) {
				tmp.add(((InteractiveModule) m).treeView());
			}
		}
		return tmp;
	}

	public void selectComponent(InteractiveComponent c, boolean selection) {
		this.pane.setComponentSelected(c, selection);
		if (c instanceof InteractiveModule) {
			this.project.setTreeNodeSelection(((InteractiveModule) c).treePath(), selection);
		}
	}

	public void clearSelection() {
		this.pane.clearSelection();
		this.project.clearTreeSelection();
	}

	public void rename(InteractiveModule module, String result) {
		module.setName(result);
		if (module.getModule().getPlugin() instanceof Grouping) {
			((Grouping) module.getModule().getPlugin()).getController().setTitle((String) result);

		}
		this.project.updateTree();

	}

	public void tmpSelectComponent(InteractiveComponent c, boolean b) {
		if (c instanceof InteractiveModule) {
			this.project.setTreeNodeSelection(((InteractiveModule) c).treePath(), b);
		}

	}

	public void focusComponent(InteractiveModule mod) {

		this.project.registerTab(this);
		this.pane.focus(mod);
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {

		this.title = title;
		this.project.changeTabTitle(this);
	}

}
