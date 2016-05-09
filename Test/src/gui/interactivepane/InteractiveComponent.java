package gui.interactivepane;

import guiinterface.InteractiveUpdateable;
import guiinterface.SizeableComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import controller.interactivepane.CableCreationListener;
import controller.interactivepane.InteractiveController;
import controller.interactivepane.ModuleListener;
import controller.interactivepane.PopupMenuListener;

public abstract class InteractiveComponent extends JPanel implements InteractiveUpdateable {

	private Vector originLocation;
	private Dimension originDimension;
	private boolean selected;
	private boolean hovered;
	protected InteractiveController controller;
	
	/**
	 * creates a new interactive gui component and places it to the origin vector
	 * @param parent
	 * @param origin
	 */
	public InteractiveComponent(InteractiveController controller, Vector origin){
		this.originLocation = origin;
		this.selected = false;
		this.hovered=false;
		this.originDimension = new Dimension();
		this.setController(controller);
		this.setOpaque(false);
	}
	
	public void setController(InteractiveController controller){
		if(this.controller != null){
			for(MouseListener l: this.getMouseListeners()){
				this.removeListeners(l);
			}
		}
//			this.removeListeners(this.controller.getModuleListener(), this.controller.getPopupMenuListener(), this.controller.getCableCreationListener());
		this.controller = controller;
		this.addListeners(controller.getModuleListener(), controller.getPopupMenuListener(),controller.getCableCreationListener());
	}
	
	public void removeListeners(MouseListener... listeners) {
		for(EventListener l: listeners){
			
			if(l instanceof MouseListener){
				this.removeMouseListener((MouseListener) l);
			}
			if (l instanceof MouseMotionListener){
				this.removeMouseMotionListener((MouseMotionListener) l);
			}
			if (l instanceof MouseWheelListener){
				this.removeMouseWheelListener((MouseWheelListener) l);
			}
		}
		
	}

	public InteractiveController getController(){
		return this.controller;
	}
	
	public Dimension getOriginDimension(){
		return this.originDimension;
	}
		public Vector getOriginLocation(){
			return this.originLocation;
		}
	
	public void translateOriginLocation(Vector translationVector){
		this.originLocation = this.originLocation.addVector(translationVector);
		this.updateView();
	}
	
	public void setOriginLocation(Vector location){
		this.originLocation = location;
	}
	
	protected void setOriginDimension(Dimension d){
		this.originDimension = d;
	}
	
	public void updateView(){
		
		//set components screen location
		this.setLocation(controller.getPane().convertToScreenLocation(this.originLocation).toPoint());
		//size component
		double scaleFactor = this.controller.getPane().getScaleFactor();
		this.setSize((int)(this.originDimension.width*scaleFactor), (int) (this.originDimension.height*scaleFactor));
	}
	
	private boolean navigating;
	public boolean navigating(){
		return this.navigating;
	}
	public void setHovered(boolean set){
		this.hovered = set;
		this.navigating = set;
		this.repaint();
	}
	
	public void setSelected(boolean set){
		this.selected = set;
		this.repaint();
	}


	public boolean isSelected(){
		return this.selected;
	}
	
	public boolean isHovered(){
		return this.hovered;
	}
	
	
	public boolean intersects(Shape s){
		return s.intersects(this.getBounds());
	}
	
	public void addListeners(EventListener... listener){
		for(EventListener l: listener){
			if(l instanceof MouseListener){
				this.addMouseListener((MouseListener) l);
			} 
			if (l instanceof MouseMotionListener){
				this.addMouseMotionListener((MouseMotionListener) l);
			} 
			if (l instanceof MouseWheelListener){
				this.addMouseWheelListener((MouseWheelListener) l);
			}
		}
	}
	

	public abstract	boolean close();
	public abstract boolean reopen();

	private void writeObject(ObjectOutputStream out) throws IOException {
		for(MouseListener l: this.getMouseListeners()){
			this.removeListeners(l);
		}
		out.defaultWriteObject();
		this.addListeners(this.controller.getModuleListener(), this.controller.getPopupMenuListener(), this.controller.getCableCreationListener());
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		//TODO: further debugging


	}
}
