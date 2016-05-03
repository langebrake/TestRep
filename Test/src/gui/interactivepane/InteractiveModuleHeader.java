package gui.interactivepane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import defaults.DefaultView;

public class InteractiveModuleHeader extends JPanel implements ActionListener {
	private InteractiveModule parent;
	public InteractiveModuleHeader(InteractiveModule parent){
		this.parent = parent;
		this.setMinimumSize(new Dimension(0,5));
		this.setLayout(new BorderLayout());
		JPanel head = new JPanel();
		head.setLayout(new GridBagLayout());
		this.add(head, BorderLayout.CENTER);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.EAST;
		c.gridx = 0;
		c.weightx = 0.3;
		c.weighty = 1;
		JButton left =new JButton("<");
		left.addActionListener(this);
		left.setActionCommand("LEFT");
		left.setFocusable(false);
		head.add(left, c );
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.weightx = 0.7;
		head.add(new DefaultView("HEADER"),c);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 2;
		c.weightx = 0.3;
		JButton right = new JButton(">");
		right.addActionListener(this);
		right.setActionCommand("RIGHT");
		right.setFocusable(false);
		head.add(right,c);
		this.setBackground(Color.BLACK);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getActionCommand().equals("LEFT")){
			parent.inputPopout(!parent.hasInputPopout(), !parent.hasInputPopout());
		} else {
			parent.outputPopout(!parent.hasOutputPopout(), !parent.hasOutputPopout());
		}
		
	}
	
}
