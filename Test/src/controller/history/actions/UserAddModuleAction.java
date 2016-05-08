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
		//TODO: Opening and closing Plugins necessary for System Ressources!
		// disconnecting Connections not necessary, cause a newly added module has none!
//		if(interactiveModule.close()){
//			interactiveModule.getController().getPane().remove(interactiveModule);
//		}
		interactiveModule.close();
		interactiveModule.getController().getPane().remove(interactiveModule);


	}

	private boolean firsttime;
	@Override
	public void execute() {
//		if(interactiveModule.reopen()){
//			interactiveModule.getController().getPane().add(interactiveModule);
//		}
		if(!firsttime){
			interactiveModule.reopen();
			System.out.println("REOPEN");
			
		} else {
			firsttime = false;
		}
		interactiveModule.getController().getPane().add(interactiveModule);

	}
	
}
