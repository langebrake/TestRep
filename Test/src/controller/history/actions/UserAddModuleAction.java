package controller.history.actions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.sound.midi.MidiUnavailableException;

import model.graph.Module;
import model.pluginmanager.Loadable;
import gui.interactivepane.InteractiveModule;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.exceptions.PluginMaxOutputsExceededException;
import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;
import engine.Engine;

public class UserAddModuleAction extends UserAction {
	private InteractiveModule interactiveModule;
	
	public UserAddModuleAction(InteractiveController sourceController, Loadable p){
		super(sourceController);
		Module module = null;
		Plugin plugin = null;
		try {
			module = new Module();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			plugin = p.getInstance(module);
			module.setPlugin(plugin);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PluginMaxOutputsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		interactiveModule = new InteractiveModule(controller.getLastMouseGridLocation(), module, controller);
		this.firsttime = true;
	}
	
	public UserAddModuleAction(InteractiveController sourceController, InteractiveModule module) {
		super(sourceController);
		interactiveModule = module;
	}
	
	@Override
	public void undo() {

		interactiveModule.close();
		interactiveModule.getController().remove(interactiveModule);


	}

	private boolean firsttime;
	@Override
	public void execute() {

		if(!firsttime){
			interactiveModule.reopen();
			
		} else {
			firsttime = false;
		}
		interactiveModule.reopen();
		interactiveModule.getController().add(interactiveModule);

	}
	
}
