package convergenotes_v01;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MinView extends JPanel {
	private JTextField note;
	public MinView(ConvergeNotes n){
		this.setLayout(new BorderLayout());
		note = new JTextField();
		note.setText(Byte.toString(n.note));
		JButton set = new JButton("set");
		set.addActionListener(n);
		this.add(note, BorderLayout.CENTER);
		this.add(set, BorderLayout.EAST);
	}
	
	public byte getNote(){
		return Byte.parseByte(note.getText());
	}
}
