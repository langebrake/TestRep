package Gui;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JPanel;

public class InteractiveGuiComponent extends JPanel {

	private Point origin;
	private Dimension originDimension;
	private InteractiveGuiPane parent;
	
	public InteractiveGuiComponent(InteractiveGuiPane parent) {
		this(parent,new Point(0,0));
	}
	
	public InteractiveGuiComponent(InteractiveGuiPane parent, Point origin){
		this.parent = parent;
		this.origin = origin;
	}
	
	private void translateOriginLocation(int dx, int dy){
		
	}
	
	private void translateScreenLocation(int dx, int dy){
		
	}
	
	private void updateView(){
		
	}
	
	
}
