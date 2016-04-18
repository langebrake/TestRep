package controller.history;

import java.util.EmptyStackException;
import java.util.Stack;

public class UserActionManager {
	private Stack<UserAction> undo;
	private Stack<UserAction> redo;
	
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
