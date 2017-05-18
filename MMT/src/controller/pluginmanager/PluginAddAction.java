package controller.pluginmanager;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.AbstractAction;

import controller.history.actions.UserAddModuleAction;
import controller.interactivepane.InteractiveController;
import engine.Engine;
import gui.interactivepane.InteractiveModule;
import model.graph.Module;
import model.pluginmanager.Loadable;

public class PluginAddAction extends AbstractAction {
	private InteractiveController controller;
	private Loadable plugin;

	public PluginAddAction(InteractiveController controller, Loadable plugin) {
		super(plugin.getName());
		
		putValue(SHORT_DESCRIPTION, plugin.getDescription());
		this.controller = controller;
		this.plugin = plugin;
	}

	public void actionPerformed(ActionEvent arg0) {

		// TODO: Threading is not necessary, will prevent Loaduptimes to interrupt interface.
		// Implement more threadingoptions on other plugininteractions
		Thread t = new Thread() {
			public void run() {
				UserAddModuleAction a = new UserAddModuleAction(controller,
						plugin);
				controller.executeAction(a);

			}
		};
		t.start();

	}

}
