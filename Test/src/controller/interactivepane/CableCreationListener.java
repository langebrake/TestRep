package controller.interactivepane;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointComponent;
import gui.interactivepane.CablePointPanel;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.Vector;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CableCreationListener extends MouseAdapter{
	private CablePointComponent tmpPoint;
	private CablePoint sourcePoint;
	private InteractiveCable tmpCable;
	private InteractiveController controller;
	public CableCreationListener(InteractiveController c) {
		this.controller = c;
	}
	
	public void mousePressed(MouseEvent e){
		Object source = searchSourceRecursive(e,controller.getPane(),CablePoint.class);
		if( validInteraction(e) && source instanceof CablePoint){
			sourcePoint = (CablePoint) source;
			if(!sourcePoint.isConnected()){
				tmpPoint = new CablePointComponent(controller.getPane());
				tmpPoint.setLocation(controller.relativeToPane(e).toPoint());
				tmpCable = new InteractiveCable(sourcePoint,tmpPoint,controller.getPane());
				tmpPoint.setCable(tmpCable);
				sourcePoint.setCable(tmpCable);
				controller.getPane().add(tmpCable);
				controller.getPane().add(tmpPoint);
			}else{
				//TODO: Reset old Connection and connect new cable, but keep the old connection saved in case an invalid release happens.
				// the old connection should be restored int that case
				//Mind the USER ACTION!!
			}
		}
	}
	
	public void mouseReleased(MouseEvent e){
		if(tmpPoint != null && SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isMiddleMouseButton(e)){
			Component source = searchSourceRecursive(e,controller.getPane(),CablePoint.class);
			if(source instanceof CablePoint && source!=sourcePoint)
			{
				CablePoint tmp = (CablePoint) source;
				if(!tmp.isConnected()){
					tmpCable.setDestination(tmp);
					tmp.setCable(tmpCable);
					controller.getPane().repaint();
					// TODO: the Module needs to be updated based upon the made connection
				} else {
					//TODO: disconnect old connection, create new, mind the UserAction
				}
			} else {
				sourcePoint.setCable(null);
				controller.getPane().remove(tmpCable);
			}
			controller.getPane().remove(tmpPoint);
			tmpPoint = null;
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
