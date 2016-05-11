package controller.maincontrol;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.JPanel;

import controller.interactivepane.InteractiveController;
import engine.Stringer;

public class Project implements Serializable{
	private transient Container contentPane;
	private LinkedList<InteractiveController> interactivePanes;
	
	public Project(){
		createContentPane();
	}
	
	private void createContentPane(){
		this.contentPane = new JPanel();
		this.contentPane.setLayout(new BorderLayout());
		if(interactivePanes == null){
		InteractiveController controller = new InteractiveController();
		interactivePanes = new LinkedList<InteractiveController>();
		interactivePanes.add(controller);
		}
		this.contentPane.add(interactivePanes.get(0).getPane(),BorderLayout.CENTER);
	}
	
	public Container getContentPane(){
		return this.contentPane;
	}

	public void close() {
		for(InteractiveController c:interactivePanes){
			c.close();
		}
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		String stringer = Stringer.getString();
		System.out.println(stringer +"PROJECT_START SAVE");
		out.defaultWriteObject();
		System.out.println(stringer +"PROJECT_END SAVE");
		Stringer.minimize();
	}
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		this.createContentPane();
	}
}
