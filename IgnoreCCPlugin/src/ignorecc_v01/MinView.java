package ignorecc_v01;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	private JTextField ignore;

	public MinView(IgnoreCC n) {
		this.setLayout(new BorderLayout());
		ignore = new JTextField();
		ignore.setText(Byte.toString(n.ignore));
		JButton set = new JButton("set");
		set.addActionListener(n);
		this.add(ignore, BorderLayout.CENTER);
		this.add(set, BorderLayout.EAST);
	}

	public byte getIgnore() {
		return Byte.parseByte(ignore.getText());
	}
}
