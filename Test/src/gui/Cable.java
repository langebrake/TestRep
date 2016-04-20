package gui;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;


import javax.swing.JComponent;

public class Cable implements Shape{
	
	private GeneralPath cable;
	private JComponent source,dest;
	private LinkedList<Clip> clips;
	
	public Cable(){
		this(null,null);
	}
	
	public Cable(JComponent source, JComponent dest){
		this.source = source;
		this.dest = dest;
		this.cable = new GeneralPath();
	}
	
	public void setSource(JComponent source){
		this.source = source;
	}
	
	public JComponent getSource(){
		return this.source;
	}
	
	public void setDestination(JComponent dest){
		this.dest = dest;
	}
	
	public JComponent getDestination(){
		return this.dest;
	}
	
	
	@Override
	public boolean contains(Point2D arg0) {
		return this.cable.contains(arg0);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return this.cable.contains(r);
	}

	@Override
	public boolean contains(double x, double y) {
		return this.cable.contains(x, y);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return this.cable.contains(x, y, w, h);
	}

	@Override
	public Rectangle getBounds() {
		return this.cable.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return this.cable.getBounds2D();
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return this.cable.getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return this.cable.getPathIterator(at, flatness);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return this.cable.intersects(r);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return this.cable.intersects(x, y, w, h);
	}
	
	
	public void setSelected(boolean set){
		
	}
	
	public void setHover(boolean set){
		
	}
	
	public void updateView(){
		
	}
	
	
}
