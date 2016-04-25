package controller;

import controller.interactivepane.InteractiveController;
import controller.pluginmanager.PluginManager;

public class Controller {
	
	private PluginManager manager;
	
	public Controller(){
		this.manager = new PluginManager(this);
	}
	
	public InteractiveController getActiveInteractiveController(){
		return null;
	}
}
