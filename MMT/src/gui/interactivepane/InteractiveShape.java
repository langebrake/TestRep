package gui.interactivepane;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.Serializable;

import controller.interactivepane.InteractiveController;

public interface InteractiveShape extends Shape, Serializable {
	public void setSelected(boolean set);

	public void setHovered(boolean set);

	public boolean isSelected();

	public boolean isHovered();

	public void paint(Graphics2D g2d);

	public void translateOriginLocation(Vector translationVectorGrid);

	public boolean selectable();

	public InteractiveController getController();

	public void setController(InteractiveController controller);
}
