package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class InteractiveGuiPane extends JLayeredPane {

	
	
	
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
	// to guarantee smooth zooming (if calculated through grid view, rounding errors result in slight jumping)
	private Vector zoomTranslation;
	
	/*
	 * Objects needed for viewport interaction
	 */
	//used for drag translation
	private Vector lastMouseGridLocation;
	
	/*
	 * Cables
	 */
	private LinkedList<Cable> cables;
	private LinkedList<Cable> selectedCables;
	private LinkedList<Cable> tmpSelectedCables;
	
	
	/*
	 * Component interaction
	 */
	private LinkedList<InteractiveGuiComponent> selectedComponents;
	private LinkedList<InteractiveGuiComponent> tmpSelectedComponents;
	private JPanel selectionArea;
	
	
	/**
	 * Constructs an interactive Gui Pane
	 */
	public InteractiveGuiPane(){
		this.setPreferredSize(new Dimension(1000,1000));
		
		//set viewport default variables
		this.scaleOrigin = new Vector(0,0);
		this.scaleFactor = 1;
		this.scaleIncrement = (float) 0.1;
		this.scaleMin = (float) 0.01;
		this.scaleMax = 3;
		this.viewportTranslation = new Vector(0,0);
		this.zoomTranslation = new Vector(0,0);
		this.cables = new LinkedList<Cable>();
		this.selectedComponents = new LinkedList<InteractiveGuiComponent>();
		this.selectedCables = new LinkedList<Cable>();
		this.tmpSelectedCables = new LinkedList<Cable>();
		this.tmpSelectedComponents = new LinkedList<InteractiveGuiComponent>();
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
	
	public synchronized void zoomViewport(Vector zoomSourceScreenLocation, int wheelRotation){
		
		
		Vector gridLocation = this.convertToGridLocation(zoomSourceScreenLocation);
		if(wheelRotation>0){
			if(this.scaleFactor - this.scaleIncrement <this.scaleMin){
				return;
			}
			
			this.scaleFactor -=this.scaleIncrement ;
		}
		else if (wheelRotation <0) {
			if (this.scaleFactor +this.scaleIncrement > this.scaleMax){
				return;
			}
			this.scaleFactor +=this.scaleIncrement ;
		}
		
		this.scaleOrigin = zoomSourceScreenLocation;
		this.zoomTranslation = this.zoomTranslation.addVector(this.convertToScreenLocation(gridLocation).diffVector(zoomSourceScreenLocation));
		this.updateView();
	}
	
	private void setViewportOrigin(Vector viewportOriginGridLocation){
		this.viewportTranslation = viewportOriginGridLocation;
	}
	
	public Vector convertToGridLocation(Vector viewportVector){
		//first untranslate zoomTranslation, than untranslate translation with translationvector*scalefactor, than unscale
		Vector unzoomtranslated = this.zoomTranslation.diffVector(viewportVector);
		Vector untranslated = this.viewportTranslation.scaleVector(this.scaleFactor).diffVector(unzoomtranslated);
		//P=(P'-S)/sf+S
		Vector untranslatedUnscaled = ((this.scaleOrigin.diffVector(untranslated)).scaleVector(1/this.scaleFactor)).addVector(this.scaleOrigin);
		return untranslatedUnscaled;
	}
	
	public Vector convertToScreenLocation(Vector gridLocationVector){
		//first scale, than translate with translatevector*scalefactor and zoomtranslate
		
		//P'=S+sf*(P-S)
		Vector scaled  = this.scaleOrigin.addVector((this.scaleOrigin.diffVector(gridLocationVector)).scaleVector(this.scaleFactor));
		//translate + zoomtranslate in one step
		Vector scaledTranslated = scaled.addVector(this.viewportTranslation.scaleVector(this.scaleFactor).addVector(this.getZoomTranslation()));
		return scaledTranslated;
	}
	
	private void updateView(){
		Component[] components = this.getComponents();
		for (Component c: components){
			if(c instanceof InteractiveGuiComponent){
			((InteractiveGuiComponent) c).updateView();
			}
		}
		for(Cable c: this.cables){
			c.updateView();
		}
		this.revalidate();
	}
	
	
	
	/*
	 * GuiComponent interaction methods
	 */
	public void addInteractiveGuiComponent(InteractiveGuiComponent component){
		
		component.updateView();
		this.add(component);
		this.moveToFront(component);
	}
	
	public void addInteractiveCable(JComponent source, JComponent dest){
		this.cables.add(new Cable(source,dest));
	}
	
	public void remove(Cable c){
		this.cables.remove(c);
	}
	
	public void setComponentSelection(InteractiveGuiComponent component, boolean selected){
		component.setSelected(selected);
		if(selected){
			this.selectedComponents.add(component);
		} else {
			this.selectedComponents.remove(component);
		}
		this.updateView();
	}
	
	public LinkedList<InteractiveGuiComponent> getComponentSelection(){
		return this.selectedComponents;
	}
	
	public LinkedList<Cable> getCableSelection(){
		return this.selectedCables;
	}
	
	public void setCableSelection(Cable cable, boolean selected){
		cable.setSelected(selected);
		if(selected){
			this.selectedCables.add(cable);
		} else {
			this.selectedCables.remove(cable);
		}
		this.updateView();
	}
	
	public void clearSelection(){
		for(InteractiveGuiComponent c:this.selectedComponents){
			c.setSelected(false);
		}
		this.selectedComponents = new LinkedList<InteractiveGuiComponent>();
		for(Cable c:this.selectedCables){
			c.setSelected(false);
		}
		this.selectedCables = new LinkedList<Cable>();
		this.updateView();
	}
	
	public boolean hasSelected(){
		return this.selectedCables.size()+this.selectedComponents.size()!=0;
	}
	
	public boolean hasMultiSelection(){
		return this.selectedCables.size()+this.selectedComponents.size()>1;
	}
	
	public void removeSelection(){
		for(InteractiveGuiComponent c: this.getComponentSelection()){
			this.remove(c);
		}
		for(Cable c:this.getCableSelection()){
			this.remove(c);
		}
		this.clearSelection();
		this.updateView();
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
		
		//TODO: better area selection algorithm!
		
		for(Component c:this.getComponents()){
			
			if(c instanceof InteractiveGuiComponent){
				if(c.getBounds().intersects(this.selectionArea.getBounds())){
					if(!((InteractiveGuiComponent) c).isSelected()){
						((InteractiveGuiComponent) c).setSelected(true);
						this.tmpSelectedComponents.add((InteractiveGuiComponent) c);
					}
					
				} else {
					if(!this.selectedComponents.contains(((InteractiveGuiComponent) c))){
						((InteractiveGuiComponent) c).setSelected(false);
						this.tmpSelectedComponents.remove(c);
					}
				}
			}
		}
		
		
	}
	
	public void resetSelectionArea(){
		this.selectedComponents.addAll(this.tmpSelectedComponents);
		this.tmpSelectedComponents = new LinkedList<InteractiveGuiComponent>();
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
	
	public Vector getZoomTranslation(){
		return this.zoomTranslation;
	}
	
	@Override
	public void remove(Component c){
		super.remove(c);
		this.repaint();
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
}
