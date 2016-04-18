package gui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JLayeredPane;
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
	private Vector lastMousePaneLocation;
	private Vector lastMouseGridLocation;
	
	
	
	/**
	 * Constructs an interactive Gui Pane
	 */
	public InteractiveGuiPane(){
		this.setPreferredSize(new Dimension(1000,1000));
		
		//set viewport default variables
		this.scaleOrigin = new Vector(0,0);
		this.scaleFactor = 1;
		this.scaleIncrement = (float) 0.1;
		this.scaleMin = (float) 0.1;
		this.scaleMax = 3;
		this.viewportTranslation = new Vector(0,0);
		this.zoomTranslation = new Vector(0,0);
		
		//add mouse listeners
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				lastMousePaneLocation = new Vector(e.getPoint());
				lastMouseGridLocation = convertToGridLocation(lastMousePaneLocation);
			}
			public void mouseClicked(MouseEvent arg0) {
				if(SwingUtilities.isLeftMouseButton(arg0)){
					addInteractiveGuiComponent(convertToGridLocation(new Vector(arg0.getPoint())));
				}	
			}
		});
		this.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent e){
				if(SwingUtilities.isLeftMouseButton(e)){
					//translate on origin Grid coordinates
					Vector currentMouseGridLocation = convertToGridLocation(new Vector(e.getPoint()));
					translateViewport(lastMouseGridLocation.diffVector(currentMouseGridLocation));
					lastMousePaneLocation = new Vector(e.getPoint());
					
				}
			}
		});
		this.addMouseWheelListener(new MouseAdapter(){
			public void mouseWheelMoved(MouseWheelEvent e){
				zoomViewport((new Vector(e.getPoint())),e.getWheelRotation());
			}
		});
		
		//TODO: delete this debug add
		int min = -10000;
		int max = 10000;
		for(int i = 1; i<1000; i++){
			this.add(new InteractiveGuiComponent(this,new Vector(ThreadLocalRandom.current().nextInt(min, max),ThreadLocalRandom.current().nextInt(min, max))));
		}
	}
	
	
	/*
	 * Viewport transformation methods
	 */
	private void translateViewport(Vector translationVectorGrid){
		
		this.viewportTranslation = this.viewportTranslation.addVector(translationVectorGrid);
		
		this.updateView();
	}
	
	private synchronized void zoomViewport(Vector zoomSourceScreenLocation, int wheelRotation){
		
		
		
		
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
			((InteractiveGuiComponent) c).updateView();
		}
	}
	
	
	
	/*
	 * GuiComponent interaction methods
	 */
	private void addInteractiveGuiComponent(Vector positionOnGrid){
		InteractiveGuiComponent newComponent = new InteractiveGuiComponent(this, positionOnGrid);
		newComponent.updateView();
		this.add(newComponent);
		this.moveToFront(newComponent);
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
	

	
	protected void paintComponent(Graphics g){
		
	}
	
}
