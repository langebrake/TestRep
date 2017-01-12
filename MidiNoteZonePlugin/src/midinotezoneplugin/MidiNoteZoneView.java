package midinotezoneplugin;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MidiNoteZoneView extends JPanel {
	private MidiNoteZone mnz;

	public MidiNoteZoneView(MidiNoteZone z) {
		this.mnz = z;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.updateView();
	}

	void updateView() {
		this.removeAll();
		for (Zone z : mnz.zones) {
			if (!this.isAncestorOf(z.getView())) {
				this.add(z.getView());
			}
		}

		JButton add = new JButton("+");
		add.addActionListener(mnz);
		this.add(add);
		this.setSize(this.getPreferredSize());
		this.revalidate();
	}
}
