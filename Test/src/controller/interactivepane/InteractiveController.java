package controller.interactivepane;

import gui.interactivepane.CablePointPanel;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveDisplay;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.InteractiveShape;
import gui.interactivepane.Vector;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.shortcut.DeleteAction;
import controller.shortcut.RedoAction;
import controller.shortcut.UndoAction;
import model.MidiGraph;

public class InteractiveController implements MouseInputListener {
	private InteractivePane pane;
	private MidiGraph graph;
	private UserActionManager actionManager;
	private Vector lastMousePaneLocation,
					lastMouseGridLocation;
	
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
				
				
				
				
				//TODO: remove this cable debug thing
				CablePointPanel p1 = new CablePointPanel(this.pane);
				CablePointPanel p2 = new CablePointPanel(this.pane);
				p1.setSize(50, 50);
				p2.setSize(50, 50);
				p1.setBackground(Color.LIGHT_GRAY);
				p2.setBackground(Color.LIGHT_GRAY);
				
				InteractiveComponent src = new InteractiveDisplay(this.pane,new Vector(0,0), p1);
				InteractiveComponent src2 = new InteractiveDisplay(this.pane,new Vector(500,900), p2);
				p1.setCable(new InteractiveCable(p1,p2,this.pane));
				p2.setCable(p1.getCable());
				ModuleListener testListener = new ModuleListener(this);
				src.addMouseListener(testListener);
				src.addMouseMotionListener(testListener);
				src.addMouseWheelListener(testListener);
				src2.addMouseListener(testListener);
				src2.addMouseMotionListener(testListener);
				src2.addMouseWheelListener(testListener);
				this.pane.add(src);
				this.pane.add(src2);
				this.pane.add(p1.getCable());
				this.pane.addMouseListener(PopupMenuListener.getInstance(this));
				src.addMouseListener(PopupMenuListener.getInstance(this));
				src2.addMouseListener(PopupMenuListener.getInstance(this));
				ShapeListener sl = new ShapeListener(this);
				this.pane.addMouseListener(sl);
				this.pane.addMouseMotionListener(sl);
				this.pane.addMouseWheelListener(sl);
		
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
		if(!arg0.isShiftDown()
				&& !SwingUtilities.isMiddleMouseButton(arg0)
				&& !SwingUtilities.isRightMouseButton(arg0)){
			this.pane.clearSelection();
		} 

		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.pane.commitSelection();
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		//this procedure is necessary due to a bug in AWT:
		//MouseWheelEvent.getLocationOnScreen() always returns (0,0)
		Vector tmp = new Vector(arg0.getComponent().getLocationOnScreen());
		tmp = tmp.addVector(new Vector(arg0.getPoint()));
		this.lastMousePaneLocation = new Vector(this.pane.getLocationOnScreen()).diffVector(tmp);
		this.lastMouseGridLocation = this.toGridCoordinate(this.lastMousePaneLocation);
		this.pane.zoomViewport(this.lastMousePaneLocation, arg0.getWheelRotation());
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		Vector currentMouseGridLocation = this.toGridCoordinate(this.relativeToPane(arg0));
		
		if(SwingUtilities.isLeftMouseButton(arg0) && (arg0.isControlDown())  || SwingUtilities.isMiddleMouseButton(arg0)){
			this.pane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
			
		} else if(SwingUtilities.isLeftMouseButton(arg0) ){
			this.pane.selectionArea(this.lastMousePaneLocation, this.relativeToPane(arg0), true);	
				
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
	
	public InteractivePane getPane(){
		return this.pane;
	}
	

}
