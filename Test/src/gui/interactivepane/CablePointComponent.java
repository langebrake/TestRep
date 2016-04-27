package gui.interactivepane;

import javax.swing.JComponent;

public class CablePointComponent extends JComponent implements CablePoint {
	private InteractivePane pane;
	private InteractiveCable cable;
	private final CablePointType type;
	private CablePointHost host;
	
	public CablePointComponent(InteractivePane parent,CablePointType type){
		this.pane = parent;
		this.type = type;
	}
	@Override
	public int getXOnPane() {
		return (int) (this.getLocationOnScreen().getX() - pane.getLocationOnScreen().getX());
	}

	@Override
	public int getYOnPane() {
		return (int) (this.getLocationOnScreen().getY() - pane.getLocationOnScreen().getY());
	}

	@Override
	public InteractiveCable getCable() {
		return cable;
	}

	@Override
	public void setCable(InteractiveCable cable) {
		this.cable = cable;
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
		this.host=host;
	}
	@Override
	public void disconnect() {
		this.cable = null;
		
	}
	
	

}
