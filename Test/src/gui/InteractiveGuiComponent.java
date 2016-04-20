package gui;

import guiinterface.InteractiveUpdateable;
import guiinterface.SizeableComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class InteractiveGuiComponent extends JPanel implements InteractiveUpdateable {

	private Vector originLocation;
	private Dimension originDimension;
	private InteractiveGuiPane parent;
	private boolean selected;
	private JComponent contentPane;
	
	/**
	 * creates a new interactive gui component and places it to the origin vector
	 * @param parent
	 * @param origin
	 */
	public InteractiveGuiComponent(InteractiveGuiPane parent, Vector origin, JComponent contentPane ){
		super(new BorderLayout());
		this.contentPane = contentPane;
		this.parent = parent;
		this.originLocation = origin;
		
		this.originDimension = contentPane.getSize();;
		this.selected = false;
		this.setSize(originDimension);
		this.add(contentPane,BorderLayout.CENTER);
		this.updateView();
		this.setOpaque(false);
		
		
		
	}
	
	
	
	
	
	
	public void translateOriginLocation(Vector translationVector){
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
	
	public void setHover(boolean set){
		if(set)
			this.setBorder(BorderFactory.createLineBorder(Color.black));
		else {
			if(this.selected){
				this.setBorder(BorderFactory.createLineBorder(Color.red));
			} else {
				this.setBorder(BorderFactory.createEmptyBorder());
			}
		}
			
	}
	
	public void setSelected(boolean set){
		this.selected = set;
		if(set)
			this.setBorder(BorderFactory.createLineBorder(Color.red));
		else
			this.setBorder(BorderFactory.createEmptyBorder());
	}

	
	public boolean isSelected(){
		return this.selected;
	}
	
	
}
