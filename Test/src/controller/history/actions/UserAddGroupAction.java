package controller.history.actions;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

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
	Vector originVector;
	String name;

	public UserAddGroupAction(InteractiveController sourceController, LinkedList<InteractiveComponent> linkedList) {
		super(sourceController);
		this.groupThis = linkedList;
		this.originVector = controller.getLastMousePaneLocation();
		this.generateGroup();
	}

	private void generateGroup(){
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
		this.groupModule = new InteractiveModule(this.originVector, module, controller);
		((Grouping) module.getPlugin()).getController().setGrouping(grouping);
		if(this.name!=null){
			this.groupModule.setName(this.name);
		}
	}
	@Override
	public void undo() {
		if(groupModule.close()){
			grouping.ungroup(groupModule);
			controller.getPane().remove(groupModule);
//			this.originVector = groupModule.getOriginLocation();
//			this.name = groupModule.getName();
		}

	}

	@Override
	public void execute() {
//			generateGroup();
		if(groupModule.reopen()){
			grouping.group(groupModule, groupThis);
			controller.getPane().add(groupModule);
		}

	}

}
