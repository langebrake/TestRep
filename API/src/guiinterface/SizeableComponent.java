package guiinterface;

import javax.swing.JComponent;

public abstract class SizeableComponent extends JComponent implements InteractiveUpdateable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6449606788990070864L;

	@Override
	public abstract void updateView();

}
