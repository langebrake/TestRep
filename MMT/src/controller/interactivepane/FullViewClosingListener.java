package controller.interactivepane;

import gui.interactivepane.InteractiveModule;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.Serializable;

public class FullViewClosingListener implements WindowListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1495110790489397158L;
	InteractiveModule module;

	public FullViewClosingListener(InteractiveModule m) {
		this.module = m;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		module.closeFullView();

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
