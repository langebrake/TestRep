package controller.maincontrol;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.JPanel;

import controller.interactivepane.InteractiveController;

public class Project implements Serializable{
	private Container contentPane;
	private LinkedList<InteractiveController> interactivePanes;
	public Project(){
		createContentPane();
	}
	
	private void createContentPane(){
		this.contentPane = new JPanel();
		this.contentPane.setLayout(new BorderLayout());
		InteractiveController controller = new InteractiveController();
		interactivePanes = new LinkedList<InteractiveController>();
		interactivePanes.add(controller);
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
}
