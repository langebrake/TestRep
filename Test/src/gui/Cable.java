package gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JComponent;

public class Cable implements Shape{
	
	private CubicCurve2D cable;
	private CablePoint source,dest;
	private LinkedList<CablePoint> cablePoints;
	private boolean selected;
	private boolean hover;
	private InteractiveGuiPane parent;
	private Color color;
	private float width;
	
	public Cable(InteractiveGuiPane parent){
		this(null,null,parent);
	}
	
	public Cable(CablePoint source, CablePoint dest,InteractiveGuiPane parent){
		this.source = source;
		this.dest = dest;
		this.cablePoints = new LinkedList<CablePoint>();
		this.selected = false;
		this.parent = parent;
		this.color = Color.black;
		
	}
	
	public void setSource(CablePoint source){
		this.source = source;
	}
	
	public CablePoint getSource(){
		return this.source;
	}
	
	public void setDestination(CablePoint dest){
		this.dest = dest;
	}
	
	public CablePoint getDestination(){
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
		this.selected = set;
	}
	
	public void setHover(boolean set){
		this.hover = set;
	}
	
	public void updateView(){
		
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public Cable calculateCable(){
		float x1pos =this.source.getLocationOnScreen().x - this.parent.getLocationOnScreen().x ;
		float y1pos =this.source.getLocationOnScreen().y - this.parent.getLocationOnScreen().y;
		float x2pos =this.dest.getLocationOnScreen().x - this.parent.getLocationOnScreen().x;
		float y2pos =this.dest.getLocationOnScreen().y - this.parent.getLocationOnScreen().y;
		this.cable = new CubicCurve2D.Float(x1pos,y1pos,x1pos+(x2pos-x1pos)/3,y1pos,x2pos-(x2pos-x1pos)/3,y2pos,x2pos,y2pos);
		System.out.println(this.source.getLocationOnScreen()+" "+this.dest.getLocationOnScreen());
		/**this.cable.moveTo(this.source.getX(), this.source.getX());
		for(CablePoint c: this.cablePoints){
			this.cable.lineTo(c.getX(), c.getX());
		}
		this.cable.lineTo(this.dest.getX(), this.dest.getX());**/
		
		return this;
	}
	
	
}
