package gui.interactivepane;

import java.awt.Graphics2D;
import java.awt.Shape;

public interface InteractiveShape extends Shape {
	public void setSelected(boolean set);
	public void setHovered(boolean set);
	public boolean isSelected();
	public boolean isHovered();
	public void updateView(Graphics2D g2d);
	public void translateOriginLocation(Vector translationVectorGrid);
}
