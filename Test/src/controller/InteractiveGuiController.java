package controller;

import java.awt.BorderLayout;
import java.awt.Color;
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
import gui.InteractiveCable;
import gui.InteractiveComponent;
import gui.InteractiveModule;
import gui.InteractivePane;
import gui.InteractiveShape;
import gui.Vector;

public class InteractiveGuiController extends MouseAdapter {
	private InteractivePane interactivePane;
	private MidiGraph midiGraph;
	private Vector lastMouseGridLocation;
	private Vector lastMouseScreenLocation;
	
	
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
		
		
		//TODO: delete this debug add
		int min = -10000;
		int max = 10000;
		for(int i = 1; i<1000; i++){
			JPanel tmp = new JPanel();
			//TODO : delete the random color thing when implementing modules
			Random rand = new Random();
			tmp.setBackground(new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
			tmp.setSize(rand.nextInt(150)+50, rand.nextInt(150)+50);
			InteractiveComponent c = new InteractiveModule(this.interactivePane,new Vector(ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max)), tmp);
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
			this.interactivePane.add(c);
		}
		
		CablePoint p1 = new CablePoint();
		CablePoint p2 = new CablePoint();
		JPanel panel1 = new JPanel(new GridBagLayout());
		JPanel panel2 = new JPanel(new GridBagLayout());
		
		panel1.setSize(50, 50);
		panel2.setSize(50, 50);
		panel1.setBackground(Color.LIGHT_GRAY);
		panel2.setBackground(Color.LIGHT_GRAY);
		
		panel1.add(p1);
		panel2.add(p2);
		InteractiveComponent src = new InteractiveModule(this.interactivePane,new Vector(0,0), panel1);
		InteractiveComponent src2 = new InteractiveModule(this.interactivePane,new Vector(500,900), panel2);
		src.addMouseListener(this);
		src2.addMouseListener(this);
		src.addMouseMotionListener(this);
		src2.addMouseMotionListener(this);
		this.interactivePane.add(src);
		this.interactivePane.add(src2);
		this.interactivePane.add(new InteractiveCable(p1,p2,this.interactivePane));
	}
	
	
	@Override
	public void mousePressed(MouseEvent e){
		Object source = e.getSource();
		lastMouseScreenLocation = new Vector(e.getLocationOnScreen());
		lastMouseGridLocation = this.currentMouseGridLocation(e);
		
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
				InteractiveComponent newComponent = new InteractiveModule(this.interactivePane, this.interactivePane.convertToGridLocation(new Vector(arg0.getPoint())),defaultPlugin.getMinimizedView());
				newComponent.addMouseListener(this);
				newComponent.addMouseMotionListener(this);
				this.interactivePane.add(newComponent);
			}
		} else if (source instanceof InteractiveComponent){
			if(SwingUtilities.isLeftMouseButton(arg0)){
				if(arg0.getClickCount() == 2) {
					
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
					this.selectionAction((new Vector(this.interactivePane.getLocationOnScreen())).diffVector(this.lastMouseScreenLocation), new Vector(e.getPoint()), true);
					
			}
			
		} else if (source instanceof InteractiveComponent){
			if(SwingUtilities.isMiddleMouseButton(e)){
				
				this.interactivePane.translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
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
