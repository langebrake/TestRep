package gui.interactivepane;

import guiinterface.InteractiveUpdateable;
import guiinterface.SizeableComponent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import controller.interactivepane.InteractiveController;

public class InteractiveDisplay extends InteractiveComponent {

	private JComponent contentPane;

	/**
	 * creates a new interactive gui component and places it to the origin
	 * vector
	 * 
	 * @param parent
	 * @param origin
	 */
	public InteractiveDisplay(InteractiveController parent, Vector origin,
			JComponent contentPane) {
		super(parent, origin);
		super.setLayout(new BorderLayout());
		this.contentPane = contentPane;
		this.setOriginDimension(contentPane.getSize());
		this.add(contentPane, BorderLayout.CENTER);
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}

	@Override
	public void setHovered(boolean set) {
		super.setHovered(set);
		if (set)
			this.setBorder(BorderFactory.createLineBorder(Color.black));
		else {
			if (this.isSelected()) {
				this.setBorder(BorderFactory.createLineBorder(Color.red));
			} else {
				this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			}
		}
	}

	@Override
	public void setSelected(boolean set) {
		super.setSelected(set);
		if (set)
			this.setBorder(BorderFactory.createLineBorder(Color.red));
		else
			this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean reopen() {
		return true;

	}

}
