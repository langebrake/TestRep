package controller;

import model.pluginmanager.PluginManager;
import controller.interactivepane.InteractiveController;

public class Controller {
	
	private PluginManager manager;
	
	public Controller(){
		this.manager = new PluginManager(this);
	}
	
	public InteractiveController getActiveInteractiveController(){
		return null;
	}
}
