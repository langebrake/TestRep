package controller.history;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.AbstractAction;

import controller.interactivepane.InteractiveController;
import controller.maincontrol.Project;

public class UserActionManager implements Serializable {
	private Stack<UserAction> undo;
	private Stack<UserAction> redo;
	private Project project;

	public UserActionManager() {
		this.undo = new Stack<UserAction>();
		this.redo = new Stack<UserAction>();
	}

	public void addEvent(UserAction e) {
		this.redo.clear();
		this.undo.push(e);
	}

	/**
	 * Calls the next UndoRedoEvents undo method.
	 * 
	 * @return true if undo was executed, false if there are no undos
	 */
	public boolean undo() {
		try {
			UserAction e = this.undo.pop();
			e.undo();
			this.redo.push(e);
		} catch (EmptyStackException e) {
			return false;
		}
		this.project.updateTree();
		return true;
	}

	public boolean redo() {
		try {
			UserAction e = this.redo.pop();
			e.execute();
			this.undo.push(e);
		} catch (EmptyStackException e) {
			return false;
		}
		this.project.updateTree();
		return true;
	}

	public void execute() {
		try {
			this.undo.peek().execute();
		} catch (EmptyStackException e) {
			// TODO: nothing to do.
		}
		this.project.updateTree();
	}

	public void setProject(Project project) {
		this.project = project;

	}

}
