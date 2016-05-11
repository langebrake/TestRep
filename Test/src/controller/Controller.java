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
import controller.maincontrol.NewProject;
import controller.maincontrol.OpenProject;
import controller.maincontrol.Project;
import controller.maincontrol.SaveProject;
import engine.Engine;
import engine.Stringer;

public class Controller implements WindowListener {
	
	private transient JFrame mainFrame;
	private Project project;
	private JFileChooser fileChooser;
	private File currentProject;
	private final String programName = "Midi Manipulator v0.1a";
	
	public Controller(){
		this.fileChooser = new JFileChooser();
		FileNameExtensionFilter fne = new FileNameExtensionFilter("Midi Manipulation Project" ,"mmp");
		this.fileChooser.setFileFilter(fne);
		this.createGui();
		this.loadProject(new Project());
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
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setSize(500,500);
	}
	
	private JMenuBar createMenuBar(){
		JMenuBar m = new JMenuBar();
		m.add(this.createFileMenu());
		return m;
	}
	
	private JMenu createFileMenu(){
		JMenu m = new JMenu("File");
		m.add(new JMenuItem(new NewProject(this)));
		m.add(new JMenuItem(new OpenProject(this)));
		m.add(new JMenuItem(new SaveProject(this)));
		
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
		String title = (this.currentProject == null) ? "Untitled" : this.currentProject.getName();
		this.mainFrame.setTitle(this.programName + " - "+ title);
		this.mainFrame.revalidate();
		System.gc();
	}
	
	public void newProject(){
		this.currentProject = null;
		closeProject();
		this.loadProject(new Project());
		
	}
	
	public void closeProject(){
		if(this.project != null) {
			this.project.close();
		}
	}
	
	public void loadProject(File file){
		closeProject();
		FileInputStream fos;
		try {
			fos = new FileInputStream(file);
			ObjectInputStream oos = new ObjectInputStream(fos);
			this.project = (Project) oos.readObject();
			this.currentProject = file;
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
		Stringer.stringer = "";
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
