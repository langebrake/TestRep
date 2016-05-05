package controller.history.actions;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;

import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;

import pluginhost.exceptions.PluginMaxOutputsExceededException;
import stdlib.grouping.Grouping;
import model.graph.Module;
import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;

public class UserAddGroupAction extends UserAction {
	LinkedList<InteractiveComponent> groupThis;
	InteractiveModule groupModule;
	Grouping grouping;

	public UserAddGroupAction(InteractiveController sourceController, LinkedList<InteractiveComponent> linkedList) {
		super(sourceController);
		this.groupThis = linkedList;
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
		this.groupModule = new InteractiveModule(controller.getLastMouseGridLocation(), module, controller);
	}

	@Override
	public void undo() {
		if(groupModule.close()){
			grouping.ungroup(controller,groupThis);
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
