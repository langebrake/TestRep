package midinotezoneplugin;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ZoneView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8888235307890876321L;
	private Zone zone;

	public ZoneView(Zone zone) {
		this.zone = zone;
		this.createGui();
	}

	private JTextField low, high;

	private void createGui() {
		low = new JTextField();
		low.setText(Byte.toString(zone.getLow()));
		high = new JTextField();
		high.setText(Byte.toString(zone.getHigh()));
		JButton set = new JButton("set");
		set.addActionListener(zone);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(low);
		this.add(high);
		this.add(set);
	}

	public byte getLow() {
		return Byte.parseByte(low.getText());
	}

	public byte getHigh() {
		return Byte.parseByte(high.getText());
	}

}
