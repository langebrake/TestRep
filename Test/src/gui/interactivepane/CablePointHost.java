package gui.interactivepane;

import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;

public interface CablePointHost extends Serializable {
	public LinkedList<? extends CablePoint> getCablePoints();
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type);
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type,int... indices);
	public CablePoint getCablePoint();
	public CablePoint getCablePoint(Point sourceInComponent);
	public CablePoint getCablePoint(CablePointType type, int index);
	public CablePoint getCablePoint(CablePointType type);
	public CablePoint getFreeCablePoint(CablePointType type);
	public boolean forceExistence(CablePoint... forceThis);
	public boolean contains(CablePoint point);
	
}
