package gui.interactivepane;

import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JComponent;

public class CablePointSimple implements CablePoint, CablePointHost {
	private InteractiveCable cable;
	private final CablePointType type;
	private CablePointHost host;
	private int xOnScreen,yOnScreen;
	public CablePointSimple(CablePointType type){
		this.type = type;
	}
	
	public void setXOnScreen(int x){
		this.xOnScreen = x;
	}
	
	public void setYOnScreen(int y){
		this.yOnScreen = y;
				
	}
	@Override
	public int getXOnScreen() {
		return xOnScreen;
	}

	@Override
	public int getYOnScreen() {
		return yOnScreen;
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
