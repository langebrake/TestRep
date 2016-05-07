package gui.interactivepane;

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

import controller.interactivepane.InteractiveController;

public class InteractiveCable implements InteractiveShape{
	
	private GeneralPath cable;
	private CablePoint source,dest;
	private LinkedList<CablePoint> cablePoints;
	private boolean selected;
	private boolean hover;
	private Paint color;
	private Paint originColor;
	private float originWidth;
	private float width;
	private boolean draggedEndpoint;
	protected InteractiveController controller;
	
	public InteractiveCable(InteractiveController parent){
		this(null,null,parent);
	}
	
	public InteractiveCable(CablePoint source, CablePoint dest, InteractiveController parent){
		this(source,dest,1,Color.BLACK,parent);
	}
	
	public InteractiveCable(CablePoint source, CablePoint dest,float width,Color color, InteractiveController parent){
		this.source = source;
		this.dest = dest;
		this.cablePoints = new LinkedList<CablePoint>();
		this.selected = false;
		this.originColor = color;
		this.originWidth = width;
		this.color = this.originColor;
		this.width = this.originWidth;
		this.controller = parent;
		
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
			this.color = this.originColor;
			
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
		if(set){
			this.width = originWidth*2;
		} else {
			this.width = originWidth*1;
		}
	}
	
	public void setDraggedEndpoint(boolean set){
		this.draggedEndpoint = set;
	}
	
	public LinkedList<CablePoint> getCablePoints(){
		LinkedList<CablePoint> points = new LinkedList<CablePoint>();
		points.add(source);
		points.addAll(cablePoints);
		points.add(dest);
		return points;
	}
	
	public void updateView(Graphics2D g2d){
		int paneX =  (int) this.controller.getPane().getLocationOnScreen().getX();
		int paneY =  (int) this.controller.getPane().getLocationOnScreen().getY();
		float x1pos =(float) (this.source.getXOnScreen() - paneX);
		float y1pos =(float) (this.source.getYOnScreen() - paneY);
		float x2pos =(float) (this.dest.getXOnScreen() - paneX);
		float y2pos =(float) (this.dest.getYOnScreen() - paneY);
		
		float lastx = x1pos;
		float lasty = y1pos;
		this.cable = new GeneralPath();
		this.cable.moveTo(x1pos, y1pos);
		
		for(CablePoint c: this.cablePoints){
			this.cable.lineTo(c.getXOnScreen() - paneX , c.getYOnScreen() - paneY);
			lastx = c.getXOnScreen() - paneX;
			lasty = c.getYOnScreen() - paneY;
		}
		
		float endpointCurvedX, endpointCurvedY;
		if(draggedEndpoint){
			endpointCurvedX = x2pos-(x2pos-lastx)/5;
			endpointCurvedY = y2pos-(y2pos-lasty)/5;
		} else {
			endpointCurvedX = x2pos-(x2pos-lastx)/5;
			endpointCurvedY = y2pos;
		}
		this.cable.curveTo(x1pos+(x2pos-x1pos)/5, y1pos, endpointCurvedX, endpointCurvedY, x2pos, y2pos);

		
		g2d.setPaint(this.color);
		g2d.setStroke(new BasicStroke(this.width*controller.getPane().getScaleFactor()));
		
		g2d.draw(this.cable);
		
	}

	@Override
	public boolean equals(Object o){
		if(o==null || !(o.getClass() == this.getClass())){
			return false;
		}
		InteractiveCable other = (InteractiveCable) o;
		boolean result = true;
		return this.source == other.source && this.dest == other.dest;
	}
	
	@Override
	public void translateOriginLocation(Vector translationVectorGrid) {
		
		
	}

	@Override
	public boolean selectable() {
		return true;
	}

	@Override
	public InteractiveController getController() {
		return this.controller;
	}
	
	public void setController(InteractiveController controller){
		this.controller = controller;
	}
	
	
}
