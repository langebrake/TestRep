package switcherplugin_v01;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoutingView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4901417568083575417L;
	private Routing routing;
	protected JComboBox<Integer> inBox, outBox, activeListenBox;

	public RoutingView(Routing routing) {
		this.routing = routing;
		this.inBox = new JComboBox<Integer>();
		this.outBox = new JComboBox<Integer>();
		this.activeListenBox = new JComboBox<Integer>();
		this.activeListenBox.addItemListener(routing);
		this.inBox.addItemListener(routing);
		this.outBox.addItemListener(routing);
		this.createView();
	}

	protected void updateComboBox() {

		this.inBox.removeAllItems();
		this.outBox.removeAllItems();
		this.activeListenBox.removeAllItems();
		routing.block = true;
		for (int i = 1; i <= this.routing.switcher.getPluginHost().getInputCount(); i++) {
			this.inBox.addItem(new Integer(i));
			this.activeListenBox.addItem(new Integer(i));
		}
		for (int i = 1; i <= this.routing.switcher.getPluginHost().getOutputCount(); i++) {
			this.outBox.addItem(new Integer(i));
		}

		routing.block = false;

		inBox.setSelectedIndex(routing.inputNr - 1);
		outBox.setSelectedIndex(routing.outputNr - 1);
		activeListenBox.setSelectedIndex(routing.activeListenNr - 1);
	}

	protected JTextField status, low, high;

	public void createView() {

		this.updateComboBox();
		this.setLayout(new BorderLayout());
		this.setBackground(Color.RED);
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(activeListenBox);
		JLabel lbl = new JLabel("Status:");
		top.add(lbl);
		status = new JTextField(4);
		status.setText(Integer.toBinaryString(routing.statusFilter));// .substring(Integer.SIZE-Byte.SIZE+4));
		top.add(status);
		lbl = new JLabel("Low Value:");
		top.add(lbl);
		low = new JTextField(3);
		low.setText(Integer.toString(routing.lowValue));
		top.add(low);
		lbl = new JLabel("High Value:");
		top.add(lbl);
		high = new JTextField(3);
		high.setText(Integer.toString(routing.highValue));
		top.add(high);
		JButton confirm = new JButton("set");
		confirm.setActionCommand("CONFIRMED");
		confirm.addActionListener(routing);
		top.add(confirm);

		this.add(top, BorderLayout.NORTH);

		JPanel sourceSelect = new JPanel();
		sourceSelect.setLayout(new BoxLayout(sourceSelect, BoxLayout.X_AXIS));
		sourceSelect.add(this.inBox);
		sourceSelect.add(new JLabel(">"));
		sourceSelect.add(this.outBox);

		this.add(sourceSelect, BorderLayout.CENTER);
	}

}
