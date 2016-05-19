package controller.clipboard;

import gui.interactivepane.CablePoint;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

import java.util.LinkedList;

import controller.interactivepane.InteractiveController;
import controller.maincontrol.Project;

public class Clipboard {
	
	private LinkedList<InteractiveComponent> clipboard;
	private LinkedList<InteractiveCable> cableClipboard;
	private Vector copyGridLocation;
	private Project project;
	
	public Clipboard(){
		this.clipboard = new LinkedList<InteractiveComponent>();
		this.cableClipboard = new LinkedList<InteractiveCable>();
	}
	
	
	public void setProject(Project project){
		this.project = project;
	}
	
	public void setClipboard(LinkedList<InteractiveComponent> clipboard){
		this.clipboard = clipboard;
		this.cableClipboard = new LinkedList<InteractiveCable>();
		for(InteractiveComponent c: this.clipboard){
			if(c instanceof InteractiveModule){
				for(CablePoint p:((InteractiveModule) c).getCablePoints()){
					InteractiveCable cable = p.getCable();
					if(p.isConnected() &&
							clipboard.contains(cable.getSource().getHost()) &&
							clipboard.contains(cable.getDestination().getHost()) && 
							!this.cableClipboard.contains(cable)){
						
						this.cableClipboard.add(cable);
						
					}
				}
			}
		}
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
	
	public LinkedList<InteractiveComponent> getClipboard(){
		return this.clipboard;
	}
	
	public LinkedList<InteractiveCable> getCableClipboard(){
		return this.cableClipboard;
	}
	
	public boolean hasClipboard() {
		// TODO Auto-generated method stub
		return this.clipboard.size() != 0;
	}
}
