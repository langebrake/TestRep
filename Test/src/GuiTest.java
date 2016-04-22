import gui.InteractiveModule;
import gui.InteractivePane;

import javax.swing.JComponent;
import javax.swing.JFrame;

import model.MidiGraph;
import controller.InteractiveGuiController;


public class GuiTest {
	
	
	
	private static void createAndShowGui() {
		JFrame mainFrame = new JFrame("GuiTest");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		InteractivePane contentPane = new InteractivePane();
		InteractiveGuiController controller = new InteractiveGuiController(contentPane, new MidiGraph() );
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