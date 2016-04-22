package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import javax.swing.JComponent;

public class InteractiveCable implements InteractiveShape{
	
	private GeneralPath cable;
	private CablePoint source,dest;
	private LinkedList<CablePoint> cablePoints;
	private boolean selected;
	private boolean hover;
	private InteractivePane parent;
	private Paint color;
	
	public InteractiveCable(InteractivePane parent){
		this(null,null,parent);
	}
	
	public InteractiveCable(CablePoint source, CablePoint dest,InteractivePane parent){
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
		if(set){
			this.color = Color.RED;
		}else{
			this.color = Color.BLACK;
			
		}
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	public boolean isHovered(){
		return this.hover;
	}
	public void setHovered(boolean set){
		this.hover = set;
	}
	
	
	public void updateView(Graphics2D g2d){
		float x1pos =this.source.getLocationOnScreen().x - this.parent.getLocationOnScreen().x ;
		float y1pos =this.source.getLocationOnScreen().y - this.parent.getLocationOnScreen().y;
		float x2pos =this.dest.getLocationOnScreen().x - this.parent.getLocationOnScreen().x;
		float y2pos =this.dest.getLocationOnScreen().y - this.parent.getLocationOnScreen().y;
		
		//this.cable = new CubicCurve2D.Float(x1pos,y1pos,x1pos+(x2pos-x1pos)/5,y1pos,x2pos-(x2pos-x1pos)/5,y2pos,x2pos,y2pos);
		this.cable = new GeneralPath();
		
		this.cable.moveTo(x1pos, y1pos);
		for(CablePoint c: this.cablePoints){
			this.cable.lineTo(c.getX(), c.getX());
		}
		
		this.cable.lineTo(x2pos, y2pos);

		
		g2d.setPaint(this.color);
		g2d.setStroke(new BasicStroke(1*parent.getScaleFactor()));
		
		g2d.draw(this.cable);
		
	}

	@Override
	public void translateOriginLocation(Vector translationVectorGrid) {
		// TODO Auto-generated method stub
		
	}
	
	
}
