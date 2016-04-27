package gui.interactivepane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class CablePointPanel extends JPanel implements CablePoint {
	private InteractivePane pane;
	private InteractiveCable cable;
	
	/**
	 * Constructs a Cable Point Panel where the cable point itself lies in the middle of the specified panel
	 * @param parent
	 */
	public CablePointPanel(InteractivePane parent){
		this.pane = parent;
	}
	@Override
	public int getXOnPane() {
		
		return (int) ((this.getLocationOnScreen().getX()+this.getWidth()/2) - pane.getLocationOnScreen().getX());
	}

	@Override
	public int getYOnPane() {
		return (int) ((this.getLocationOnScreen().getY()+this.getHeight()/2) - pane.getLocationOnScreen().getY());
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
			g2d.setColor(Color.GREEN);
			g2d.fillOval(this.getHeight()/2-radius/2, this.getWidth()/2-radius/2, radius, radius);
		}
	}
	@Override
	public boolean isConnected() {
		return this.cable != null;
	}

}
