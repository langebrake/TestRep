package controller.history.actions;

import java.lang.reflect.InvocationTargetException;

import javax.sound.midi.MidiUnavailableException;

import model.graph.Module;
import model.pluginmanager.Loadable;
import gui.interactivepane.InteractiveModule;
import plugin.Plugin;

import pluginhost.exceptions.PluginMaxOutputsExceededException;
import controller.history.UserAction;
import controller.interactivepane.InteractiveController;

public class UserAddModuleAction extends UserAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InteractiveModule interactiveModule;

	public UserAddModuleAction(InteractiveController sourceController,
			Loadable p) {
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
			module.setPlugin(plugin, p.getPluginClass());
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
		interactiveModule = new InteractiveModule(
				controller.getLastMouseGridLocation(), module, controller);
		this.firsttime = true;
	}

	public UserAddModuleAction(InteractiveController sourceController,
			InteractiveModule module) {
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

		if (!firsttime) {
			interactiveModule.reopen();

		} else {
			firsttime = false;
		}
		interactiveModule.reopen();
		interactiveModule.getController().add(interactiveModule);

	}

}
