package controller.history.actions;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

import java.lang.reflect.Method;
import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;

import pluginhost.PluginHost;
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
			module.setPlugin(grouping,Grouping.class);
		} catch (MidiUnavailableException e) {
			// TODO unexpected Error Handling + log
			e.printStackTrace();
		} catch (PluginMaxOutputsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.groupModule = new InteractiveModule(this.originVector, module, controller);
		
		if(this.name!=null){
			this.groupModule.setName(this.name);
		}
	}
	@Override
	public void undo() {
		
			grouping.ungroup(groupModule);
			groupModule.close();
			controller.remove(groupModule);
//			this.originVector = groupModule.getOriginLocation();
//			this.name = groupModule.getName();
		

	}

	private boolean firsttime = true;
	@Override
	public void execute() {
//			generateGroup();
		
			grouping.group(groupModule, groupThis);
			if(firsttime){
				firsttime = false;
			}else {
				groupModule.reopen();
			}
			controller.add(groupModule);
		

	}

}
