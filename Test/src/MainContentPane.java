import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainContentPane extends JLayeredPane {
	private int xold, yold;
	private float scalefactor;
	private Point scaleSource;

	public MainContentPane() {
		Moveable moveable = new Moveable(10, 10);
		this.add(moveable);
		this.setPreferredSize(new Dimension(1000, 1000));
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xold = e.getXOnScreen();
				yold = e.getYOnScreen();
			}

			public void mouseClicked(MouseEvent arg0) {

				if (SwingUtilities.isLeftMouseButton(arg0)) {
					addMoveable(arg0.getX(), arg0.getY());
				}

			}

		});
		this.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				zoomPane(e.getX(), e.getY(), e.getWheelRotation());
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				movePaneMouse(e.getXOnScreen(), e.getYOnScreen());
			}
		});
		xold = 0;
		yold = 0;
		scalefactor = 1;
		scaleSource = new Point(0, 0);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	private void zoomPane(int x, int y, int amount) {
		Point unscaled = this.unscaledPoint(x, y);
		this.scaleSource = unscaled;
		if (!(this.scaleSource.x == x & this.scaleSource.y == y)) {

		}

		if (amount >= 0) {
			this.scalefactor -= 0.1;
		} else if (amount <= 0) {
			this.scalefactor += 0.1;
		}
		if (scalefactor <= 0.1)
			scalefactor = (float) 0.1;

		Component[] components = this.getComponents();

		for (Component c : components) {

			((Moveable) c).reposition(this.scaleSource, this.scalefactor);
		}
	}

	private void addMoveable(int x, int y) {
		Random rand = new Random();
		Point unscaledPoint = this.unscaledPoint(x, y);
		this.add(new Moveable(unscaledPoint.x, unscaledPoint.y,
				new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()), this.scaleSource, this.scalefactor));
	}

	private Point unscaledPoint(int scaledx, int scaledy) {
		return new Point((int) ((scaledx - this.scaleSource.x) / this.scalefactor + this.scaleSource.x),
				(int) ((scaledy - this.scaleSource.y) / this.scalefactor + this.scaleSource.y));
	}

	private void setPressOrigin(int x, int y) {
		xold = x;
		yold = y;
	}

	private void movePane(int dx, int dy) {
		Component[] components = this.getComponents();

		this.scaleSource.translate(dx, dy);

		for (Component c : components) {
			((Moveable) c).translateComponent(dx, dy);
		}

	}

	private void movePaneMouse(int x, int y) {
		this.movePane((x - xold), (y - yold));
		this.xold = x;
		this.yold = y;
	}

	public float getScalefactor() {
		return this.scalefactor;
	}

	public Point getScaleSource() {
		return this.scaleSource;
	}
}
