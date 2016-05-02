package gui.interactivepane;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CablePointPanel extends JPanel implements CablePointHost, MouseListener,MouseMotionListener,MouseWheelListener {
	private InteractiveCable cable;
	private final CablePointType type;
	private CablePointSimple cps;
	private int index;
	/**
	 * Constructs a Cable Point Panel where the cable point itself lies in the middle of the specified panel
	 * @param parent
	 */
	public CablePointPanel(CablePointSimple cps){
		this.type = cps.getType();
		this.cps = cps;
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		this.setMinimumSize(new Dimension(0,0));
	}
	public void updatePoint(){
		if(this.isShowing()){
			this.cps.setXOnScreen((int) (this.getLocationOnScreen().getX()+this.getWidth()/2));
			this.cps.setYOnScreen((int) (this.getLocationOnScreen().getY()+this.getHeight()/2));
		}
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.updatePoint();
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
	public LinkedList<CablePoint> getCablePoints() {
		LinkedList<CablePoint> tmp = new LinkedList<CablePoint>();
		tmp.add(this.cps);
		return tmp;
	}
	@Override
	public CablePoint getCablePoint() {
		
		return this.cps;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type) {
		LinkedList<CablePoint> tmp = new LinkedList<CablePoint>();
		if(this.cps.getType() == type){
			tmp.add(this.cps);
		}
		return tmp;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type,int... indices) {
	LinkedList<CablePoint> tmp = new LinkedList<CablePoint>();
		if(indices.length == 1 && indices[0] == this.index && this.cps.getType() == type){
			tmp.add(this.cps);
		}
		return tmp;
	}


	@Override
	public CablePoint getCablePoint(CablePointType type,int index) {
		if(this.cps.getType() == type && index == this.index){
			return this.cps;
		} else {
			return null;
		}
		
	}

	@Override
	public CablePoint getCablePoint(CablePointType type) {
		if(this.cps.getType() == type){
			return this.cps;
		} else {
			return null;
		}
	}

	@Override
	public boolean forceExistence(CablePoint... forceThis) {
		if(forceThis.length != 1 && !Arrays.asList(forceThis).contains(this.cps)){
			return false;
		} else {
			return true;
		}
		
	}

	@Override
	public boolean contains(CablePoint point) {
		return this == point;
	}


	@Override
	public CablePoint getFreeCablePoint(CablePointType type) {
		if(!this.cps.isConnected())
			return this.cps;
		else
			return null;
	}
	@Override
	public CablePoint getCablePoint(Point sourceInComponent) {
		return this.cps;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("PANEL CLICKED");
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.setBorder(new LineBorder(Color.BLACK));
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		this.setBorder(new EmptyBorder(1, 1, 1, 1));
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}

	
	private boolean cableDrawProcess;
	@Override
	public void mousePressed(MouseEvent arg0) {
		//TODO: CableCreation Shortcut
		if(arg0.isShiftDown() && arg0.isControlDown() && SwingUtilities.isLeftMouseButton(arg0))
			this.cableDrawProcess = true;
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.cableDrawProcess = false;
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}
	
	private void modifyEvent(MouseEvent arg0){
		arg0.setSource(this.getParent());
//		arg0.getPoint().translate((this.getParent().getLocationOnScreen().x-this.getLocationOnScreen().x),
//				this.getParent().getLocationOnScreen().y-this.getLocationOnScreen().y);

	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(!cableDrawProcess){
			modifyEvent(arg0);
		}
		this.getParent().dispatchEvent(arg0);
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		modifyEvent(arg0);
		this.getParent().dispatchEvent(arg0);
		
	}
}
