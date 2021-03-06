package networkmidiinput_v01;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4309131068695026412L;
	private NetworkMidiInput nmi;

	public MinView(NetworkMidiInput networkMidiInput) {
		this.setLayout(new BorderLayout());
		this.nmi = networkMidiInput;
		this.createGui();
	}

	private JTextField port;

	private void createGui() {
		port = new JTextField();
		JButton setup = new JButton("create");
		setup.addActionListener(nmi);
		port.setText(Integer.toString(nmi.port));
		this.add(port, BorderLayout.CENTER);
		this.add(setup, BorderLayout.EAST);
	}

	public int getPort() {
		// TODO:Error Management
		return Integer.parseInt(port.getText());
	}
}
