package switcherplugin_v01;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;

import javax.sound.midi.MidiMessage;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import defaults.MidiIO;
import defaults.MidiListener;

public class Routing implements Serializable, MidiListener,Cloneable,ActionListener, ItemListener {
	protected SwitcherPlugin switcher;
	protected int inputNr, outputNr;
	private boolean active;
	protected transient RoutingView routingView;
	private byte statusFilter;
	private byte lowValue;
	private byte highValue;
	public boolean block;
	public Routing(SwitcherPlugin switcher){
		this.switcher = switcher;
		this.inputNr = 0;
		this.outputNr = 0;
		this.active = false;
		this.init();
	}
	
	public void setSwitcher(SwitcherPlugin s){
		this.switcher = s;
	}
	public void init(){
		this.routingView = new RoutingView(this);
		this.updateInput(0);
	}
	
	private void updateInput(int oldInput){
		if(oldInput != 0){
			this.switcher.getPluginHost().getInput(oldInput-1).removeMidiListener(this);
		}
		if(inputNr != 0){
			this.switcher.getPluginHost().getInput(inputNr-1).addMidiListener(this);
		}
	}

	@Override
	public void listen(MidiIO source, MidiMessage msg, long timestamp) {
		int statusMSG = (msg.getMessage()[0] & 0xf0) >> 4;
		if(statusMSG == this.statusFilter && msg.getMessage()[1]<=this.highValue && msg.getMessage()[1]>=this.lowValue){
			System.out.println("ACTIVATE");
			active = true;
		} else {
			System.out.println("DEACTIVATE");
			active = false;
		}
		if(this.outputNr != 0 && active){
			
			this.switcher.getPluginHost().getOuput(outputNr-1).send(msg, timestamp);
		}
		
	}
	
	public JPanel getView(){
		return routingView;
	}
	
	public Routing clone(){
		Routing tmp = new Routing(this.switcher);
		tmp.inputNr = this.inputNr;
		tmp.outputNr = this.outputNr;
		tmp.active = this.active;
		return tmp;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(this.routingView!=null && this.routingView.isAncestorOf((Component) e.getSource())){
			String command = e.getActionCommand();
			if(command.equals("CONFIRMED")){
				this.lowValue = Byte.parseByte(routingView.low.getText());
				this.highValue = Byte.parseByte(routingView.high.getText());
				this.statusFilter = Byte.parseByte(routingView.status.getText(),2);
				
			} else if(command.equals("INPUT")){
				
			} else if(command.equals("OUTPUT")){
				
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if(this.routingView != null && arg0.getStateChange() == ItemEvent.SELECTED && !block){
			if(arg0.getSource() == this.routingView.inBox){
				int oldInput = inputNr;
				this.inputNr = (int) arg0.getItem();
				this.updateInput(oldInput);
			} else if(arg0.getSource() == this.routingView.outBox){
				this.outputNr = (int) arg0.getItem();
			}
		}
		
		
	}
	
	
	
}
