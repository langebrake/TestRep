import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.MidiGraph;
import model.pluginmanager.PluginManager;
import controller.Controller;
import controller.DebuggingObjectInputStream;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;


public class GuiTest {
	private Controller c;
	
	
	private static void createAndShowGui() throws Exception {
		
		
		try{
		FileInputStream fin = new FileInputStream("./controller.ser");
		DebuggingObjectInputStream ois = new DebuggingObjectInputStream(fin);
		Controller c = (Controller) ois.readObject();
		ois.close();
		c.getMainFrame().setVisible(true);
		} catch (Exception ex){
			System.out.println("No saved file found");
			ex.printStackTrace();
			try{
				Controller c = new Controller();
				c.getMainFrame().setVisible(true);
				} catch (Exception e){
					e.printStackTrace();
				}
		}
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