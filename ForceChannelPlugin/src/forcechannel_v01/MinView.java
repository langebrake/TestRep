package forcechannel_v01;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class MinView extends JPanel {
	public MinView(ForceChannel f){
		this.setLayout( new BorderLayout());
		JComboBox<String> channelSelect = new JComboBox<String>();
		channelSelect.addItem("omni");
		for(int i = 1; i<=16;i++){
			channelSelect.addItem(Integer.toString(i));
		}
		channelSelect.setActionCommand("CHANNEL");
		channelSelect.addActionListener(f);
		channelSelect.setSelectedIndex(f.selectedChannel + 1);
		this.add(channelSelect, BorderLayout.CENTER);
	}
}
