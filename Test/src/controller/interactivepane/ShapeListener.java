package controller.interactivepane;

import gui.interactivepane.InteractiveShape;

import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class ShapeListener extends ControllerListenerAdapter{
	private boolean inPane;
	public ShapeListener(InteractiveController controller) {
		super(controller);
		inPane = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		inPane = true;
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		inPane = false;
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		
		if(inPane){
			
			LinkedList<InteractiveShape> shapes = controller.getPane().getShapes();
			for (InteractiveShape s:shapes){
				if(s.contains(e.getPoint())){
					controller.getPane().setShapeHovered(s, true);
				} else {
					controller.getPane().setShapeHovered(s, false);
				}
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
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
