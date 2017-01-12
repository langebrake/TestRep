package networkmidioutput_v01;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	private NetworkMidiOutput out;

	public MinView(NetworkMidiOutput out) {
		this.out = out;
		this.createGui();
	}

	private JTextField port;
	private JTextField name;

	private void createGui() {
		this.setLayout(new BorderLayout());
		name = new JTextField();
		name.setText(out.name);
		port = new JTextField();
		port.setText(Integer.toString(out.port));
		JButton connect = new JButton("connect");
		connect.addActionListener(out);

		this.add(name, BorderLayout.CENTER);
		this.add(port, BorderLayout.EAST);
		this.add(connect, BorderLayout.SOUTH);
	}

	public String getServerName() {
		return name.getText();
	}

	public int getServerPort() {
		return Integer.parseInt(port.getText());
	}

}
