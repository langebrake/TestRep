package transposenote_v01;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	
	private TransposeNote t;
	
	public MinView(TransposeNote t){
		this.t = t;
		this.createGui();
	}
	
	private JTextField transpose;
	private void createGui(){
		this.setLayout(new BorderLayout());
		transpose = new JTextField();
		transpose.setText(Byte.toString(t.transpose));
		JButton set = new JButton("set");
		set.addActionListener(t);
		this.add(transpose, BorderLayout.CENTER);
		this.add(set,BorderLayout.EAST);
	}
	
	public byte getTranspose(){
		return Byte.parseByte(transpose.getText());
	}
}
