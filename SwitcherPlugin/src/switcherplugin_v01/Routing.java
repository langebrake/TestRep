package switcherplugin_v01;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import defaults.MidiIOCommunicator;
import defaults.MidiListener;

public class Routing implements Serializable, MidiListener, Cloneable, ActionListener, ItemListener {
	protected SwitcherPlugin switcher;
	protected int inputNr, outputNr, activeListenNr;
	private boolean active;
	protected transient RoutingView routingView;
	protected byte statusFilter;
	protected byte lowValue;
	protected byte highValue;
	public boolean block;
	private transient ActiveListener activeListener;

	public Routing(SwitcherPlugin switcher) {
		this.switcher = switcher;
		this.inputNr = 0;
		this.outputNr = 0;
		this.active = false;
		this.activeListenNr = 0;
	}

	public void setSwitcher(SwitcherPlugin s) {
		this.switcher = s;
	}

	public void init() {
		this.routingView = new RoutingView(this);
		this.activeListener = new ActiveListener();
		this.updateInput(this.inputNr);
	}

	private void updateInput(int oldInput) {
		if (oldInput != 0) {
			this.switcher.getPluginHost().getInput(oldInput - 1).removeMidiListener(this);
		}
		if (inputNr != 0) {
			this.switcher.getPluginHost().getInput(inputNr - 1).addMidiListener(this);
		}
	}

	@Override
	public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {

		if (this.outputNr != 0 && active) {

			this.switcher.getPluginHost().getOutput(outputNr - 1).send(msg, timestamp);
		}

	}

	public JPanel getView() {
		return routingView;
	}

	public Routing clone(SwitcherPlugin switcherPlugin) {
		Routing tmp = new Routing(switcherPlugin);
		tmp.inputNr = this.inputNr;
		tmp.outputNr = this.outputNr;
		tmp.active = this.active;
		tmp.statusFilter = this.statusFilter;
		tmp.lowValue = this.lowValue;
		tmp.highValue = this.highValue;
		tmp.activeListenNr = this.activeListenNr;
		return tmp;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.routingView != null && this.routingView.isAncestorOf((Component) e.getSource())) {
			String command = e.getActionCommand();
			if (command.equals("CONFIRMED")) {
				this.lowValue = Byte.parseByte(routingView.low.getText());
				this.highValue = Byte.parseByte(routingView.high.getText());
				this.statusFilter = Byte.parseByte(routingView.status.getText(), 2);

			} else if (command.equals("INPUT")) {

			} else if (command.equals("OUTPUT")) {

			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if (this.routingView != null && arg0.getStateChange() == ItemEvent.SELECTED && !block) {
			if (arg0.getSource() == this.routingView.inBox) {
				int oldInput = inputNr;
				this.inputNr = (int) arg0.getItem();
				this.updateInput(oldInput);
			} else if (arg0.getSource() == this.routingView.outBox) {
				this.outputNr = (int) arg0.getItem();
			} else if (arg0.getSource() == this.routingView.activeListenBox) {
				this.activeListener.setListen((int) arg0.getItem() - 1);
			}
		}

	}

	private class ActiveListener implements MidiListener {
		private int listenOn;

		public ActiveListener() {
			if (activeListenNr != 0)
				this.setListen(activeListenNr - 1);
		}

		public void listen(MidiIOCommunicator source, MidiMessage msg, long timestamp) {
			int statusMSG = (msg.getMessage()[0] & 0xf0) >> 4;
			if (statusMSG == statusFilter && msg.getMessage()[1] <= highValue && msg.getMessage()[1] >= lowValue) {
				System.out.println("ACTIVATE");
				active = true;
			} else if (statusMSG == statusFilter) {
				System.out.println("DEACTIVATE");
				if (active) {
					for (int i = 0; i <= 15; i++) {
						try {
							ShortMessage noteOff = new ShortMessage(0b10110000, i, 123, 0);
							ShortMessage pedalOff = new ShortMessage(0b10110000, i, 64, 0);
							switcher.getPluginHost().getOutput(outputNr - 1).send(noteOff, -1);
							switcher.getPluginHost().getOutput(outputNr - 1).send(pedalOff, -1);
						} catch (InvalidMidiDataException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				active = false;
			}

		}

		public void setListen(int listenOn) {
			switcher.getPluginHost().getInput(this.listenOn).removeMidiListener(this);
			this.listenOn = listenOn;
			Routing.this.activeListenNr = listenOn + 1;
			switcher.getPluginHost().getInput(this.listenOn).addMidiListener(this);
			System.out.println("Listen on: " + this.listenOn);
		}

	}

}
