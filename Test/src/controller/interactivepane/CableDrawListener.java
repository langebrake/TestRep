package controller.interactivepane;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointComponent;
import gui.interactivepane.InteractiveCable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CableDrawListener extends MouseAdapter{
	private CablePointComponent tmpPoint;
	private CablePoint sourcePoint;
	private InteractiveCable tmpCable;
	private InteractiveController controller;
	public CableDrawListener(InteractiveController c) {
		this.controller = c;
	}
	
	public void mousePressed(MouseEvent e){
		Object source = e.getSource();
		if(source instanceof CablePoint){
			sourcePoint = (CablePoint) source;
			if(!sourcePoint.isConnected()){
				tmpPoint = new CablePointComponent(controller.getPane());
				tmpCable = new InteractiveCable(sourcePoint,tmpPoint,controller.getPane());
				tmpPoint.setCable(tmpCable);
				sourcePoint.setCable(tmpCable);
				controller.getPane().add(tmpCable);
				controller.getPane().add(tmpPoint);
			}
		}
	}
	
	public void mouseReleased(MouseEvent e){
		if(controller.getPane().getComponentAt(controller.relativeToPane(e).toPoint()) instanceof CablePoint)
		{
			CablePoint dest = (CablePoint) controller.getPane().getComponentAt(controller.relativeToPane(e).toPoint());
			tmpCable.setDestination(dest);
			dest.setCable(tmpCable);
		} else {
			sourcePoint.setCable(null);
			controller.getPane().remove(tmpCable);
		}
		controller.getPane().remove(tmpPoint);
		tmpPoint = null;
	}
	
}
