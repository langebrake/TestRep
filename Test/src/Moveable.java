import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

public class Moveable extends JPanel {
	private int offset;
	private int xold, yold; // for dragging: old mouse positions

	private Point scaleBufferPosition;
	private Dimension scaleBufferDimension;

	private Point unzoomedPosition;
	private Dimension unzoomedDimension;
	private Point scaleSource;
	private float scalefactor;
	private JFrame dialog = null;

	public Moveable() {
		this(0, 0);
	}

	public Moveable(int x, int y) {
		this(x, y, Color.YELLOW, new Point(0, 0), 1);
	}

	public Moveable(int x, int y, Color bg, Point scaleSource, float scalefactor) {
		this.setLocation(x, y);
		this.scaleBufferPosition = new Point(x, y);
		this.unzoomedDimension = new Dimension(50, 50);
		this.scaleBufferDimension = new Dimension(50, 50);
		this.setSize(this.unzoomedDimension);
		this.setBackground(bg);
		this.scalefactor = scalefactor;
		this.scaleSource = scaleSource;
		this.offset = 10;
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point unscaled = unscaledPoint(e.getXOnScreen(),
						e.getYOnScreen());
				dragComponent(unscaled.x, unscaled.y);
			}
		});
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Point unscaled = unscaledPoint(e.getXOnScreen(),
						e.getYOnScreen());
				focus();

				setPressOrigin(unscaled.x, unscaled.y);
			}

			public void mouseEntered(MouseEvent e) {
				setBorder(true);
			}

			public void mouseExited(MouseEvent e) {
				setBorder(false);
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					createFrame(e.getLocationOnScreen());
				}
			}
		});
		xold = 0;
		yold = 0;
		unzoomedPosition = new Point(x, y);
		this.reposition();
	}

	private void focus() {
	}

	private void createFrame(Point pos) {
		if (this.dialog == null) {
			JFrame dialog = new JFrame(
					new BigInteger(130, new SecureRandom()).toString(32));
			this.dialog = dialog;
			dialog.setLocation(pos);
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					noDialog();
				}

				public void windowClosing(WindowEvent e) {
					noDialog();
				}
			});
			dialog.setSize(300, 100);
			dialog.getContentPane().setBackground(this.getBackground());
			dialog.setVisible(true);
		} else {
			this.dialog.toFront();
		}
	}

	private void noDialog() {
		this.dialog = null;
	}

	private JFrame rootFrame() {
		return (JFrame) SwingUtilities.getWindowAncestor(this);
	}

	private void setBorder(boolean set) {
		if (set)
			this.setBorder(BorderFactory.createLineBorder(Color.black));
		else
			this.setBorder(BorderFactory.createEmptyBorder());
	}

	private void setPressOrigin(int x, int y) {
		xold = x;
		yold = y;
	}

	public void moveComponent(int x, int y) {

		this.unzoomedPosition.translate((x - xold), (y - yold));
		this.xold = x;
		this.yold = y;
		reposition();
	}

	private void dragComponent(int mousex, int mousey) {
		this.translateComponent(mousex - xold, mousey - yold);
		this.xold = mousex;
		this.yold = mousey;
	}

	public void translateComponent(int dx, int dy) {
		this.unzoomedPosition.translate((int) dx, (int) (dy));
		this.reposition();
	}

	public void setScale(Point Source, float factor) {
		this.scalefactor = factor;
		this.scaleSource = Source;
	}

	public void reposition(Point Source, float factor) {
		this.scalefactor = factor;
		this.scaleSource = Source;
		this.reposition();
	}

	private void reposition() {
		this.setLocation((int) (this.scaleSource.x + this.scalefactor
				* (this.unzoomedPosition.x - this.scaleSource.x)),
				(int) (this.scaleSource.y + this.scalefactor
						* (this.unzoomedPosition.y - this.scaleSource.y)));
		this.setSize((int) (this.unzoomedDimension.height * this.scalefactor),
				(int) (this.unzoomedDimension.width * this.scalefactor));
	}

	private Point unscaledPoint(int scaledx, int scaledy) {
		return new Point(
				(int) ((scaledx - this.scaleSource.x) / this.scalefactor + this.scaleSource.x),
				(int) ((scaledy - this.scaleSource.y) / this.scalefactor + this.scaleSource.y));
	}

}
