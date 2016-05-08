package gui.interactivepane;

import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JComponent;

public class CablePointSimple implements CablePoint {
	private InteractiveCable cable;
	private final CablePointType type;
	private CablePointHost host;
	private int xOnScreen,yOnScreen;
	private int index;
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
		if(host!=null)
			host.changedState(this);
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
		if(host!=null)
			host.changedState(this);
		
	}


	@Override
	public CablePointHost getHost() {
		return this.host;
	}

	@Override
	public void setIndex(int i) {
		this.index = i;
		
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public int compareTo(CablePoint o) {
		return this.getIndex() - o.getIndex();
	}

	private boolean tmpDisconnect;
	@Override
	public boolean getTmpDisconnect() {
		return this.tmpDisconnect;
	}

	@Override
	public void tmpDisconnect(boolean set) {
		this.tmpDisconnect = set;
		if(host!=null)
			host.changedState(this);
		
	}
	
	

}
