package defaults;


import guiinterface.InteractiveUpdateable;
import guiinterface.SizeableLabel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class DefaultView extends JPanel implements InteractiveUpdateable {
	private JLabel lbl;
	public DefaultView(){
		this("DefaultPlugin");
	}
	public DefaultView(String name){
		super(new BorderLayout());
		this.setSize(200, 100);
		this.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e) {
                DefaultView.this.sizeFont();
            }
		});
		this.lbl = new JLabel(name);
		this.add(lbl,BorderLayout.CENTER);
	}
	private void sizeFont(){
		Font labelFont = this.lbl.getFont();
		String labelText = this.lbl.getText();

		int stringWidth = this.lbl.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = this.lbl.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio)-1;
		int componentHeight = this.lbl.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		// Set the label's font size to the newly determined size.
		this.lbl.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
	}
	@Override
	public void updateView() {
		
		
	}
	
}
