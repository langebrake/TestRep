package controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLClassLoader;
import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.pluginmanager.PluginManager;
import controller.interactivepane.InteractiveController;
import engine.Engine;

public class Controller implements Serializable,WindowListener {
	
	private JFrame mainFrame;
	private LinkedList<InteractiveController> interactivePanes;
	private transient LinkedList<URLClassLoader> classes;
	
	public Controller(){
		mainFrame = new JFrame("GuiTest");
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		InteractiveController controller = new InteractiveController();
		interactivePanes = new LinkedList<InteractiveController>();
		interactivePanes.add(controller);
		mainFrame.addWindowStateListener(controller);
		mainFrame.add(controller.getPane());
		mainFrame.pack();
		Controller c = this;
		mainFrame.addWindowListener(this);
		try {
			PluginManager.loadPlugins();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.classes = PluginManager.classes;
	}
	
	public InteractiveController getActiveInteractiveController(){
		return null;
	}
	
	public JFrame getMainFrame(){
		return this.mainFrame;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
//		JOptionPane jop = new JOptionPane();
//		if (JOptionPane.showConfirmDialog(this.getMainFrame(), 
//	            "Save State?", "Exit Handling", 
//	            JOptionPane.YES_NO_OPTION,
//	            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
	            try{
			FileOutputStream fout = new FileOutputStream("./controller.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			oos.close();
	            }catch (Exception ex){
	             ex.printStackTrace();	
	            }
			
//	        }
	            this.mainFrame.setVisible(false);
		System.out.println("Exit");
		System.exit(0);
	}
		

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//		try {
//			PluginManager.loadPlugins();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			PluginManager.loadPlugins();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Engine e = Engine.load();
			e.refreshMidiDevices();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.defaultReadObject();
		
		
		
		
	}
}
