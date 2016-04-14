package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.util.Random;

import javax.swing.JPanel;

public class InteractiveGuiComponent extends JPanel {

	private Vector originLocation;
	private Dimension originDimension;
	private InteractiveGuiPane parent;
	
	public InteractiveGuiComponent(InteractiveGuiPane parent) {
		this(parent,new Vector(0,0));
	}
	
	public InteractiveGuiComponent(InteractiveGuiPane parent, Vector origin){
		this(parent,origin,new Dimension(100,50));
	}
	
	public InteractiveGuiComponent(InteractiveGuiPane parent, Vector originLocation, Dimension originDimension){
		this.parent = parent;
		this.originLocation = originLocation;
		this.originDimension = originDimension;
		this.setSize(originDimension);
		this.updateView();


		
		//TODO : delete the random color thing when implementing modules
		Random rand = new Random();
		this.setBackground(new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
		
		
		/*
		 * add required Mouselisteners
		 */
		
		this.addMouseListener(new MouseAdapter(){
			
		});
		
	}
	
	
	
	
	
	private void translateOriginLocation(Vector translationVector){
		this.originLocation = this.originLocation.addVector(translationVector);
		this.updateView();
	}
	
	private void setOriginLocation(Vector originLocation){
		this.originLocation = originLocation;
	}
	
	private void translateScreenLocation(int dx, int dy){
		
	}
	
	public void updateView(){
		//set components screen location
		
		this.setLocation(parent.convertToScreenLocation(this.originLocation).toPoint());
		//size component
		double scaleFactor = this.parent.getScaleFactor();
		this.setSize((int)(this.originDimension.width*scaleFactor), (int) (this.originDimension.height*scaleFactor));
	}
	
	
}
