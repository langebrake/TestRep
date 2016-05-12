package defaults;


import guiinterface.InteractiveUpdateable;
import guiinterface.SizeableLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class DefaultView extends JPanel implements InteractiveUpdateable {
	private JLabel lbl;
	public DefaultView(){
		this("TEST");
	}
	public DefaultView(String name){
		super(new BorderLayout());
		this.setPreferredSize(new Dimension(200,100));
		this.setMinimumSize(new Dimension(0,10));
		this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e) {
                DefaultView.this.sizeFont();
            }
		});
		this.lbl = new JLabel("",SwingConstants.CENTER);
		this.lbl.setText(name);
		lbl.setOpaque(false);
		this.add(lbl,BorderLayout.CENTER);
	}

	private void sizeFont(){
//		Font labelFont = this.lbl.getFont();
//		String labelText = this.lbl.getText();
//
//		int stringWidth = this.lbl.getFontMetrics(labelFont).stringWidth(labelText);
//		int componentWidth = this.lbl.getWidth();
//
//		// Find out how much the font can grow in width.
//		double widthRatio = (double)componentWidth / (double)stringWidth;
//
//		int newFontSize = (int)(labelFont.getSize() * widthRatio)-2;
//		int componentHeight = this.lbl.getHeight();
//
//		// Pick a new font size so it will not be larger than the height of label.
//		int fontSizeToUse = Math.min(newFontSize, componentHeight);
//
//		// Set the label's font size to the newly determined size.
//		this.lbl.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
//		lbl.paintImmediately(lbl.getVisibleRect());
//		this.paintImmediately(getVisibleRect());
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e) {
                DefaultView.this.sizeFont();
            }
		});
	}
	
	@Override
	public void updateView() {
		
		
	}
	
	public void setText(String text){
		this.lbl.setText(text);
	}
}
