package controller.maincontrol;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import model.pluginmanager.PluginManager;
import controller.Controller;

public class RescanPlugins extends AbstractAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -225547741616388329L;
	Controller controller;
	public RescanPlugins(Controller c){
		super("Rescan Plugins");
		super.putValue(SHORT_DESCRIPTION, "searches for new plugins");
		super.putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		this.controller = c;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			PluginManager.loadPlugins();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
