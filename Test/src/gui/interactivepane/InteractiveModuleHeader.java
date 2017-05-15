package gui.interactivepane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import defaults.DefaultView;

public class InteractiveModuleHeader extends JPanel implements ActionListener {
	private InteractiveModule parent;
	private DefaultView display;
	private JButton left, right;
	private JPanel head;

	public InteractiveModuleHeader(InteractiveModule parent) {
		this.parent = parent;
		this.setMinimumSize(new Dimension(0, 0));
		this.setPreferredSize(new Dimension(0, 5));
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setLayout(new BorderLayout());
		head = new JPanel();
		head.setLayout(new GridBagLayout());
		head.setOpaque(false);
		this.add(head, BorderLayout.CENTER);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.WEST;
		left = new JButton("<");
		left.setMinimumSize(new Dimension(0, 0));
		left.setOpaque(false);
		left.setContentAreaFilled(false);
		left.addActionListener(this);
		left.setActionCommand("LEFT");
		left.setFocusable(false);
		left.setPreferredSize(new Dimension(0, 0));
		left.setBorderPainted(false);
		;
		c.gridx = 0;
		c.gridwidth = 1;
		head.add(left, c);
		c.gridwidth = 3;
		this.display = new DefaultView();
		display.setMinimumSize(new Dimension(0, 0));
		display.setPreferredSize(new Dimension(0, 0));
		display.setOpaque(false);
		c.gridx = 1;
		c.weightx = 3;
		head.add(display, c);
		c.weightx = 1;
		c.gridwidth = 1;
		c.gridx = 4;
		right = new JButton(">");
		right.setOpaque(false);
		right.setContentAreaFilled(false);
		right.addActionListener(this);
		right.setActionCommand("RIGHT");
		right.setFocusable(false);
		right.setMinimumSize(new Dimension(0, 0));
		right.setPreferredSize(new Dimension(0, 0));
		right.setBorderPainted(false);
		head.add(right, c);
	}

	public void setText(String text) {
		this.display.setText(text);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("LEFT")) {
			parent.inputPopout(!parent.hasInputPopout(),
					!parent.hasInputPopout());
		} else {
			parent.outputPopout(!parent.hasOutputPopout(),
					!parent.hasOutputPopout());
		}

	}

}
