import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.sound.midi.MidiDevice;
import javax.swing.JComboBox;
import javax.swing.JPanel;


public class MiniView extends JPanel {
	private LinkedList<String> list;
	private ActionListener listener;
	private String selectedItem;
	public MiniView(LinkedList<MidiDevice> list, ActionListener listener,String selectedItem){
		this.setLayout(new BorderLayout());

		this.listener = listener;
		this.selectedItem = selectedItem;
		this.updateList(list);
	}
	
	public void updateList(LinkedList<MidiDevice> list){
		this.list = new LinkedList<String>();
		for(MidiDevice m:list){
			this.list.add(m.getDeviceInfo().getName());
		}
		this.createGui();
		this.setPreferredSize(this.getPreferredSize());
	}
	
	private void createGui(){
		JComboBox<String> cbox = new JComboBox<String>();
		for(String m:list){
			cbox.addItem(m);
		}
		if(this.getComponentCount() != 0){
			this.remove(this.getComponent(0));
		}
		this.add(cbox, BorderLayout.CENTER);
		cbox.addActionListener(listener);
		if(selectedItem!=null){
			cbox.setSelectedItem(selectedItem);
		}
	}

	
}
