package controller.history.actions;

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
			module = new Module(Engine.load());
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
		interactiveModule = new InteractiveModule(controller.getPane(), controller.getLastMouseGridLocation(), module);
		interactiveModule.addMouseListener(controller.getPopupMenuListener());
		interactiveModule.addMouseMotionListener(controller.getPopupMenuListener());
		interactiveModule.addMouseWheelListener(controller.getPopupMenuListener());
		interactiveModule.addMouseListener(controller.getModuleListener());
		interactiveModule.addMouseMotionListener(controller.getModuleListener());
		interactiveModule.addMouseWheelListener(controller.getModuleListener());
		
	}
	@Override
	public void undo() {
		//TODO: Opening and closing Plugins necessary for System Ressources!
		controller.getPane().remove(interactiveModule);

	}

	@Override
	public void execute() {
		controller.getPane().add(interactiveModule);

	}

}
