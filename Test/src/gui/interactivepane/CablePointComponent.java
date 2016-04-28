package gui.interactivepane;

import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JComponent;

public class CablePointComponent extends JComponent implements CablePoint, CablePointHost {
	private InteractiveCable cable;
	private final CablePointType type;
	private CablePointHost host;
	private Component parent;
	public CablePointComponent(Component parent, CablePointType type){
		this.type = type;
		this.parent = parent;
	}
	
	@Override
	public int getXOnScreen() {
		return (int) this.getLocationOnScreen().getX();
	}

	@Override
	public int getYOnScreen() {
		return (int) this.getLocationOnScreen().getY();
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
	@Override
	public LinkedList<CablePoint> getCablePoints() {
		LinkedList<CablePoint> tmp = new LinkedList<CablePoint>();
		tmp.add(this);
		return null;
	}
	@Override
	public CablePoint getCablePoint() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public CablePointHost getHost() {
		return this.host;
	}
	
	

}
