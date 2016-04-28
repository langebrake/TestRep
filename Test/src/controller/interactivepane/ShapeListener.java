package controller.interactivepane;

import gui.interactivepane.InteractiveShape;
import gui.interactivepane.Vector;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class ShapeListener extends MouseAdapter{
	private boolean inPane;
	private InteractiveController controller;
	public ShapeListener(InteractiveController controller) {
		this.controller = controller;
		inPane = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		inPane = controller.getPane().contains(controller.relativeToPane(e).toPoint());
		if(inPane){
			Vector paneVector  = controller.relativeToPane(e);
			LinkedList<InteractiveShape> shapes = controller.getPane().getShapes();
			
			for (InteractiveShape s:shapes){
					if(s.contains(paneVector.toPoint())){
						controller.getPane().setShapeHovered(s, true);
					} else {
						controller.getPane().setShapeHovered(s, false);
					}
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		//TODO: seleactable with SHIFT+click!!
		if(inPane){
			LinkedList<InteractiveShape> shapes = controller.getPane().getShapes();
			for (InteractiveShape s:shapes){
				if(s.contains(e.getPoint())){
					controller.getPane().setShapeSelected(s, true);
				} else {
					controller.getPane().setShapeSelected(s, false);
				}
			}
		}
	}
	
}
