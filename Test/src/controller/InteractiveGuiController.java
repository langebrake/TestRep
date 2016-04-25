package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import defaultplugin.DefaultPlugin;
import plugin.Plugin;
import engine.Engine;
import model.MidiGraph;
import model.graph.Module;
import gui.CablePoint;
import gui.CablePointComponent;
import gui.CablePointPanel;
import gui.InteractiveCable;
import gui.InteractiveComponent;
import gui.InteractiveDisplay;
import gui.InteractiveModule;
import gui.InteractivePane;
import gui.InteractiveShape;
import gui.Vector;

public class InteractiveGuiController extends MouseAdapter {
	private InteractivePane interactivePane;
	private MidiGraph midiGraph;
	private Vector lastMouseGridLocation;
	private Vector lastMouseScreenLocation;
	private CablePointComponent tmpCablePoint;
	
	public InteractiveGuiController(InteractivePane interactivePane, MidiGraph midiGraph){
		this.interactivePane = interactivePane;
		this.midiGraph = midiGraph;
		
		this.interactivePane.addMouseListener(this);
		this.interactivePane.addMouseMotionListener(this);
		this.interactivePane.addMouseWheelListener(this);
		
		// TODO: Shortcut handling should be done by other class
		KeyStroke deleteCode = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
		this.interactivePane.getInputMap().put(deleteCode, "deletePerformed");
		AbstractAction deleteAction = new DeleteAction();
		this.interactivePane.getActionMap().put("deletePerformed", deleteAction);
		
		//TODO: remove this cable debug thing
		CablePointPanel p1 = new CablePointPanel(this.interactivePane);
		CablePointPanel p2 = new CablePointPanel(this.interactivePane);
		p1.setSize(50, 50);
		p2.setSize(50, 50);
		p1.setBackground(Color.LIGHT_GRAY);
		p2.setBackground(Color.LIGHT_GRAY);
		
		InteractiveComponent src = new InteractiveDisplay(this.interactivePane,new Vector(0,0), p1);
		InteractiveComponent src2 = new InteractiveDisplay(this.interactivePane,new Vector(500,900), p2);
		src.addMouseListener(this);
		src2.addMouseListener(this);
		src.addMouseMotionListener(this);
		src2.addMouseMotionListener(this);
		p1.setCable(new InteractiveCable(p1,p2,this.interactivePane));
		p2.setCable(p1.getCable());
		this.interactivePane.add(src);
		this.interactivePane.add(src2);
		this.interactivePane.add(p1.getCable());
	}
	
	
	@Override
	public void mousePressed(MouseEvent e){
		Object source = e.getSource();
		lastMouseScreenLocation = new Vector(this.interactivePane.getLocationOnScreen()).diffVector((new Vector(e.getLocationOnScreen())));
		lastMouseGridLocation = this.currentMouseGridLocation(e);
		
		if(SwingUtilities.isLeftMouseButton(e) 
				&& (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == 0 
				&& (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0){
			if(source == this.interactivePane){
				this.interactivePane.clearSelection();
			}
		}else if(e.getSource() instanceof InteractiveComponent 
				&& SwingUtilities.isLeftMouseButton(e)
				&& (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK)!=0){
			if(((Container) e.getSource()).getComponentAt(e.getPoint()) instanceof CablePointPanel){
				this.tmpCablePoint = new CablePointComponent(this.interactivePane);
			}
		}
		
	}
	
	@Override 
	public void mouseReleased(MouseEvent e){
		this.interactivePane.commitSelection();
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		Object source = arg0.getSource();
		if(source == this.interactivePane){
			if(SwingUtilities.isRightMouseButton(arg0)){
				
				Module mod = null;
				try {
					mod = new Module(Engine.load());
				} catch (MidiUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DefaultPlugin defaultPlugin = new DefaultPlugin(mod);
				mod.setPlugin(defaultPlugin);
				InteractiveComponent newComponent = new InteractiveModule(this.interactivePane, this.interactivePane.convertToGridLocation(new Vector(arg0.getPoint())),mod);
				newComponent.addMouseListener(this);
				newComponent.addMouseMotionListener(this);
				this.interactivePane.add(newComponent);
			}
		} else if (source instanceof InteractiveComponent){
			if(SwingUtilities.isLeftMouseButton(arg0)){
				if(arg0.getClickCount() == 2) {
					if(source instanceof InteractiveModule){
						((InteractiveModule) source).openFullView();
					}
				}
				
				boolean componentWasSelected = ((InteractiveComponent) source).isSelected();
				boolean userMultiSelect = (arg0.getModifiers() & InputEvent.SHIFT_MASK) != 0;
				boolean paneHasSelection = this.interactivePane.hasSelected();
				boolean paneHasMultiSelection = this.interactivePane.hasMultiSelected();
				
				if(userMultiSelect){
					if(componentWasSelected){
						this.interactivePane.setComponentSelection((InteractiveComponent)source,false);
					} else {
						this.interactivePane.setComponentSelection((InteractiveComponent)source,true);
					}
				} else  {
					if(paneHasSelection){
						this.interactivePane.clearSelection();
					}
					if(componentWasSelected){
						if(paneHasMultiSelection){
							this.interactivePane.setComponentSelection((InteractiveComponent)source,true);
						}else{
							this.interactivePane.setComponentSelection((InteractiveComponent)source,false);
						}
					} else {
						this.interactivePane.setComponentSelection((InteractiveComponent)source,true);
					}
				}
			}
		}

	}
	
	public void mouseDragged(MouseEvent e){
		Object source = e.getSource();
		Vector currentMouseGridLocation = this.currentMouseGridLocation(e);
		
		// TODO: REPAINT ALWAYS!
		if(source == this.interactivePane){
			
			if(SwingUtilities.isLeftMouseButton(e) && (e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0  || SwingUtilities.isMiddleMouseButton(e)){
				this.interactivePane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
				
			} else if(SwingUtilities.isLeftMouseButton(e) ){
					this.selectionAction(this.lastMouseScreenLocation, new Vector(e.getPoint()), true);
					
			}
			
		} else if (source instanceof InteractiveComponent){
			if(SwingUtilities.isMiddleMouseButton(e)){
				System.out.println("AHA");
				this.interactivePane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
			}else if (SwingUtilities.isLeftMouseButton(e) && (e.getModifiersEx()&InputEvent.CTRL_DOWN_MASK)!=0){
				if (((InteractiveComponent) source).getComponentAt(e.getPoint()) instanceof CablePointPanel){
					
				}
				
			}else{
				if(!((InteractiveComponent) source).isSelected()){
					if( (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK )== 0)
						this.interactivePane.clearSelection();
					this.interactivePane.setComponentSelection((InteractiveComponent) source,true);
				}
				
				Vector translation = lastMouseGridLocation.diffVector(currentMouseGridLocation);

				this.interactivePane.translateSelection(translation);
				lastMouseGridLocation = currentMouseGridLocation;
			}
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e){
		this.interactivePane.zoomViewport((new Vector(e.getPoint())),e.getWheelRotation());
		lastMouseScreenLocation = new Vector(e.getLocationOnScreen());
		lastMouseGridLocation = this.interactivePane.convertToGridLocation(new Vector(e.getPoint()));
	}
	
	public void mouseEntered(MouseEvent e){
		Object source = e.getSource();
		if(source instanceof InteractiveComponent){
			((InteractiveComponent) source).setHover(true);
		}
		
	}
	
	public void mouseExited(MouseEvent e){
		Object source = e.getSource();
		if(source instanceof InteractiveComponent){
			((InteractiveComponent) source).setHover(false);
		}
	}

	private void selectionAction(Vector upperLeft, Vector lowerRight, boolean additive){
		this.interactivePane.selectionArea(upperLeft, lowerRight, additive);
	}

	private Vector currentMouseGridLocation(MouseEvent e){
		return this.interactivePane.convertToGridLocation(
				(new Vector(this.interactivePane.getLocationOnScreen())).diffVector(
						new Vector(e.getLocationOnScreen())));
	}
	
	
	private class DeleteAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			for(InteractiveComponent c: interactivePane.getComponentSelection()){
				interactivePane.remove(c);
			}
			for(InteractiveShape c:interactivePane.getShapeSelection()){
				interactivePane.remove(c);
				
			}
			interactivePane.clearSelection();
			
			
		}
		
	}
}
