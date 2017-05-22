package testsender_v01;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2347363809050759796L;
	private transient JButton sendButton;
	private transient JTextField status, channel, data1, data2;
	private transient TestSenderPlugin t;

	public MinView(TestSenderPlugin testSenderPlugin) {
		this.setLayout(new BorderLayout());
		this.sendButton = new JButton("send");
		this.t = testSenderPlugin;

		status = new JTextField();
		status.setText(Integer.toBinaryString(t.status));
		channel = new JTextField();
		channel.setText(Integer.toString(t.channel));
		data1 = new JTextField();
		data1.setText(Integer.toString(t.data1));
		data2 = new JTextField();
		data2.setText(Integer.toString(t.data2));

		JPanel message = new JPanel();
		message.setLayout(new BoxLayout(message, BoxLayout.X_AXIS));
		message.add(status);
		message.add(channel);
		message.add(data1);
		message.add(data2);
		this.add(sendButton, BorderLayout.SOUTH);
		this.add(message, BorderLayout.CENTER);
		this.sendButton.addActionListener(t);

	}

	public int status() {
		return Integer.parseInt(status.getText(), 2);
	}

	public int channel() {
		return Integer.parseInt(channel.getText());
	}

	public int data1() {
		return Integer.parseInt(data1.getText());
	}

	public int data2() {
		return Integer.parseInt(data2.getText());
	}
}
