package controller.history.actions;

import gui.grouping.Grouping;
import gui.interactivepane.InteractiveModule;

import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;

import pluginhost.exceptions.PluginMaxOutputsExceededException;
import model.graph.Module;
import controller.history.UserAction;
import controller.history.UserActionManager;

public class UserAddGroupAction extends UserAction {
	LinkedList<InteractiveModule> groupThis;
	InteractiveModule groupModule;
	Grouping grouping;

	public UserAddGroupAction(UserActionManager m, LinkedList<InteractiveModule> groupThis) {
		super(m);
		this.groupThis = groupThis;
		Module module = null;
		try {
			module = new Module();
			grouping = new Grouping(module);
			module.setPlugin(grouping);
		} catch (MidiUnavailableException e) {
			// TODO unexpected Error Handling + log
			e.printStackTrace();
		} catch (PluginMaxOutputsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.groupModule = new InteractiveModule(controller.getPane(), controller.getLastMousePaneLocation(), module, controller);
	}

	@Override
	public void undo() {
		if(groupModule.close()){
			grouping.ungroup(controller);
			controller.getPane().remove(groupModule);
		}

	}

	@Override
	public void execute() {
		if(groupModule.reopen()){
			grouping.group(controller, groupThis);
			controller.getPane().add(groupModule);
		}

	}

}
