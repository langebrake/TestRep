package controller;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import model.MidiGraph;
import gui.InteractiveGuiComponent;
import gui.InteractiveGuiPane;
import gui.Vector;

public class InteractiveGuiController extends MouseAdapter {
	private InteractiveGuiPane interactivePane;
	private MidiGraph midiGraph;
	private Vector lastMouseGridLocation;
	private Vector lastMouseScreenLocation;
	
	public InteractiveGuiController(InteractiveGuiPane interactivePane, MidiGraph midiGraph){
		this.interactivePane = interactivePane;
		this.midiGraph = midiGraph;
		
		this.interactivePane.addMouseListener(this);
		this.interactivePane.addMouseMotionListener(this);
		this.interactivePane.addMouseWheelListener(this);
		
		// TODO: Shortcut handling should be done by other class
		KeyStroke deleteCode = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
		this.interactivePane.getInputMap().put(deleteCode, "deletePerformed");
		AbstractAction deleteAction = new DeleteAction();
		this.interactivePane.getActionMap().put("deletePerformed", deleteAction);
		
		
		//TODO: delete this debug add
		int min = -10000;
		int max = 10000;
		for(int i = 1; i<1000; i++){
			InteractiveGuiComponent c = new InteractiveGuiComponent(this.interactivePane,new Vector(ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max)));
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
			this.interactivePane.addInteractiveGuiComponent(c);
		}
	}
	
	
	@Override
	public void mousePressed(MouseEvent e){
		Object source = e.getSource();
		lastMouseScreenLocation = new Vector(e.getLocationOnScreen());
		lastMouseGridLocation = this.interactivePane.convertToGridLocation(new Vector(e.getPoint()));
		
		if(SwingUtilities.isLeftMouseButton(e) 
				&& (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == 0 
				&& (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0){
			if(source == this.interactivePane){
				this.interactivePane.clearSelection();
			}
		}
		
	}
	
	@Override 
	public void mouseReleased(MouseEvent e){
		this.interactivePane.resetSelectionArea();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		Object source = arg0.getSource();
		if(source == this.interactivePane){
			if(SwingUtilities.isRightMouseButton(arg0)){
				InteractiveGuiComponent newComponent = new InteractiveGuiComponent(this.interactivePane, this.interactivePane.convertToGridLocation(new Vector(arg0.getPoint())));
				newComponent.addMouseListener(this);
				newComponent.addMouseMotionListener(this);
				this.interactivePane.addInteractiveGuiComponent(newComponent);
			}
		} else if (source instanceof InteractiveGuiComponent){
			if(SwingUtilities.isLeftMouseButton(arg0)){
				if(arg0.getClickCount() == 2) {
					
				}
				
				boolean componentWasSelected = ((InteractiveGuiComponent) source).isSelected();
				boolean userMultiSelect = (arg0.getModifiers() & InputEvent.SHIFT_MASK) != 0;
				boolean paneHasSelection = this.interactivePane.hasSelected();
				boolean paneHasMultiSelection = this.interactivePane.hasMultiSelection();
				
				if(userMultiSelect){
					if(componentWasSelected){
						this.interactivePane.setComponentSelection((InteractiveGuiComponent)source,false);
					} else {
						this.interactivePane.setComponentSelection((InteractiveGuiComponent)source,true);
					}
				} else  {
					if(paneHasSelection){
						this.interactivePane.clearSelection();
					}
					if(componentWasSelected){
						if(paneHasMultiSelection){
							this.interactivePane.setComponentSelection((InteractiveGuiComponent)source,true);
						}else{
							this.interactivePane.setComponentSelection((InteractiveGuiComponent)source,false);
						}
					} else {
						this.interactivePane.setComponentSelection((InteractiveGuiComponent)source,true);
					}
				}
			}
		}

	}
	
	public void mouseDragged(MouseEvent e){
		Object source = e.getSource();
		Vector currentMouseGridLocation = this.interactivePane.convertToGridLocation(new Vector(e.getPoint()));
		
		
		if(source == this.interactivePane){
			
			if(SwingUtilities.isLeftMouseButton(e) && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0  || SwingUtilities.isMiddleMouseButton(e)){
				this.interactivePane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
			} else if(SwingUtilities.isLeftMouseButton(e) && (e.getModifiers() & InputEvent.SHIFT_MASK) == 0){
					this.selectionAction((new Vector(this.interactivePane.getLocationOnScreen())).diffVector(this.lastMouseScreenLocation), new Vector(e.getPoint()), true);
			}else if(SwingUtilities.isLeftMouseButton(e) && (e.getModifiers() & InputEvent.SHIFT_MASK) != 0){
				this.selectionAction((new Vector(this.interactivePane.getLocationOnScreen())).diffVector(this.lastMouseScreenLocation), new Vector(e.getPoint()), true);
			}
			
		} else if (source instanceof InteractiveGuiComponent){
			if(SwingUtilities.isMiddleMouseButton(e)){
				//TODO : implement translate view while translate component functionality
			}else{
				if(!((InteractiveGuiComponent) source).isSelected()){
						this.interactivePane.clearSelection();
					this.interactivePane.setComponentSelection((InteractiveGuiComponent) source,true);
				}
				Vector translation = lastMouseGridLocation.diffVector(currentMouseGridLocation);
				for(InteractiveGuiComponent c:this.interactivePane.getComponentSelection()){
					c.translateOriginLocation(translation);
				}
			}
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e){
		this.interactivePane.zoomViewport((new Vector(e.getPoint())),e.getWheelRotation());
	}
	
	public void mouseEntered(MouseEvent e){
		Object source = e.getSource();
		if(source instanceof InteractiveGuiComponent){
			((InteractiveGuiComponent) source).setHover(true);
		}
		
	}
	
	public void mouseExited(MouseEvent e){
		Object source = e.getSource();
		if(source instanceof InteractiveGuiComponent){
			((InteractiveGuiComponent) source).setHover(false);
		}
	}

	private void selectionAction(Vector upperLeft, Vector lowerRight, boolean additive){
		this.interactivePane.selectionArea(upperLeft, lowerRight, additive);
	}

	
	
	private class DeleteAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO : move delete selection process to InteractiveGuiPane class!
			for(InteractiveGuiComponent c:interactivePane.getComponentSelection()){
				interactivePane.remove(c);
			}
			interactivePane.clearSelection();
			
			
		}
		
	}
}
