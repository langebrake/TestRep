package controller;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.pluginmanager.PluginManager;
import controller.interactivepane.InteractiveController;
import controller.maincontrol.OpenProject;
import controller.maincontrol.Project;
import controller.maincontrol.SaveProject;
import engine.Engine;

public class Controller implements WindowListener {
	
	private transient JFrame mainFrame;
	private Project project;
	private JFileChooser fileChooser;
	private File currentProject;
	
	public Controller(){
		this.project = new Project();
		this.fileChooser = new JFileChooser();
		FileNameExtensionFilter fne = new FileNameExtensionFilter("Midi Manipulation Project" ,".mmp");
		this.fileChooser.setFileFilter(fne);
		this.createGui();
		this.initClasses();
	}
	public Project getProject(){
		return this.project;
	}
	private void createGui(){
		mainFrame = new JFrame("Midi Manipulator v0.1a");
		
		JMenuBar mainBar = this.createMenuBar();
		mainFrame.setJMenuBar(mainBar);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.addWindowListener(this);
		mainFrame.setContentPane(this.project.getContentPane());
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar m = new JMenuBar();
		m.add(this.createFileMenu());
		return m;
	}
	
	private JMenu createFileMenu(){
		JMenu m = new JMenu("File");
		JMenuItem open = new JMenuItem(new OpenProject(this));
		JMenuItem save = new JMenuItem(new SaveProject(this));
		m.add(open);
		m.add(save);
		return m;
	}
	
	public File getCurrentProject(){
		return this.currentProject;
	}
	private void initClasses(){
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
	}
	
	
	public JFrame getMainFrame(){
		return this.mainFrame;
	}
	
	public void loadProject(Project project){
		this.project = project;
		this.mainFrame.setContentPane(project.getContentPane());
	}
	
	public void loadProject(File file){
		FileInputStream fos;
		try {
			fos = new FileInputStream(file);
			ObjectInputStream oos = new ObjectInputStream(fos);
			this.project = (Project) oos.readObject();
			this.loadProject(project);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassCastException e) { 
			e.printStackTrace();
		}
		
	}
	
	public void saveProject(File file){
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this.getProject());
			this.currentProject = file;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	public JFileChooser getFileChooser(){
		return this.fileChooser;
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
//	            try{
//			FileOutputStream fout = new FileOutputStream("./controller.ser");
//			ObjectOutputStream oos = new ObjectOutputStream(fout);
//			oos.writeObject(this);
//			oos.close();
//	            }catch (Exception ex){
//	             ex.printStackTrace();	
//	            }
			
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
	
}
