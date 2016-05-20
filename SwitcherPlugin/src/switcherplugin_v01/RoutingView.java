package switcherplugin_v01;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class RoutingView extends JPanel {
	private Routing routing;
	protected JComboBox<Integer> inBox, outBox;
	public RoutingView(Routing routing) {
		this.routing = routing;
		this.inBox = new JComboBox<Integer>();
		this.outBox = new JComboBox<Integer>();
		this.inBox.addItemListener(routing);
		this.outBox.addItemListener(routing);
		this.createView();
	}
	
	protected void updateComboBox(){
		
		this.inBox.removeAllItems();
		this.outBox.removeAllItems();
		
		routing.block = true;
		for(int i = 1;i<=this.routing.switcher.getPluginHost().getInputCount();i++){
			this.inBox.addItem(new Integer(i));
		}
		for(int i = 1;i<=this.routing.switcher.getPluginHost().getOutputCount();i++){
			this.outBox.addItem(new Integer(i));
		}
		routing.block=false;
		
		inBox.setSelectedIndex(routing.inputNr-1);
		outBox.setSelectedIndex(routing.outputNr -1);
	}
	protected JTextField status,low,high;
	
	public void createView(){
		
		this.updateComboBox();
		this.setLayout(new BorderLayout());
		this.setBackground(Color.RED);
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		JLabel lbl = new JLabel("Status:");
		top.add(lbl);
		status = new JTextField(4);
		top.add(status);
		lbl = new JLabel("Low Value:");
		top.add(lbl);
		low = new JTextField(3);
		top.add(low);
		lbl = new JLabel("High Value:");
		top.add(lbl);
		high=new JTextField(3);
		top.add(high);
		JButton confirm = new JButton("set");
		confirm.setActionCommand("CONFIRMED");
		confirm.addActionListener(routing);
		top.add(confirm);
		
		this.add(top,BorderLayout.NORTH);
		
		JPanel sourceSelect = new JPanel();
		sourceSelect.setLayout(new BoxLayout(sourceSelect,BoxLayout.X_AXIS));
		sourceSelect.add(this.inBox);
		sourceSelect.add(new JLabel(">"));
		sourceSelect.add(this.outBox);
		
		this.add(sourceSelect, BorderLayout.CENTER);
	}
	
	

}
