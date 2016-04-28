package gui.interactivepane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

import javax.swing.BorderFactory;

public class InteractiveShapeComponent extends InteractiveComponent implements CablePointHost{
	private Shape s;
	private LinkedList<CablePointSimple> cablePoints;
	public InteractiveShapeComponent(InteractivePane parent, Vector origin) {
		super(parent, origin);
		this.setLayout(new BorderLayout());
		this.cablePoints = new LinkedList<CablePointSimple>();
		this.setOriginDimension(new Dimension(200, 200));
		this.setOpaque(false);
		this.updateView();
	}
	
	

	
	
	@Override
	public void updateView(){
		super.updateView();
		this.s = new Ellipse2D.Float(0,0,this.getWidth() ,this.getHeight());
			for(CablePointSimple c:this.cablePoints){
				c.setXOnScreen((int) (this.getLocationOnScreen().getX() + this.getWidth() / 2));
				c.setYOnScreen((int) (this.getLocationOnScreen().getY() + this.getHeight() / 2));
			}
			this.repaint();

	}
	
	@Override
	public boolean intersects(Shape s){
		Rectangle r = s.getBounds();
		r.translate(- this.getX(), -this.getY());
		return this.s.intersects(r);
	}
	
	@Override
	public boolean contains(Point p){
		return s.contains(p);
	}
	
	@Override 
	public boolean contains(int x, int y){
		return s.contains(x, y);
	}
	
	
	
	
	
	public void paint(Graphics g){
		super.paint(g);
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D) g;

			if(this.isHovered()){
				g2d.setColor(Color.GREEN);
				g2d.fill(s);
			} else {
				g2d.setColor(new Color(0,0,0,0));
				g2d.fill(s);
			}
			if(this.isSelected()){
				g2d.setColor(Color.RED);
			} else {
				g2d.setColor(Color.BLACK);
			}
			g2d.draw(s);
		}
	}
	
	
	@Override
	public void validate(){
		if(this.getParentPane() != null){
			this.updateView();
		}
		super.validate();
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints() {
		return this.cablePoints;
	}

	@Override
	public CablePoint getCablePoint() {
		CablePointSimple c = new CablePointSimple(CablePointType.UNKNOWN);
		this.cablePoints.add(c);
		this.updateView();
		return (CablePoint) c;
	}





	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type) {
		LinkedList<CablePointSimple> tmp = new LinkedList<CablePointSimple>();
		for(CablePointSimple cps:this.cablePoints){
			if(cps.getType() == type){
				tmp.add(cps);
			}
		}
		return tmp;
	}





	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type, int... indices) {
		LinkedList<CablePointSimple> tmp = new LinkedList<CablePointSimple>();
		for(int i:indices){
			if(i >= this.cablePoints.size()){
				continue;
			}
			CablePointSimple cps = this.cablePoints.get(i);
			if(cps.getType() == type && !tmp.contains(cps)){
				tmp.add(cps);
			}
		}
		return tmp;
	}


	@Override
	public CablePoint getCablePoint(CablePointType type, int index) {
		if(index >= this.cablePoints.size() || this.cablePoints.get(index).getType() != type){
			return null;
		} else {
			return this.cablePoints.get(index);
		}
	}





	@Override
	public CablePoint getCablePoint(CablePointType type) {
		for(CablePointSimple cps:this.cablePoints){
			if(cps.getType() == type){
				return cps;
			}
		}
		CablePointSimple cps = new CablePointSimple(type);
		cps.setIndex(this.cablePoints.size());
		this.cablePoints.add(cps);
		return cps;
	}





	@Override
	public boolean forceExistence(CablePoint... forceThis) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public boolean contains(CablePoint point) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public CablePoint getFreeCablePoint(CablePointType type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
