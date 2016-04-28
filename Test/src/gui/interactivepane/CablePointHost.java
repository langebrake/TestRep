package gui.interactivepane;

import java.util.LinkedList;

public interface CablePointHost {
	public LinkedList<? extends CablePoint> getCablePoints();
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type);
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type,int... indices);
	public CablePoint getCablePoint();
	public CablePoint getCablePoint(CablePointType type, int index);
	public CablePoint getCablePoint(CablePointType type);
	public CablePoint getFreeCablePoint(CablePointType type);
	public boolean forceExistence(CablePoint... forceThis);
	public boolean contains(CablePoint point);
	
}
