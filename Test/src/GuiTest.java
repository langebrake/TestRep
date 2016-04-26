import java.io.IOException;

import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;

import javax.swing.JComponent;
import javax.swing.JFrame;

import model.MidiGraph;
import model.pluginmanager.PluginManager;
import controller.Controller;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;


public class GuiTest {
	
	
	
	private static void createAndShowGui() throws Exception {
		JFrame mainFrame = new JFrame("GuiTest");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		InteractiveController controller = new InteractiveController();
		mainFrame.setContentPane(controller.getPane());
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		
	}
	
	public static void main(String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGui();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		PluginManager.loadPlugins();
	}
}