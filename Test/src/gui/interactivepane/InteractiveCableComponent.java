package gui.interactivepane;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

import javax.swing.BorderFactory;

import controller.interactivepane.InteractiveController;

public class InteractiveCableComponent extends InteractiveComponent {

	private GeneralPath cable;
	private CablePoint source, dest;
	private LinkedList<CablePoint> cablePoints;
	private boolean selected;
	private boolean hover;
	private Paint color;
	private float width;
	private boolean draggedEndpoint;

	public InteractiveCableComponent(CablePoint source, CablePoint dest, InteractiveController parent) {
		this(parent, null);
		this.source = source;
		this.dest = dest;
		this.setOpaque(false);
		this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		this.cablePoints = new LinkedList<CablePoint>();
		this.cable = new GeneralPath();

	}

	private InteractiveCableComponent(InteractiveController parent, Vector origin) {
		super(parent, origin);
		// TODO Auto-generated constructor stub
	}

	public void setSource(CablePoint source) {
		this.source = source;
	}

	public CablePoint getSource() {
		return this.source;
	}

	public void setDestination(CablePoint dest) {
		this.dest = dest;
	}

	public CablePoint getDestination() {
		return this.dest;
	}

	public void setSelected(boolean set) {
		this.selected = set;
		if (set) {
			this.color = Color.RED;
		} else {
			this.color = Color.BLACK;

		}
	}

	public boolean isSelected() {
		return this.selected;
	}

	public boolean isHovered() {
		return this.hover;
	}

	public void setHovered(boolean set) {
		this.hover = set;
		if (set) {
			this.width = 2;
		} else {
			this.width = 1;
		}
	}

	public void setDraggedEndpoint(boolean set) {
		this.draggedEndpoint = set;
	}

	public LinkedList<CablePoint> getCablePoints() {
		LinkedList<CablePoint> points = new LinkedList<CablePoint>();
		points.add(source);
		points.addAll(cablePoints);
		points.add(dest);
		return points;
	}

	@Override
	public void updateView() {
		if (controller.getPane().isShowing()) {
			this.setBounds(controller.getPane().getBounds());

			int paneX = (int) controller.getPane().getLocationOnScreen().getX();
			int paneY = (int) controller.getPane().getLocationOnScreen().getY();
			float x1pos = (float) (this.source.getXOnScreen() - paneX);
			float y1pos = (float) (this.source.getYOnScreen() - paneY);
			float x2pos = (float) (this.dest.getXOnScreen() - paneX);
			float y2pos = (float) (this.dest.getYOnScreen() - paneY);

			float lastx = x1pos;
			float lasty = y1pos;
			this.cable = new GeneralPath();
			this.cable.moveTo(x1pos, y1pos);

			for (CablePoint c : this.cablePoints) {
				this.cable.lineTo(c.getXOnScreen() - paneX, c.getYOnScreen() - paneY);
				lastx = c.getXOnScreen() - paneX;
				lasty = c.getYOnScreen() - paneY;
			}

			float endpointCurvedX, endpointCurvedY;
			if (draggedEndpoint) {
				endpointCurvedX = x2pos - (x2pos - lastx) / 5;
				endpointCurvedY = y2pos - (y2pos - lasty) / 5;
			} else {
				endpointCurvedX = x2pos - (x2pos - lastx) / 5;
				endpointCurvedY = y2pos;
			}
			this.cable.curveTo(x1pos + (x2pos - x1pos) / 5, y1pos, endpointCurvedX, endpointCurvedY, x2pos, y2pos);

		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;

			g2d.setPaint(this.color);
			g2d.setStroke(new BasicStroke(this.width * controller.getPane().getScaleFactor()));

			g2d.draw(this.cable);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o.getClass() == this.getClass())) {
			return false;
		}
		InteractiveCableComponent other = (InteractiveCableComponent) o;
		boolean result = true;
		return this.source == other.source && this.dest == other.dest;
	}

	@Override
	public boolean intersects(Shape s) {
		Rectangle r = s.getBounds();
		r.translate(-this.getX(), -this.getY());
		return this.cable.intersects(r);
	}

	@Override
	public boolean contains(int x, int y) {
		return this.cable.contains(x, y);
	}

	@Override
	public boolean contains(Point p) {
		return this.cable.contains(p);
	}

	@Override
	public void translateOriginLocation(Vector translationVectorGrid) {
		this.updateView();
		this.repaint();

	}

	@Override
	public boolean close() {
		return true;

	}

	@Override
	public boolean reopen() {
		return true;

	}
}
