package guiinterface;

import javax.swing.JComponent;

public abstract class SizeableComponent extends JComponent implements InteractiveUpdateable {

	@Override
	public abstract void updateView();

}
