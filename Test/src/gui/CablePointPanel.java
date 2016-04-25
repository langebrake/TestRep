package gui;

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
		// TODO Auto-generated method stub
		return cable;
	}

	@Override
	public void setCable(InteractiveCable cable) {
		this.cable = cable;
	}

}
