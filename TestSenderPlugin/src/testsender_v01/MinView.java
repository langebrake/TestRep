package testsender_v01;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MinView extends JPanel implements ActionListener{
	private transient JButton sendButton;
	private transient JTextField textField;
	private transient TestSenderPlugin t;
	public MinView(TestSenderPlugin testSenderPlugin){
		this.setLayout(new BorderLayout());
		this.sendButton = new JButton("send");
		this.textField = new JTextField();
		textField.setEditable(true);
		this.add(sendButton,  BorderLayout.EAST);
		this.add(textField,BorderLayout.CENTER);
		this.sendButton.addActionListener(this);
		this.t = testSenderPlugin;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		t.send(Integer.parseInt(textField.getText()));
	}
}
