import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;

import javax.swing.JComponent;
import javax.swing.JFrame;

import model.MidiGraph;
import model.pluginmanager.PluginManager;
import controller.Controller;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;
import controller.interactivepane.InteractiveGuiController;


public class GuiTest {
	
	
	
	private static void createAndShowGui() {
		JFrame mainFrame = new JFrame("GuiTest");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		InteractiveController controller = new InteractiveController();
		mainFrame.setContentPane(controller.getPane());
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