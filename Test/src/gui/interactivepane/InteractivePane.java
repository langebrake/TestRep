package gui.interactivepane;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class InteractivePane extends JLayeredPane {

	
	
	
	/*
	 * Objects needed for viewport rendering
	 */
	private Vector scaleOrigin;
	private float scaleFactor;
	private float scaleIncrement;
	private float scaleMin;
	private float scaleMax;
	// set Coordinate origin (0|0) to this position
	private Vector viewportTranslation;

	
	/*
	 * Cables
	 */
	private LinkedList<InteractiveShape> shapes;
	private LinkedList<InteractiveShape> selectedShapes;
	private LinkedList<InteractiveShape> tmpSelectedShapes;
	private LinkedList<InteractiveCableComponent> cableComponents;
	
	
	/*
	 * Component interaction
	 */
	private LinkedList<InteractiveComponent> selectedComponents;
	private LinkedList<InteractiveComponent> tmpSelectedComponents;
	private JPanel selectionArea;
	
	
	/**
	 * Constructs an interactive Gui Pane
	 */
	public InteractivePane(){
		this.setPreferredSize(new Dimension(1000,1000));
		
		//set viewport default variables
		this.cableComponents = new LinkedList<InteractiveCableComponent>();
		this.scaleOrigin = new Vector(0,0);
		this.scaleFactor = 1;
		this.scaleIncrement = (float) 0.1;
		this.scaleMin = (float) 0.01;
		this.scaleMax = 3;
		this.viewportTranslation = new Vector(0,0);
		this.shapes = new LinkedList<InteractiveShape>();
		this.selectedComponents = new LinkedList<InteractiveComponent>();
		this.selectedShapes = new LinkedList<InteractiveShape>();
		this.tmpSelectedShapes = new LinkedList<InteractiveShape>();
		this.tmpSelectedComponents = new LinkedList<InteractiveComponent>();
		this.selectionArea = new JPanel();
		this.selectionArea.setBorder(BorderFactory.createLineBorder(Color.black));
		this.selectionArea.setOpaque(false);
		this.add(selectionArea);
		

		
	}
	
	
	/*
	 * Viewport transformation methods
	 */
	public void translateViewport(Vector translationVectorGrid){
		this.viewportTranslation = this.viewportTranslation.addVector(translationVectorGrid);
		
		this.updateView();
	}
	
	public synchronized void zoomViewport(Vector zoomSourceScreenLocation, double d){
		
		Vector gridLocation = this.convertToGridLocation(zoomSourceScreenLocation);
		if(d>0){
			if(this.scaleFactor - this.scaleIncrement <this.scaleMin){
				return;
			}
			
			this.scaleFactor -=this.scaleIncrement ;
		}
		else if (d <0) {
			if (this.scaleFactor +this.scaleIncrement > this.scaleMax){
				return;
			}
			this.scaleFactor +=this.scaleIncrement ;
		}
		
		this.scaleOrigin = gridLocation;
		this.translateViewport(gridLocation.diffVector(this.convertToGridLocation(zoomSourceScreenLocation)));
	}
	
	public void translateSelection(Vector translationVectorGrid){
		for(InteractiveComponent c:this.getComponentSelection()){
			c.translateOriginLocation(translationVectorGrid);
		}
		for(InteractiveShape s:this.getShapeSelection()){
			s.translateOriginLocation(translationVectorGrid);
		}
		for(InteractiveCableComponent c: this.cableComponents){
			c.updateView();
		}
		this.repaint();
	}
	
	public Vector convertToGridLocation(Vector viewportVector){
		//first untranslate zoomTranslation, than untranslate translation with translationvector*scalefactor, than unscale
		Vector untranslated = this.viewportTranslation.scaleVector(this.scaleFactor).diffVector(viewportVector);
		//P=(P'-S)/sf+S
		Vector untranslatedUnscaled = ((this.scaleOrigin.diffVector(untranslated)).scaleVector(1/this.scaleFactor)).addVector(this.scaleOrigin);
		return untranslatedUnscaled;
	}
	
	public Vector convertToScreenLocation(Vector gridLocationVector){
		//first scale, than translate with translatevector*scalefactor
		
		//P'=S+sf*(P-S)
		Vector scaled  = this.scaleOrigin.addVector((this.scaleOrigin.diffVector(gridLocationVector)).scaleVector(this.scaleFactor));
		//translate + zoomtranslate in one step
		Vector scaledTranslated = scaled.addVector(this.viewportTranslation.scaleVector(this.scaleFactor));
		return scaledTranslated;
	}
	
	public void updateView(){
		Component[] components = this.getComponents();
		for (Component c: components){
			if(c instanceof InteractiveComponent){
			((InteractiveComponent) c).updateView();
			}
		}
		this.revalidate();
	}
	
	
	public void add(InteractiveShape c){
		this.shapes.add(c);
		this.repaint();
	}
	
	public void remove(InteractiveShape c){
		this.shapes.remove(c);
		this.repaint();
	}
	
	public void remove(InteractiveComponent c){
		if(c instanceof InteractiveCableComponent){
			this.cableComponents.remove(c);
		}
		super.remove(c);
		this.repaint();
	}
	
	public void add(InteractiveComponent c){
		if(c instanceof InteractiveCableComponent){
			this.cableComponents.add((InteractiveCableComponent) c);
		}
		super.add(c);
		c.updateView();
		this.moveToFront(c);
		this.repaint();
		
	}
	

	
	public void setComponentSelected(InteractiveComponent component, boolean selected){
		component.setSelected(selected);
		if(selected){
			this.selectedComponents.add(component);
		} else {
			this.selectedComponents.remove(component);
		}
		
	}
	
	public LinkedList<InteractiveComponent> getComponentSelection(){
		return this.selectedComponents;
	}
	
	public LinkedList<InteractiveShape> getShapeSelection(){
		return this.selectedShapes;
	}
	
	public void setShapeSelected(InteractiveShape shape, boolean selected){
		shape.setSelected(selected);
		if(selected){
			this.selectedShapes.add(shape);
		} else {
			this.selectedShapes.remove(shape);
		}
		this.repaint();
		
	}
	
	public void setComponentHovered(InteractiveComponent component, boolean hovered){
		component.setHovered(hovered);
	}
	
	public void setShapeHovered(InteractiveShape shape, boolean hovered){
		shape.setHovered(hovered);
		this.repaint();
	}
	public void clearSelection(){
		for(InteractiveComponent c:this.selectedComponents){
			c.setSelected(false);
		}
		this.selectedComponents = new LinkedList<InteractiveComponent>();
		for(InteractiveShape c:this.selectedShapes){
			c.setSelected(false);
		}
		this.repaint();
		this.selectedShapes = new LinkedList<InteractiveShape>();
		
	}
	
	public boolean hasSelected(){
		return this.selectedShapes.size()+this.selectedComponents.size()!=0;
	}
	
	public boolean hasMultiSelected(){
		return this.selectedShapes.size()+this.selectedComponents.size()>1;
	}
	
	/**
	 * 
	 * @param upperLeft
	 * @param lowerRight
	 * @param additive
	 */
	public void selectionArea(Vector vec1, Vector vec2, boolean additive){
		int xpos,ypos,height,width;
		xpos = Math.min(vec1.getX(), vec2.getX());
		ypos = Math.min(vec1.getY(), vec2.getY());
		height = Math.max(vec1.getY(), vec2.getY()) - ypos;
		width = Math.max(vec1.getX(), vec2.getX()) - xpos;
		this.selectionArea.setVisible(true);
		this.selectionArea.setBounds(xpos, ypos, width, height);
		this.moveToFront(selectionArea);
		
		//TODO: better area selection algorithm!
		Rectangle2D selectionRect = this.selectionArea.getBounds();
		for(Component c:this.getComponents()){
			
			if(c instanceof InteractiveComponent){
				if(((InteractiveComponent)c).intersects(selectionRect)){
					if(!((InteractiveComponent) c).isSelected()){
						((InteractiveComponent) c).setSelected(true);
						this.tmpSelectedComponents.add((InteractiveComponent) c);
					}
					
				} else {
					if(!this.selectedComponents.contains(((InteractiveComponent) c))){
						((InteractiveComponent) c).setSelected(false);
						this.tmpSelectedComponents.remove(c);
					}
				}
			}
		}
		for(InteractiveShape c: this.shapes){
			if(c.intersects(selectionRect)){
				
				if(!c.isSelected()){
					c.setSelected(true);
					this.tmpSelectedShapes.add(c);
					this.repaint();
				}
			} else {
				// TODO: Field tmpselected will give better performance than calling contains!
				if(!this.selectedShapes.contains(c)){
					c.setSelected(false);
					this.tmpSelectedShapes.remove(c);
					this.repaint();
				}
			}
			}
		
		
	}
	
	public void commitSelection(){
		this.selectedComponents.addAll(this.tmpSelectedComponents);
		this.tmpSelectedComponents = new LinkedList<InteractiveComponent>();
		this.selectedShapes.addAll(this.tmpSelectedShapes);
		this.tmpSelectedShapes = new LinkedList<InteractiveShape>();
		this.selectionArea.setVisible(false);
	}
	
	
	
	
	public Vector getScaleOrigin(){
		return this.scaleOrigin;
	}
	
	public float getScaleFactor(){
		return this.scaleFactor;
	}
	
	public Vector getViewportTranslation(){
		return this.viewportTranslation;
	}
	
	
	
	public LinkedList<InteractiveShape> getShapes(){
		return this.shapes;
	}
	

	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(g instanceof Graphics2D){
			RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHints(rh);
			for(InteractiveShape c: this.shapes){
				c.updateView(g2d);
			}
		}
		
	}
	
}
