import java.io.FileInputStream;
import model.pluginmanager.PluginManager;
import controller.Controller;

public class MMTStarter {
	private static void createAndShowGui() throws Exception {

		Controller c = new Controller();
		c.getMainFrame().setVisible(true);

	}

	public static void main(String[] args) throws Exception {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowGui();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		PluginManager.loadPlugins();
	}
}