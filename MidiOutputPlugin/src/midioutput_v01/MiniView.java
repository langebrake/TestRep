package midioutput_v01;

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
	private int selectedChannel;

	public MiniView(LinkedList<MidiDevice> list, ActionListener listener, String selectedItem, int selectedChannel) {
		this.setLayout(new BorderLayout());
		this.selectedChannel = selectedChannel;
		this.listener = listener;
		this.selectedItem = selectedItem;
		this.updateList(list);
	}

	public void updateList(LinkedList<MidiDevice> list) {
		this.list = new LinkedList<String>();
		for (MidiDevice m : list) {
			this.list.add(m.getDeviceInfo().getName());
		}
		this.createGui();
		this.setPreferredSize(this.getPreferredSize());
	}

	private void createGui() {
		if (this.getComponentCount() != 0) {
			this.removeAll();
		}
		JComboBox<String> channelSelect = new JComboBox<String>();
		channelSelect.addItem("omni");
		for (int i = 1; i <= 16; i++) {
			channelSelect.addItem(Integer.toString(i));
		}
		channelSelect.setActionCommand("CHANNEL");
		channelSelect.addActionListener(listener);
		channelSelect.setSelectedIndex(selectedChannel + 1);
		this.add(channelSelect, BorderLayout.EAST);

		JComboBox<String> cbox = new JComboBox<String>();
		for (String m : list) {
			cbox.addItem(m);
		}

		this.add(cbox, BorderLayout.CENTER);
		cbox.setActionCommand("DEVICE");
		cbox.addActionListener(listener);
		if (selectedItem != null) {
			cbox.setSelectedItem(selectedItem);
		}

	}

}
