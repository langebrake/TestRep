package controller.interactivepane;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

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
	public void mousePressed(MouseEvent arg0){
		if(!SwingUtilities.isMiddleMouseButton(arg0)){
		controller.setDragged(true);
		}
		
		controller.updateLastMouseLocation(arg0);
	}
	
	public void mouseReleased(MouseEvent arg0){
		if(!SwingUtilities.isMiddleMouseButton(arg0)){
		controller.setDragged(false);
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0) && !SwingUtilities.isMiddleMouseButton(arg0)){
			InteractiveComponent source = (InteractiveComponent) arg0.getSource();
			if(!((InteractiveComponent) source).isSelected()){
				if( !arg0.isShiftDown()){
					controller.getPane().clearSelection();
					
				}
				controller.getPane().setComponentSelected((InteractiveComponent) source,true);
			}
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
		controller.getPane().setComponentHovered((InteractiveComponent) arg0.getSource(), true);
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		controller.getPane().setComponentHovered((InteractiveComponent) arg0.getSource(), false);
		
	}

	
	@Override
	public void mouseClicked(MouseEvent arg0){
		Object source = arg0.getSource();
		if(source instanceof InteractiveComponent){
			if(arg0.getClickCount() == 2){
				if (source instanceof InteractiveModule){
					//TODO: Save all active Frames into one Hashmap to be able to hide all
					((InteractiveModule) source).getModule().getPlugin().getFullView().setVisible(true);
				}
			}
			boolean componentWasSelected = ((InteractiveComponent) source).isSelected();
			boolean userMultiSelect = (arg0.getModifiers() & InputEvent.SHIFT_MASK) != 0;
			boolean paneHasSelection = controller.getPane().hasSelected();
			boolean paneHasMultiSelection = controller.getPane().hasMultiSelected();
			
			if(userMultiSelect){
				if(componentWasSelected){
					controller.getPane().setComponentSelected((InteractiveComponent)source,false);
				} else {
					controller.getPane().setComponentSelected((InteractiveComponent)source,true);
				}
			} else  {
				if(paneHasSelection){
					controller.getPane().clearSelection();
				}
				if(componentWasSelected){
					if(paneHasMultiSelection){
						controller.getPane().setComponentSelected((InteractiveComponent)source,true);
					}else{
						controller.getPane().setComponentSelected((InteractiveComponent)source,false);
					}
				} else {
					controller.getPane().setComponentSelected((InteractiveComponent)source,true);
				}
			}
		} else {
			controller.mouseClicked(arg0);
		}
	}
	
}
