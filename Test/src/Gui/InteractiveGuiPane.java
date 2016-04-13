package Gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class InteractiveGuiPane extends JLayeredPane {

	
	/*
	 * Objects needed for viewport rendering
	 */
	private Point scaleOrigin;
	private float scaleFactor;
	private Point viewportTranslation;
	
	/*
	 * Objects needed for viewport interaction
	 */
	//used for drag translation
	private Point lastMousePaneLocation;
	
	
	/**
	 * Constructs an interactive Gui Pane
	 */
	public InteractiveGuiPane(){
		this.setPreferredSize(new Dimension(1000,1000));
		
		
		//add mouse listeners
		this.addMouseListener(new MouseAdapter(){
	
			public void mousePressed(MouseEvent e){
				lastMousePaneLocation = e.getPoint();
			}
			
			public void mouseClicked(MouseEvent arg0) {
				if(SwingUtilities.isLeftMouseButton(arg0)){
					addInteractiveGuiComponent(arg0.getPoint());
				}	
			}
			
		});
		
		this.addMouseMotionListener(new MouseAdapter(){
			
		});
		
		this.addMouseWheelListener(new MouseAdapter(){
			
		});
		
		
	}
	
	
	private void addInteractiveGuiComponent(Point positionOnPane){
		
	}
	
	
	
	
	
	
	
	
	
	public Point getScaleOrigin(){
		return this.scaleOrigin;
	}
	
	public float getScaleFactor(){
		return this.scaleFactor;
	}
	
	public Point getViewportTranslation(){
		return this.viewportTranslation;
	}
	
}
