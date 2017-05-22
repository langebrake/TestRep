package midinotezoneplugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.sound.midi.MidiMessage;

import defaults.MidiListener;
import dev.MidiIOCommunicator;
import engine.MidiUtilities;

public class Zone implements MidiListener, ActionListener, Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3225867591760294801L;
	private int index;
	private MidiNoteZone mnz;
	private byte low, high;
	private transient ZoneView view;

	public Zone(MidiNoteZone z, int index) {
		this.mnz = z;
		this.low = 0;
		this.high = 127;
		this.index = index;
	}

	public void setIndex(int i) {
		this.index = i;
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
		byte status = MidiUtilities.getStatus(msg);
		if ((status == MidiUtilities.NOTE_ON || status == MidiUtilities.NOTE_OFF)) {
			if (MidiUtilities.getData1(msg) >= low && MidiUtilities.getData1(msg) <= high) {
				this.mnz.getPluginHost().getOutput(index).send(msg, timestamp);
			}
		}

	}

	protected void init() {
		this.view = new ZoneView(this);
		this.mnz.getPluginHost().getInput(0).addMidiListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.low = view.getLow();
		this.high = view.getHigh();

	}

	public byte getLow() {
		return this.low;
	}

	public byte getHigh() {
		return this.high;
	}

	public Zone clone() {
		Zone tmp = new Zone(this.mnz, this.index);
		tmp.high = high;
		tmp.low = low;
		return tmp;
	}

	public ZoneView getView() {
		return this.view;
	}

}
