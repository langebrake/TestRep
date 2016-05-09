package controller.interactivepane;

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

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import plugin.Plugin;
import pluginhost.PluginHost;
import stdlib.grouping.Grouping;
import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.shortcut.DeleteAction;
import controller.shortcut.RedoAction;
import controller.shortcut.UndoAction;
import model.MidiGraph;
import model.graph.Module;

public class InteractiveController implements MouseInputListener,WindowStateListener, Serializable {
	private transient InteractivePane pane;
	private MidiGraph graph;
	private transient UserActionManager actionManager;
	private transient Vector lastMousePaneLocation,
					lastMouseGridLocation;
	private transient ModuleListener moduleListener;
	private transient PopupMenuListener popupMenuListener;
	private transient ShapeListener shapeListener;
	private transient CableCreationListener cableCreationListener;
	private transient boolean componentAndViewDrag;
	private transient boolean cableAddProcess;
	private transient CablePointHost cableAddProcessSource;
	
	public InteractiveController(){
		this(new InteractivePane(), new MidiGraph(), new UserActionManager());
	}
	public InteractiveController(InteractivePane pane, MidiGraph graph, UserActionManager actionManager){
		this.graph = graph;
		this.actionManager = actionManager;
		this.actionManager.setController(this);
		this.moduleListener = new ModuleListener(this);
		this.popupMenuListener = new PopupMenuListener(this);
		this.shapeListener = new ShapeListener(this);
		this.cableCreationListener = new CableCreationListener(this);
		this.pane = this.initPane(pane);
				
				
				

				
		
	}
	
	private InteractivePane initPane(InteractivePane pane){
		pane.addMouseListener(this);
		pane.addMouseMotionListener(this);
		pane.addMouseWheelListener(this);
		pane.addMouseListener(this.cableCreationListener);
		pane.addMouseMotionListener(this.cableCreationListener);
		// TODO: Shortcut handling should be done by other class
				InputMap inputMap = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
				ActionMap actionMap = pane.getActionMap();
				
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
		// TODO: think about adding Cables as CableComponents instead of paint graphic shapes
		ShapeListener sl = new ShapeListener(this);
		pane.addMouseListener(sl);
		pane.addMouseMotionListener(sl);
		pane.addMouseWheelListener(sl);
		pane.addMouseListener(this.popupMenuListener);
		
		return pane;
	}
	
	public MidiGraph getGraph(){
		return this.graph;
	}
	
	public void add(InteractiveModule m){
		this.pane.add(m);
		this.graph.add(m.getModule());
	}
	
	public void remove(InteractiveModule m){
		this.pane.remove(m);
		this.graph.remove(m.getModule());
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
		//just read the MIDI Graph, populate the pane! (transient pane) TODO
		this.moduleListener = new ModuleListener(this);
		this.popupMenuListener = new PopupMenuListener(this);
		this.shapeListener = new ShapeListener(this);
		this.cableCreationListener = new CableCreationListener(this);
		this.cableAddProcess = false;
		this.componentAndViewDrag = false;
		this.actionManager = new UserActionManager();
		this.actionManager.setController(this);
		this.pane = this.initPane(new InteractivePane());
		Populator.populateWith(this,this.graph);
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	public void setCableAddProcessSource(CablePointHost source) {
		this.cableAddProcessSource = source;
		
	}
	
	public CablePointHost getCableAddProcessSource(){
		return this.cableAddProcessSource;
	}
	public boolean close() {
		for(Component c:this.pane.getComponents()){
			if(c instanceof InteractiveModule){
				((InteractiveComponent) c).close();
			}
		}
		return true;
	}
	
	public boolean reOpen() {
		for(Component c:this.pane.getComponents()){
			if(c instanceof InteractiveModule){
				((InteractiveComponent) c).reopen();
			}
		}
		return true;
		
	}

	

}
