package gui.tools;

import java.awt.Point;

public class ViewportTools {
	/**
	 * Calculate a Differentiation Vector for two points
	 * 
	 * @param p1
	 *            Point 1
	 * @param p2
	 *            Point 2
	 * @return Vector from Point 1 to Point 2
	 */
	public static Point diffVector(Point p1, Point p2) {
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}

}
