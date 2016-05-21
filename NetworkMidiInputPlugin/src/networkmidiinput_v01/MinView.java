package networkmidiinput_v01;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.net.SocketImpl;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	private NetworkMidiInput nmi;
	public MinView(NetworkMidiInput networkMidiInput) {
		this.setLayout(new BorderLayout());
		this.nmi = networkMidiInput;
		this.createGui();
	}
	
	private JTextField port;
	private void createGui(){
		port = new JTextField();
		JButton setup = new JButton("create");
		setup.addActionListener(nmi);
	}
	
	public int getPort() {
		//TODO:Error Management
		return Integer.parseInt(port.getText());
	}
}
