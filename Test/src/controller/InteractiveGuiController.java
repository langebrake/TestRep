package controller;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
	
	public InteractiveGuiController(InteractiveGuiPane interactivePane, MidiGraph midiGraph){
		this.interactivePane = interactivePane;
		this.midiGraph = midiGraph;
		
		this.interactivePane.addMouseListener(this);
		this.interactivePane.addMouseMotionListener(this);
		this.interactivePane.addMouseWheelListener(this);
		// TODO: Shortcut handling to other class
		KeyStroke deleteCode = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
		this.interactivePane.getInputMap().put(deleteCode, "deletePerformed");
		AbstractAction deleteAction = new DeleteAction();
		this.interactivePane.getActionMap().put("deletePerformed", deleteAction);
	}
	
	
	@Override
	public void mousePressed(MouseEvent e){
		Object source = e.getSource();
		lastMouseGridLocation = this.interactivePane.convertToGridLocation(new Vector(e.getPoint()));
		if(SwingUtilities.isLeftMouseButton(e)){
			if(source instanceof InteractiveGuiComponent){
				
				
				
			
			
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		Object source = arg0.getSource();
		if(source == this.interactivePane){
			if(SwingUtilities.isLeftMouseButton(arg0)){
				InteractiveGuiComponent newComponent = new InteractiveGuiComponent(this.interactivePane, this.interactivePane.convertToGridLocation(new Vector(arg0.getPoint())));
				newComponent.addMouseListener(this);
				newComponent.addMouseMotionListener(this);
				this.interactivePane.addInteractiveGuiComponent(newComponent);
			}
		} else if (source instanceof InteractiveGuiComponent){
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
	
	public void mouseDragged(MouseEvent e){
		Object source = e.getSource();
		Vector currentMouseGridLocation = this.interactivePane.convertToGridLocation(new Vector(e.getPoint()));
		
		if(source == this.interactivePane){
			
			if(SwingUtilities.isLeftMouseButton(e)){
				//translate on origin Grid coordinates
				this.interactivePane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
			}
			
		} else if (source instanceof InteractiveGuiComponent){
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
