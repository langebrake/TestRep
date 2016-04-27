package controller.interactivepane;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointComponent;
import gui.interactivepane.CablePointPanel;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.Vector;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.history.actions.UserAddConnectionsAction;

public class CableCreationListener extends MouseAdapter{
	private CablePointComponent tmpPoint;
	private CablePoint sourcePoint;
	private InteractiveCable tmpCable;
	private InteractiveController controller;
	private HashMap<InteractiveCable,InteractiveCable[]> newConnections;
	private InteractiveCable oldSourceConnection;
	public CableCreationListener(InteractiveController c) {
		this.controller = c;
		this.newConnections = new HashMap<InteractiveCable,InteractiveCable[]>();
	}
	
	public void mousePressed(MouseEvent e){
		Object source = searchSourceRecursive(e,controller.getPane(),CablePoint.class);
		if( validInteraction(e) && source instanceof CablePoint){
			sourcePoint = (CablePoint) source;
			if(sourcePoint.isConnected()){
				//TODO: Reset old Connection and connect new cable, but keep the old connection saved in case an invalid release happens.
				// the old connection should be restored int that case
				//Mind the USER ACTION!!
				this.oldSourceConnection = sourcePoint.getCable();
				controller.getPane().remove(this.oldSourceConnection);
			}
			tmpPoint = new CablePointComponent(controller.getPane(),CablePointType.THROUGH);
			tmpPoint.setLocation(controller.relativeToPane(e).toPoint());
			tmpCable = new InteractiveCable(sourcePoint,tmpPoint,controller.getPane());
			tmpPoint.setCable(tmpCable);
			sourcePoint.setCable(tmpCable);
			tmpCable.setDraggedEndpoint(true);
			controller.getPane().add(tmpCable);
			controller.getPane().add(tmpPoint);

		}
	}
	
	public void mouseReleased(MouseEvent e){
		if(tmpPoint != null && SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isMiddleMouseButton(e)){
			Component source = searchSourceRecursive(e,controller.getPane(),CablePoint.class);
			if(source instanceof CablePoint && ((CablePoint) source).getType()!=sourcePoint.getType())
			{
				CablePoint destPoint = (CablePoint) source;
				tmpCable.setDraggedEndpoint(false);
				if(!destPoint.isConnected()){
					tmpCable.setDestination(destPoint);
					controller.getPane().remove(tmpCable);
					InteractiveCable[] overrides = null;
					if(this.oldSourceConnection != null){
						overrides = new InteractiveCable[1];
						overrides[0] = this.oldSourceConnection;
					}
					this.newConnections.put(tmpCable, overrides);
					
					
				} else {
					tmpCable.setDestination(destPoint);
					controller.getPane().remove(tmpCable);
					InteractiveCable[] overrides = null;
					if(this.oldSourceConnection != null){
						overrides = new InteractiveCable[2];
						overrides[0] = this.oldSourceConnection;
						overrides[1] = destPoint.getCable();
					}else {
						overrides = new InteractiveCable[1];
						overrides[0] = destPoint.getCable();
					}
					this.newConnections.put(tmpCable, overrides);
					//TODO: disconnect old connection, create new, mind the UserAction
				}
				
				this.controller.executeAction(new UserAddConnectionsAction(controller.getActionManager(),newConnections));
			} else {
				sourcePoint.disconnect();
				controller.getPane().remove(tmpCable);
			}
			controller.getPane().remove(tmpPoint);
			tmpPoint = null;
			this.oldSourceConnection = null;
			this.newConnections = new HashMap<InteractiveCable, InteractiveCable[]>();
		} 
	}
	
	public void mouseDragged(MouseEvent e){
		if(tmpPoint != null ){
			tmpPoint.setLocation(controller.relativeToPane(e).toPoint());
			controller.getPane().repaint();
		}
	}
	
	public void mouseWheelMoved(MouseEvent e){
		if(tmpPoint != null && validInteraction(e)){
			tmpPoint.setLocation(controller.relativeToPane(e).toPoint());
			controller.getPane().repaint();
		}
	}
	
	private Component searchSourceRecursive(MouseEvent e, Component c, Class<?> searchFor){
		if(c.isShowing()){
			Component tmp = c.getComponentAt((new Vector(c.getLocationOnScreen())).diffVector((new Vector(e.getLocationOnScreen()))).toPoint());
			if( searchFor.isInstance(c)){
				return c;
			} else if(tmp == null || c == tmp) {
				return null;
			} else {
				return searchSourceRecursive(e,tmp, searchFor);
			}
		} else {
			return null;
		}
	}
	
	
	private boolean validInteraction(MouseEvent e){
		return SwingUtilities.isLeftMouseButton(e)
				&& (e.isControlDown() && e.isShiftDown())
				&& !SwingUtilities.isMiddleMouseButton(e);
	}
	
	
}
