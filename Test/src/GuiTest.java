import java.io.FileInputStream;
import model.pluginmanager.PluginManager;
import controller.Controller;


public class GuiTest {
	private static void createAndShowGui() throws Exception {
		
		Controller c = new Controller();
		c.getMainFrame().setVisible(true);
//		try{
//		FileInputStream fin = new FileInputStream("./controller.ser");
//		DebuggingObjectInputStream ois = new DebuggingObjectInputStream(fin);
//		Controller c = (Controller) ois.readObject();
//		ois.close();
//		c.getMainFrame().setVisible(true);
//		} catch (Exception ex){
//			System.out.println("No saved file found");
//			ex.printStackTrace();
//			try{
//				Controller c = new Controller();
//				c.getMainFrame().setVisible(true);
//				} catch (Exception e){
//					e.printStackTrace();
//				}
//		}
//		c.getMainFrame().addWindowListener(new WindowAdapter(){
//			@Override
//			public void windowClosed(WindowEvent e) {
//				if (JOptionPane.showConfirmDialog(c.getMainFrame(), 
//			            "Save option?", "Save state?", 
//			            JOptionPane.YES_NO_OPTION,
//			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//			            try{
//					FileOutputStream fout = new FileOutputStream("./controller.ser");
//					ObjectOutputStream oos = new ObjectOutputStream(fout);
//					oos.writeObject(c);
//					oos.close();
//			            }catch (Exception ex){
//			            	
//			            }
//					
//			        }
//				System.out.println("Exit");
//				System.exit(0);
//			}
//		});
		
		
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