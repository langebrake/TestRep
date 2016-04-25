package controller;

import gui.InteractiveComponent;
import gui.InteractiveModule;
import gui.Vector;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class ModuleListener extends ControllerListenerAdapter {
	public ModuleListener(InteractiveController c){
		super(c);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0) && !SwingUtilities.isMiddleMouseButton(arg0)){
			Vector currentMouseGridLocation = controller.toGridCoordinate(controller.relativeToPane(arg0));
			
			Vector translation = controller.getLastMouseGridLocation().diffVector(currentMouseGridLocation);

			controller.getPane().translateSelection(translation);
			controller.updateLastMouseLocation(arg0);
			
		} else {
		
			controller.mouseDragged(arg0);
		}
		
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
		
		if(SwingUtilities.isLeftMouseButton(arg0)){
			controller.updateLastMouseLocation(arg0);
			if(!((InteractiveComponent)arg0.getSource()).isSelected()){
				if(!arg0.isShiftDown()){
					controller.getPane().clearSelection();
				}
				controller.getPane().setComponentSelection((InteractiveComponent) arg0.getSource(),true);
			}
			
		} else {
			controller.mousePressed(arg0);
		}
		
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0){
		Object source = arg0.getSource();
		if(source instanceof InteractiveComponent){
			boolean componentWasSelected = ((InteractiveComponent) source).isSelected();
			boolean userMultiSelect = (arg0.getModifiers() & InputEvent.SHIFT_MASK) != 0;
			boolean paneHasSelection = controller.getPane().hasSelected();
			boolean paneHasMultiSelection = controller.getPane().hasMultiSelected();
			
			if(userMultiSelect){
				if(componentWasSelected){
					controller.getPane().setComponentSelection((InteractiveComponent)source,false);
				} else {
					controller.getPane().setComponentSelection((InteractiveComponent)source,true);
				}
			} else  {
				if(paneHasSelection){
					controller.getPane().clearSelection();
				}
				if(componentWasSelected){
					if(paneHasMultiSelection){
						controller.getPane().setComponentSelection((InteractiveComponent)source,true);
					}else{
						controller.getPane().setComponentSelection((InteractiveComponent)source,false);
					}
				} else {
					controller.getPane().setComponentSelection((InteractiveComponent)source,true);
				}
			}
		} else {
			controller.mouseClicked(arg0);
		}
	}
	
}
