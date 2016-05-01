package controller.history;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.AbstractAction;

import controller.interactivepane.InteractiveController;

public class UserActionManager implements Serializable{
	private Stack<UserAction> undo;
	private Stack<UserAction> redo;
	private InteractiveController controller;
	
	public UserActionManager(){
		this.undo = new Stack<UserAction>();
		this.redo = new Stack<UserAction>();
	}
	
	public void setController(InteractiveController controller){
		this.controller = controller;
	}
	
	public InteractiveController getController(){
		return this.controller;
	}
	
	public void addEvent(UserAction e){
		this.redo.clear();
		this.undo.push(e);
	}
	
	/**
	 * Calls the next UndoRedoEvents undo method.
	 * @return true if undo was executed, false if there are no undos
	 */
	public boolean undo(){
		try{
		UserAction e = this.undo.pop();
		e.undo();
		this.redo.push(e);
		} catch (EmptyStackException e){
			return false;
		}
		return true;
	}
	
	public boolean redo(){
		try{
			UserAction e = this.redo.pop();
			e.execute();
			this.undo.push(e);
		} catch (EmptyStackException e){
			return false;
		}
		return true;
	}


}
