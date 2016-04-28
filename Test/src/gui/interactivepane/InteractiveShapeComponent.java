package gui.interactivepane;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.EventListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

public class InteractiveShapeComponent extends InteractiveComponent implements MouseListener, MouseMotionListener, MouseWheelListener{
	private LinkedList<MouseListener> mouseListeners;
	private LinkedList<MouseMotionListener> mouseMotionListeners;
	private LinkedList<MouseWheelListener> mouseWheelListeners;
	private Shape s;
	private boolean constructor;
	public InteractiveShapeComponent(InteractivePane parent, Vector origin) {
		super(parent, origin);
		this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		this.lastEntered = parent;
		constructor = true;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		constructor = false;
		this.mouseListeners = new LinkedList<MouseListener>();
		this.mouseMotionListeners = new LinkedList<MouseMotionListener>();
		this.mouseWheelListeners = new LinkedList<MouseWheelListener>();
		this.setOriginDimension(new Dimension(200, 200));
		this.setOpaque(false);
		this.updateView();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(s.contains(e.getPoint())){
			for(MouseWheelListener l:mouseWheelListeners){
				l.mouseWheelMoved(e);
			}
			this.repaint();
		} else {
			this.dispatch(e);
		}
		
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(((s.contains(arg0.getPoint()) || dragged) && !outsidepressed)
				&& !outsidepressed){
			for(MouseMotionListener l:mouseMotionListeners){
				l.mouseDragged(arg0);
			}
			
			this.repaint();
		} else {
			arg0.setSource(lastDispatch);
			this.dispatch(arg0);
		}
		
		
	}
	private boolean entered = false;
	private Component lastEntered;
	@Override
	public void mouseMoved(MouseEvent arg0) {

		
		if (entered && !s.contains(arg0.getPoint()) && !dragged){
			for(MouseListener l:mouseListeners){
				l.mouseExited(arg0);
			}
			dispatch(updateLastDispatched(arg0));
			entered = false;
		} 
		else if (!entered && s.contains(arg0.getPoint()) && !dragged){
			for(MouseListener l:mouseListeners){
				l.mouseEntered(arg0);
			}
			if(lastDispatch == lastEntered){
				MouseEvent tmp = new MouseEvent(lastEntered,MouseEvent.MOUSE_EXITED,arg0.getWhen(),arg0.getModifiersEx(),arg0.getX(),arg0.getY(),arg0.getXOnScreen(),arg0.getYOnScreen(),arg0.getClickCount(),arg0.isPopupTrigger(),arg0.getButton());
				lastEntered.dispatchEvent(tmp);
				lastEntered = this;
			}
			entered = true;
		} else if(s.contains(arg0.getPoint()) && !dragged){
			for(MouseMotionListener l:mouseMotionListeners){
				l.mouseMoved(arg0);
			}
			
		} else {
			this.outsidepressed = true;
			
			this.dispatch(updateLastDispatched(arg0));
			if(lastDispatch != lastEntered && lastEntered != null && lastDispatch != null){
				MouseEvent tmp = new MouseEvent(lastEntered,MouseEvent.MOUSE_EXITED,arg0.getWhen(),arg0.getModifiersEx(),arg0.getX(),arg0.getY(),arg0.getXOnScreen(),arg0.getYOnScreen(),arg0.getClickCount(),arg0.isPopupTrigger(),arg0.getButton());
				lastEntered.dispatchEvent(tmp);
				tmp = new MouseEvent(lastDispatch,MouseEvent.MOUSE_ENTERED,arg0.getWhen(),arg0.getModifiersEx(),arg0.getX(),arg0.getY(),arg0.getXOnScreen(),arg0.getYOnScreen(),arg0.getClickCount(),arg0.isPopupTrigger(),arg0.getButton());
				lastDispatch.dispatchEvent(tmp);
				lastEntered = lastDispatch;
			} else if(lastEntered == null){
				lastEntered = lastDispatch;
				MouseEvent tmp = new MouseEvent(lastDispatch,MouseEvent.MOUSE_ENTERED,arg0.getWhen(),arg0.getModifiersEx(),arg0.getX(),arg0.getY(),arg0.getXOnScreen(),arg0.getYOnScreen(),arg0.getClickCount(),arg0.isPopupTrigger(),arg0.getButton());
				lastDispatch.dispatchEvent(tmp);
			}
			this.outsidepressed = false;
		}
		
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(s.contains(arg0.getPoint())){
			for(MouseListener l:mouseListeners){
				l.mouseClicked(arg0);
			}
			this.repaint();
		} else {
			this.outsidepressed = true;
			this.dispatch(updateLastDispatched(arg0));
			this.outsidepressed = false;
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if(entered && !dragged){
			for(MouseListener l:mouseListeners){
				l.mouseExited(arg0);
			}
			entered = false;
			this.repaint();
		} else {
			
		}
		lastEntered = null;
		
	}
	
	private boolean dragged = false;
	private boolean outsidepressed = false;
	@Override
	public void mousePressed(MouseEvent arg0) {
		if(s.contains(arg0.getPoint())){
			for(MouseListener l:mouseListeners){
				l.mousePressed(arg0);
			}
			dragged = true;
			this.repaint();
		} else {
			outsidepressed = true;
			this.dispatch(updateLastDispatched(arg0));
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(s.contains(arg0.getPoint()) && !outsidepressed){
			for(MouseListener l:mouseListeners){
				l.mouseReleased(arg0);
			}
			dragged = false;
			this.repaint();
		} else {
			this.dispatch(updateLastDispatched(arg0));
		}
		outsidepressed = false;
	}
	
	@Override
	public void addMouseListener(MouseListener l){
		if(constructor){
			super.addMouseListener(l);
		}else {
			this.mouseListeners.add(l);
		}
		this.repaint();
	}
	
	@Override
	public void addMouseWheelListener(MouseWheelListener l){
		if(constructor){
			super.addMouseWheelListener(l);
		}else {
			this.mouseWheelListeners.add(l);
		}
		this.repaint();
	}
	
	@Override
	public void addMouseMotionListener(MouseMotionListener l){
		if(constructor){
			super.addMouseMotionListener(l);
		} else {
			this.mouseMotionListeners.add(l);
		}
		this.repaint();
	}
	
	@Override
	public void removeMouseListener(MouseListener l){
		this.mouseListeners.remove(l);
	}
	
	@Override
	public void removeMouseMotionListener(MouseMotionListener l){
		this.mouseMotionListeners.remove(l);
	}
	
	@Override
	public void removeMouseWheelListener(MouseWheelListener l){
		this.mouseWheelListeners.remove(l);
	}
	
	@Override
	public void updateView(){
		super.updateView();
		this.s = new Rectangle2D.Float(this.getWidth()/10,this.getHeight()/10,this.getWidth() - this.getWidth()/2,this.getHeight() - this.getHeight()/2);
		
		this.repaint();
	}
	
	@Override
	public boolean intersects(Shape s){
		Rectangle r = s.getBounds();
		r.translate(- this.getX(), -this.getY());
		return this.s.intersects(r);
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
	
	private Component lastDispatch;
	private void dispatch(MouseEvent e){
		if(this.outsidepressed && lastDispatch != null){
			lastDispatch.dispatchEvent(e);
		} else {

		}
		
	}
	
	private MouseEvent updateLastDispatched(MouseEvent e){
		
		e.translatePoint((int)this.getLocation().getX(), (int)this.getLocation().getY());
			JComponent parent = (JComponent) this.getParent();
			int zOrder = parent.getComponentZOrder(this);
			parent.remove(this);
			Component source = parent.getComponentAt(e.getPoint());
			parent.add(this);
			
			parent.setComponentZOrder(this, zOrder);
			e.translatePoint(-source.getX(), -source.getY());
			e.setSource(source);
			lastDispatch = source;
			e.setSource(lastDispatch);
		
		
		return e;
	}
	
	
}
