package switcherplugin_v01;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class SwitchView extends JPanel {
	private SwitcherPlugin switcherPlugin;

	public SwitchView() {
		this(null);
	}

	public SwitchView(SwitcherPlugin switcherPlugin) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.switcherPlugin = switcherPlugin;
		this.updateView();

	}

	void updateView() {

		this.removeAll();
		for (Routing r : switcherPlugin.routings) {
			if (!this.isAncestorOf(r.getView())) {
				this.add(r.getView());
			}
		}
		for (Routing r : this.switcherPlugin.routings) {
			r.routingView.updateComboBox();
		}
		JButton add = new JButton("+");
		add.addActionListener(switcherPlugin);
		this.add(add);
		this.setSize(this.getPreferredSize());
		this.revalidate();

	}

}
