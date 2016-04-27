package controller.interactivepane;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointComponent;
import gui.interactivepane.CablePointHost;
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
		Object source = searchSourceRecursive(e,controller.getPane(),CablePointHost.class);
		if( validInteraction(e) && source instanceof CablePointHost){
			sourcePoint = ((CablePointHost) source).getCablePoint();
			if(sourcePoint.isConnected()){
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
			controller.setCableAddProcess(true);
		}
	}
	
	public void mouseReleased(MouseEvent e){
		
		if(tmpPoint != null && SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isMiddleMouseButton(e)){
			Component source = searchSourceRecursive(e,controller.getPane(),CablePointHost.class);
			if(source instanceof CablePointHost 
					&& ((CablePointHost) source).getCablePoint().getType()!=sourcePoint.getType() 
					&& source!=null)
			{
				CablePoint destPoint = ((CablePointHost) source).getCablePoint();
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
					
					this.controller.executeAction(new UserAddConnectionsAction(controller.getActionManager(),newConnections));
				} else {
					if(! (this.oldSourceConnection != null 
							&&((destPoint == this.oldSourceConnection.getDestination() 
								|| destPoint == this.oldSourceConnection.getSource())))){
						
					
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
						this.controller.executeAction(new UserAddConnectionsAction(controller.getActionManager(),newConnections));
					} else {
						sourcePoint.setCable(this.oldSourceConnection);
						controller.getPane().remove(tmpCable);
						if(this.oldSourceConnection != null){
							controller.getPane().add(this.oldSourceConnection);
						}
					}
				}
				
				
			} else {
				sourcePoint.setCable(this.oldSourceConnection);
				controller.getPane().remove(tmpCable);
				if(this.oldSourceConnection != null){
					controller.getPane().add(this.oldSourceConnection);
				}
			}
			controller.getPane().remove(tmpPoint);
			tmpPoint = null;
			this.oldSourceConnection = null;
			this.newConnections = new HashMap<InteractiveCable, InteractiveCable[]>();
			controller.setCableAddProcess(false);
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
			if( searchFor.isInstance(c)){
				return c;
			} 
			
			Component tmp = c.getComponentAt((new Vector(c.getLocationOnScreen())).diffVector((new Vector(e.getLocationOnScreen()))).toPoint());
			if(tmp == null || c == tmp) {
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
