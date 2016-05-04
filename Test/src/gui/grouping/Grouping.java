package gui.grouping;

import java.util.LinkedList;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;

import javax.swing.JComponent;
import javax.swing.JFrame;

import controller.interactivepane.InteractiveController;
import model.graph.Module;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class Grouping extends Plugin {
	private InteractiveController controller;
	public static Grouping getInstance(PluginHost host){
		return new Grouping(host);
		
	}
	
	public Grouping(PluginHost host) {
		super(host);
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JFrame getFullView() {
		JFrame frame = new JFrame();
		frame.add(this.controller.getPane());
		return frame;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayName() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxInputs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxOutputs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinInputs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinOutputs() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notify(HostEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		controller = new InteractiveController();
	}
	
	public void group(InteractiveController info, LinkedList<InteractiveModule> groupThis){
		
		//TODO: mind the translation: the modules in the new pane need to be translated relative to their old position! best: 
		// where left-click for grouping was detected is the new (0|0)
	}
	
	public void ungroup(InteractiveController info){
		
	}
	
	public void init(){
		
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reOpen() {
		// TODO Auto-generated method stub

	}

}
