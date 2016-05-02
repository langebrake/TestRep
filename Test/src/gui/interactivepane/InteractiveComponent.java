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
import java.util.EventListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class InteractiveComponent extends JPanel implements InteractiveUpdateable {

	private Vector originLocation;
	private Dimension originDimension;
	private InteractivePane parent;
	private boolean selected;
	private boolean hovered;
	
	/**
	 * creates a new interactive gui component and places it to the origin vector
	 * @param parent
	 * @param origin
	 */
	public InteractiveComponent(InteractivePane parent, Vector origin){
		this.parent = parent;
		this.originLocation = origin;
		this.selected = false;
		this.hovered=false;
		this.originDimension = new Dimension();
		this.setOpaque(false);
	
		
		
		
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
	
	protected void setOriginDimension(Dimension d){
		this.originDimension = d;
	}
	
	public void updateView(){
		
		//set components screen location
		this.setLocation(parent.convertToScreenLocation(this.originLocation).toPoint());
		//size component
		double scaleFactor = this.parent.getScaleFactor();
		this.setSize((int)(this.originDimension.width*scaleFactor), (int) (this.originDimension.height*scaleFactor));
	}
	
	public void setHovered(boolean set){
		this.hovered = set;
	}
	
	public void setSelected(boolean set){
		this.selected = set;
	}


	public boolean isSelected(){
		return this.selected;
	}
	
	public boolean isHovered(){
		return this.hovered;
	}
	
	protected InteractivePane getParentPane(){
		return this.parent;
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

	
}
