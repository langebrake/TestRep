package gui.interactivepane;

import java.util.LinkedList;

public interface CablePointHost {
	public LinkedList<? extends CablePoint> getCablePoints();
	public CablePoint getCablePoint();
}
