package analyzerplugin_v01;
import java.awt.BorderLayout;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MinView extends JPanel {
	private transient JLabel label;
	public MinView(){
		this.setLayout(new BorderLayout());
		this.label = new JLabel("no MSG received yet");
		this.add(label, BorderLayout.CENTER);
	}
	
	public void updateView(MidiMessage msg){
		String message = Integer.toBinaryString(msg.getMessage()[0] & 0xFF).replace(' ', '0')+" "+msg.getMessage()[1]+ " " + msg.getMessage()[2];
		label.setText(message);
	}
	
}
