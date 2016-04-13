package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class InteractiveGuiPane extends JLayeredPane {

	
	
	
	/*
	 * Objects needed for viewport rendering
	 */
	private Vector scaleOrigin;
	private float scaleFactor;
	private float scaleIncrement;
	// set Coordinate origin (0|0) to this position
	private Vector viewportTranslation;
	
	
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
		this.viewportTranslation = new Vector(0,0);
		
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
					translateViewport(currentMouseGridLocation.diffVector(lastMouseGridLocation));
					//lastMousePaneLocation = currentMousePaneLocation;
					
				}
			}
		});
		this.addMouseWheelListener(new MouseAdapter(){
			public void mouseWheelMoved(MouseWheelEvent e){
				//translate to origin Grid coordinates
				zoomViewport(convertToGridLocation((new Vector(e.getPoint()))),e.getWheelRotation());
			}
		});
	}
	
	
	/*
	 * Viewport transformation methods
	 */
	private void translateViewport(Vector translationVectorGrid){
		
		this.viewportTranslation = this.viewportTranslation.addVector(translationVectorGrid);
		
		this.updateView();
	}
	
	private void zoomViewport(Vector zoomSourceGridLocation, int wheelRotation){
		if(wheelRotation>0 && this.scaleFactor>0.1){
			this.scaleFactor -=0.1 ;
		}
		else if (wheelRotation <0) {
			this.scaleFactor +=0.1 ;
		}
		this.scaleOrigin = zoomSourceGridLocation;
		this.updateView();
	}
	
	private void setViewportOrigin(Vector viewportOriginGridLocation){
		this.viewportTranslation = viewportOriginGridLocation;
	}
	
	private Vector convertToGridLocation(Vector viewportVector){
		//first untranslate with translationvector*scalefactor, than unscale
		Vector untranslated = viewportVector.diffVector(this.viewportTranslation.scaleVector(this.scaleFactor));
		//P=(P'-S)/sf+S
		Vector untranslatedUnscaled = ((untranslated.diffVector(this.scaleOrigin)).scaleVector(1/this.scaleFactor)).addVector(this.scaleOrigin);
		return untranslatedUnscaled;
	}
	
	public Vector convertToScreenLocation(Vector gridLocationVector){
		//first scale, than translate with translatevector*scalefactor
		
		//P'=S+sf*(P-S)
		Vector scaled  = this.scaleOrigin.addVector((gridLocationVector.diffVector(this.scaleOrigin)).scaleVector(this.scaleFactor));
		Vector scaledTranslated = scaled.addVector(this.viewportTranslation.scaleVector(this.scaleFactor));
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
	
}
