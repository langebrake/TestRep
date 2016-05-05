package controller.interactivepane;

import gui.interactivepane.CablePointPanel;
import gui.interactivepane.CablePointSimple;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveCableComponent;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveDisplay;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.InteractiveShape;
import gui.interactivepane.InteractiveShapeComponent;
import gui.interactivepane.Vector;

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
import java.io.Serializable;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plugin.Plugin;
import pluginhost.PluginHost;
import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.shortcut.DeleteAction;
import controller.shortcut.RedoAction;
import controller.shortcut.UndoAction;
import model.MidiGraph;

public class InteractiveController implements MouseInputListener,WindowStateListener, Serializable {
	private InteractivePane pane;
	private MidiGraph graph;
	private transient UserActionManager actionManager;
	private Vector lastMousePaneLocation,
					lastMouseGridLocation;
	private transient ModuleListener moduleListener;
	private transient PopupMenuListener popupMenuListener;
	private transient ShapeListener shapeListener;
	private transient CableCreationListener cableCreationListener;
	private transient boolean componentAndViewDrag;
	private transient boolean cableAddProcess;
	
	public InteractiveController(){
		this(new InteractivePane(), new MidiGraph(), new UserActionManager());
	}
	public InteractiveController(InteractivePane pane, MidiGraph graph, UserActionManager actionManager){
		this.pane = pane;
		this.graph = graph;
		this.actionManager = actionManager;
		this.actionManager.setController(this);

		this.pane.addMouseListener(this);
		this.pane.addMouseMotionListener(this);
		this.pane.addMouseWheelListener(this);
		
		this.moduleListener = new ModuleListener(this);
		this.popupMenuListener = new PopupMenuListener(this);
		this.shapeListener = new ShapeListener(this);
		this.cableCreationListener = new CableCreationListener(this);
		this.pane.addMouseListener(this.cableCreationListener);
		this.pane.addMouseMotionListener(this.cableCreationListener);
		// TODO: Shortcut handling should be done by other class
				InputMap inputMap = this.pane.getInputMap();
				ActionMap actionMap = this.pane.getActionMap();
				
				//undo/redo shortcuts
				KeyStroke undoCode = KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_DOWN_MASK);
				inputMap.put(undoCode, "undoPerformed");
				actionMap.put("undoPerformed", new UndoAction(this));
				KeyStroke redoCode = KeyStroke.getKeyStroke(KeyEvent.VK_Y,InputEvent.CTRL_DOWN_MASK);
				inputMap.put(redoCode, "redoPerformed");
				actionMap.put("redoPerformed", new RedoAction(this));
				
				
				//delete shortcut
				KeyStroke deleteCode = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
				inputMap.put(deleteCode, "deletePerformed");
				AbstractAction deleteAction = new DeleteAction(this);
				actionMap.put("deletePerformed", deleteAction);
				
				
				
				
//				//TODO: remove this cable debug thing
//				CablePointPanel p1 = new CablePointPanel(new CablePointSimple(CablePointType.INPUT));
//				CablePointPanel p2 = new CablePointPanel(new CablePointSimple(CablePointType.OUTPUT));
//				CablePointPanel p3 = new CablePointPanel(new CablePointSimple(CablePointType.OUTPUT));
//				CablePointPanel p4 = new CablePointPanel(new CablePointSimple(CablePointType.INPUT));
//				p1.setSize(50, 50);
//				p2.setSize(50, 50);
//				p3.setSize(50, 50);
//				p4.setSize(50, 50);
//				p1.setBackground(Color.LIGHT_GRAY);
//				p2.setBackground(Color.LIGHT_GRAY);
//				p3.setBackground(Color.LIGHT_GRAY);
//				p4.setBackground(Color.LIGHT_GRAY);
//				
//				InteractiveComponent src = new InteractiveDisplay(this,new Vector(0,0), p1);
//				InteractiveComponent src2 = new InteractiveDisplay(this,new Vector(500,900), p2);
//				InteractiveComponent src3 = new InteractiveDisplay(this,new Vector(24,789), p3);
//				InteractiveComponent src4 = new InteractiveDisplay(this,new Vector(654,345), p4);
//
//				
//				src.addListeners(this.moduleListener,this.popupMenuListener,this.shapeListener,this.cableCreationListener);
//				src2.addListeners(this.moduleListener,this.popupMenuListener,this.shapeListener,this.cableCreationListener);
//				src3.addListeners(this.moduleListener,this.popupMenuListener,this.shapeListener,this.cableCreationListener);
//				src4.addListeners(this.moduleListener,this.popupMenuListener,this.shapeListener,this.cableCreationListener);
//				this.pane.add(src);
//				this.pane.add(src2);
//				this.pane.add(src3);
//				this.pane.add(src4);
//				InteractiveShapeComponent s = new InteractiveShapeComponent(this, new Vector(0,0));
//				s.addListeners(this.moduleListener,this.popupMenuListener, this.shapeListener, this.cableCreationListener);
//				this.pane.add(s);
				// a few graphical bugs come with the introduction of interactive cable components
//				InteractiveCableComponent icc = new InteractiveCableComponent(p1,p2,this.pane);
//				this.pane.add(icc);
				// TODO: think about adding Cables as CableComponents instead of paint graphic shapes
				ShapeListener sl = new ShapeListener(this);
				this.pane.addMouseListener(sl);
				this.pane.addMouseMotionListener(sl);
				this.pane.addMouseWheelListener(sl);
				this.pane.addMouseListener(this.popupMenuListener);

				
		
	}
	
	public void setUserActionManager(UserActionManager manager){
		this.actionManager = manager;
	}
	public void executeAction(UserAction a){
		this.actionManager.addEvent(a);
		a.execute();
	}
	
	public void undoAction(){
		this.actionManager.undo();
	}
	
	public void redoAction(){
		this.actionManager.redo();
	}
	

	
	public UserActionManager getActionManager(){
		return this.actionManager;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {

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
		if(validInteraction(arg0)){
			if(!arg0.isShiftDown()
					&& !SwingUtilities.isMiddleMouseButton(arg0)
					&& !SwingUtilities.isRightMouseButton(arg0)
					&&! arg0.isControlDown()){

				this.pane.clearSelection();
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
		//prevents buggy zooming actions while moving components
		if(!this.getDragged()){
			//this procedure is necessary due to a bug in AWT:
			//MouseWheelEvent.getLocationOnScreen() always returns (0,0)
			Vector tmp = new Vector(arg0.getComponent().getLocationOnScreen());
			tmp = tmp.addVector(new Vector(arg0.getPoint()));
			this.lastMousePaneLocation = new Vector(this.pane.getLocationOnScreen()).diffVector(tmp);
			this.lastMouseGridLocation = this.toGridCoordinate(this.lastMousePaneLocation);
		}
		
		this.pane.zoomViewport(this.lastMousePaneLocation, arg0.getPreciseWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(!this.getCableAddProcess() || SwingUtilities.isMiddleMouseButton(arg0)){
			if(validInteraction(arg0)){
				Vector currentMouseGridLocation = this.toGridCoordinate(this.relativeToPane(arg0));
				if(SwingUtilities.isLeftMouseButton(arg0) && (arg0.isControlDown())  || SwingUtilities.isMiddleMouseButton(arg0)){
					this.pane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
					
					
				} else if(SwingUtilities.isLeftMouseButton(arg0) ){
					this.pane.selectionArea(this.lastMousePaneLocation, this.relativeToPane(arg0), true);	
						
				}
			}
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void setCableAddProcess(boolean set){
		this.cableAddProcess = set;
	}
	
	public boolean getCableAddProcess(){
		return this.cableAddProcess;
	}
	
	public void setDragged(boolean set){
		this.componentAndViewDrag = set;
	}
	
	public boolean getDragged(){
		return this.componentAndViewDrag;
	}
	
	
	public void updateLastMouseLocation(MouseEvent e){
		this.lastMousePaneLocation = this.relativeToPane(e);
		this.lastMouseGridLocation = this.toGridCoordinate(lastMousePaneLocation);
	}
	
	public Vector relativeToPane(MouseEvent e){
		return (new Vector(this.pane.getLocationOnScreen())).diffVector((new Vector(e.getLocationOnScreen())));
	}
	
	public Vector toGridCoordinate(Vector e){
		return this.pane.convertToGridLocation(e);
	}
	
	public Vector getLastMousePaneLocation(){
		return this.lastMousePaneLocation;
	}
	
	public Vector getLastMouseGridLocation(){
		return this.lastMouseGridLocation;
	}
	
	public PopupMenuListener getPopupMenuListener(){
		return this.popupMenuListener;
	}
	
	public ModuleListener getModuleListener(){
		return this.moduleListener;
	}
	
	public ShapeListener getShapeListener(){
		return this.shapeListener;
	}
	
	public CableCreationListener getCableCreationListener(){
		return this.cableCreationListener;
	}
	
	public InteractivePane getPane(){
		return this.pane;
	}
	
	private boolean validInteraction(MouseEvent e){
		return SwingUtilities.isLeftMouseButton(e)
				&& !(e.isControlDown() && e.isShiftDown())
				&& !SwingUtilities.isRightMouseButton(e)
				|| SwingUtilities.isMiddleMouseButton(e);
	}
	@Override
	public void windowStateChanged(WindowEvent arg0) {
		this.pane.updateView();
		this.pane.repaint();
		System.out.println("WINDOW");
		
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
//		this.actionManager = new UserActionManager();
//		this.actionManager.setController(this);
		this.moduleListener = new ModuleListener(this);
		this.popupMenuListener = new PopupMenuListener(this);
		this.shapeListener = new ShapeListener(this);
		this.cableCreationListener = new CableCreationListener(this);
		this.cableAddProcess = false;
		this.componentAndViewDrag = false;
		this.actionManager = new UserActionManager();
		this.actionManager.setController(this);
	}

	

}
