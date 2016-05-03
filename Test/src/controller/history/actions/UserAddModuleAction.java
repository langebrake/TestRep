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
import controller.history.UserAction;
import controller.history.UserActionManager;
import engine.Engine;

public class UserAddModuleAction extends UserAction {
	private Plugin plugin;
	private InteractiveModule interactiveModule;
	private Module module;
	public UserAddModuleAction(UserActionManager manager,Loadable p){
		super(manager);
		module = null;
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
		}
		interactiveModule = new InteractiveModule(controller.getPane(), controller.getLastMouseGridLocation(), module,manager.getController());
		interactiveModule.addListeners(controller.getModuleListener(), controller.getPopupMenuListener(),controller.getCableCreationListener());

		
	}
	@Override
	public void undo() {
		//TODO: Opening and closing Plugins necessary for System Ressources!
		// disconnecting Connections not necessary, cause a newly added module has none!
		if(interactiveModule.close()){
			controller.getPane().remove(interactiveModule);
		}

	}

	@Override
	public void execute() {
		if(interactiveModule.reopen()){
			controller.getPane().add(interactiveModule);
		}

	}
	
}
