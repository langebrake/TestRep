import gui.InteractiveGuiComponent;
import gui.InteractiveGuiPane;

import javax.swing.JComponent;
import javax.swing.JFrame;


public class GuiTest {
	
	
	
	private static void createAndShowGui() {
		JFrame mainFrame = new JFrame("GuiTest");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComponent contentPane = new InteractiveGuiPane();
		mainFrame.setContentPane(contentPane);
		mainFrame.pack();
		mainFrame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
	}
}