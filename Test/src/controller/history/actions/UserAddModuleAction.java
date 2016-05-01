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
	private transient Plugin plugin;
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
		interactiveModule = new InteractiveModule(controller.getPane(), controller.getLastMouseGridLocation(), module);
		interactiveModule.addMouseListener(controller.getPopupMenuListener());
		interactiveModule.addMouseMotionListener(controller.getPopupMenuListener());
		interactiveModule.addMouseWheelListener(controller.getPopupMenuListener());
		interactiveModule.addMouseListener(controller.getModuleListener());
		interactiveModule.addMouseMotionListener(controller.getModuleListener());
		interactiveModule.addMouseWheelListener(controller.getModuleListener());
		interactiveModule.inputPopout(true);
		
	}
	@Override
	public void undo() {
		//TODO: Opening and closing Plugins necessary for System Ressources!
		// disconnecting Connections not necessary, cause a newly added module has none!
		controller.getPane().remove(interactiveModule);

	}

	@Override
	public void execute() {
		controller.getPane().add(interactiveModule);

	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
		out.writeObject(this.plugin);
	}
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		in.defaultReadObject();
		// TODO: save plugin reading
		this.plugin = (Plugin) in.readObject();
	}

}
