package controller.clipboard;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

import java.util.LinkedList;

import controller.interactivepane.InteractiveController;

public class Clipboard {
	
	private LinkedList<InteractiveComponent> clipboard;
	private InteractiveController controller;
	private Vector copyGridLocation;
	
	public Clipboard(){
		this.clipboard = new LinkedList<InteractiveComponent>();
	}
	
	public void setController(InteractiveController c){
		this.controller = c;
	}
	
	public void setClipboard(LinkedList<InteractiveComponent> clipboard){
		this.clipboard = clipboard;
	}
	
	public void setCopyLocation(Vector copyGridLocation){
		this.copyGridLocation = copyGridLocation;
	}
	
	public Vector getCopyGridLocation(){
		return this.copyGridLocation;
	}
	
	/**
	 * will only return cloned modules, general components are not cloned.
	 * @param originLocation
	 * @param destinationController
	 * @return
	 */
	public LinkedList<InteractiveComponent> getClipboardClone(Vector offset, InteractiveController destinationController){
		LinkedList<InteractiveComponent> tmp = new LinkedList<InteractiveComponent>();
		for(InteractiveComponent c: clipboard){
			if(c instanceof InteractiveModule)
				tmp.add(((InteractiveModule)c).cloneTo(offset, destinationController));
			
		}
		return tmp;
	}

	public boolean hasClipboard() {
		// TODO Auto-generated method stub
		return this.clipboard.size() != 0;
	}
}
