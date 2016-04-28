package gui.interactivepane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JPanel;

public class CablePointPanel extends JPanel implements CablePoint,CablePointHost {
	private Component pane;
	private InteractiveCable cable;
	private final CablePointType type;
	private int index;
	/**
	 * Constructs a Cable Point Panel where the cable point itself lies in the middle of the specified panel
	 * @param parent
	 */
	public CablePointPanel(Component parent,CablePointType type){
		this.pane = parent;
		this.type = type;
	}
	@Override
	public int getXOnScreen() {
		
		return (int) ((this.getLocationOnScreen().getX()+this.getWidth()/2) );
	}

	@Override
	public int getYOnScreen() {
		return (int) ((this.getLocationOnScreen().getY()+this.getHeight()/2) );
	}
	
	

	@Override
	public InteractiveCable getCable() {
		return cable;
	}

	@Override
	public void setCable(InteractiveCable cable) {
		this.cable = cable;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(g instanceof Graphics2D){
			Graphics2D g2d = (Graphics2D) g;
			int radius = Math.min(this.getWidth(), this.getHeight())/7;
			if(this.type == CablePointType.INPUT){
				g2d.setColor(Color.BLUE);
			} else if(this.type == CablePointType.OUTPUT){
				g2d.setColor(Color.MAGENTA);
			}
			g2d.fillOval(this.getHeight()/2-radius/2, this.getWidth()/2-radius/2, radius, radius);
		}
	}
	@Override
	public boolean isConnected() {
		return this.cable != null;
	}
	@Override
	public CablePointType getType() {
		return this.type;
	}
	@Override
	public void setHost(CablePointHost host) {
		
	}
	@Override
	public void disconnect() {
		this.cable = null;
		
	}
	@Override
	public LinkedList<CablePoint> getCablePoints() {
		LinkedList<CablePoint> tmp = new LinkedList<CablePoint>();
		tmp.add(this);
		return tmp;
	}
	@Override
	public CablePoint getCablePoint() {
		
		return this;
	}
	@Override
	public CablePointHost getHost() {

		return this;
	}
	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type) {
		LinkedList<CablePointPanel> tmp = new LinkedList<CablePointPanel>();
		if(this.getType() == type){
			tmp.add(this);
		}
		return tmp;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type,int... indices) {
	LinkedList<CablePointPanel> tmp = new LinkedList<CablePointPanel>();
		if(indices.length == 1 && indices[0] == this.index && this.getType() == type){
			tmp.add(this);
		}
		return tmp;
	}


	@Override
	public CablePoint getCablePoint(CablePointType type,int index) {
		if(this.getType() == type && index == this.index){
			return this;
		} else {
			return null;
		}
		
	}

	@Override
	public CablePoint getCablePoint(CablePointType type) {
		if(this.getType() == type){
			return this;
		} else {
			return null;
		}
	}

	@Override
	public boolean forceExistence(CablePoint... forceThis) {
		if(forceThis.length != 1 && !Arrays.asList(forceThis).contains(this)){
			return false;
		} else {
			return true;
		}
		
	}

	@Override
	public boolean contains(CablePoint point) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setIndex(int i) {
		this.index = index;
		
	}
	@Override
	public int getIndex() {
		return this.index;
	}
	@Override
	public CablePoint getFreeCablePoint(CablePointType type) {
		// TODO Auto-generated method stub
		return null;
	}

}
