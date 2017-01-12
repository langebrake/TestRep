package gui.interactivepane;

import java.awt.Component;
import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JComponent;

public class CablePointComponent extends JComponent implements CablePoint, CablePointHost {
	private InteractiveCable cable;
	private final CablePointType type;
	private CablePointHost host;
	private Component parent;
	private int index;
	private boolean tmpDisconnect;

	public CablePointComponent(Component parent, CablePointType type) {
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
		this.host = host;
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
		return this;
	}

	@Override
	public CablePointHost getHost() {
		return this.host;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type) {
		LinkedList<CablePointComponent> tmp = new LinkedList<CablePointComponent>();
		if (this.getType() == type) {
			tmp.add(this);
		}
		return tmp;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type, int... indices) {
		LinkedList<CablePointComponent> tmp = new LinkedList<CablePointComponent>();
		if (indices.length == 1 && indices[0] == 0 && this.getType() == type) {
			tmp.add(this);
		}
		return tmp;
	}

	@Override
	public CablePoint getCablePoint(CablePointType type, int index) {
		if (this.getType() == type && index == 0) {
			return this;
		} else {
			return null;
		}

	}

	@Override
	public CablePoint getCablePoint(CablePointType type) {
		if (this.getType() == type) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public boolean forceExistence(CablePoint... forceThis) {
		if (forceThis.length != 1 && !Arrays.asList(forceThis).contains(this)) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean contains(CablePoint point) {
		return this == point;
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
	public CablePoint getFreeCablePoint(CablePointType type) {
		if (!this.isConnected()) {
			return this;
		} else
			return null;
	}

	@Override
	public CablePoint getCablePoint(Point sourceInComponent) {

		return this;
	}

	@Override
	public void changedState(CablePoint point) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(CablePoint arg0) {
		return this.getIndex() - arg0.getIndex();
	}

	@Override
	public boolean getTmpDisconnect() {
		return this.tmpDisconnect;
	}

	@Override
	public void tmpDisconnect(boolean set) {
		this.tmpDisconnect = set;
	}

}
