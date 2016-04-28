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
		this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		this.cablePoints = new LinkedList<CablePointSimple>();
		this.setOriginDimension(new Dimension(200, 200));
		this.setOpaque(false);
		this.updateView();
	}
	
	

	
	
	@Override
	public void updateView(){
		super.updateView();
		this.s = new Ellipse2D.Float(this.getWidth()/10,this.getHeight()/10,this.getWidth() - this.getWidth()/7,this.getHeight() - this.getHeight()/7);
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
	public LinkedList<? extends CablePoint> getCablePoints() {
		// TODO Auto-generated method stub
		return this.cablePoints;
	}

	@Override
	public CablePoint getCablePoint() {
		// TODO Auto-generated method stub
		CablePointSimple c = new CablePointSimple(CablePointType.THROUGH);
		this.cablePoints.add(c);
		this.updateView();
		return (CablePoint) c;
	}
	
	
}
