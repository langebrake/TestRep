package controller.interactivepane;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ControllerListenerAdapter implements MouseInputListener {
	
	protected InteractiveController controller;
	
	public ControllerListenerAdapter(InteractiveController controller){
		this.controller = controller;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		controller.mouseWheelMoved(arg0);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		controller.mouseDragged(e);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		controller.mouseMoved(e);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		controller.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		controller.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		controller.mouseExited(e);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		controller.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		controller.mouseReleased(e);
	}

}
