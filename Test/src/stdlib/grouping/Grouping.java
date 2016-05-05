package stdlib.grouping;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;

import javax.swing.JComponent;
import javax.swing.JFrame;

import controller.interactivepane.InteractiveController;
import defaults.DefaultView;
import defaults.MidiIO;
import model.graph.Module;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class Grouping extends Plugin {
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 1;
	private static final int MINOUTPUTS = 1;
	private static final String NAME = "GROUP";
	private String msg = "group";
	private DefaultView view;
	private HashMap<MidiIO,MidiIO> ioMap;
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
		return this.NAME;
	}

	@Override
	public JComponent getMinimizedView() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public Component getFullView() {
		return controller.getPane();
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
		return MAXINPUTS;
	}

	@Override
	public int getMaxOutputs() {
		// TODO Auto-generated method stub
		return MAXOUTPUTS;
	}

	@Override
	public int getMinInputs() {
		// TODO Auto-generated method stub
		return MININPUTS;
	}

	@Override
	public int getMinOutputs() {
		// TODO Auto-generated method stub
		return MINOUTPUTS;
	}

	@Override
	public void notify(HostEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() {
		controller = new InteractiveController();
		view = new DefaultView(this.msg);
		
	}
	
	public void group(InteractiveController info, LinkedList<InteractiveComponent> groupThis){
		this.controller.setUserActionManager(info.getActionManager());
		for(InteractiveComponent c:groupThis){
			if(c instanceof Groupable){
				c.setController(this.controller);
				info.getPane().clearSelection();
				info.getPane().remove(c);
				this.controller.getPane().add(c);
			}
		}
		this.controller.getPane().updateView();
		//TODO: mind the translation: the modules in the new pane need to be translated relative to their old position! best: 
		// where left-click for grouping was detected is the new (0|0)
	}
	
	public void ungroup(InteractiveController info, LinkedList<InteractiveComponent> groupThis){
		this.controller.getPane().clearSelection();
		for(Component c:this.controller.getPane().getComponents()){
			if(groupThis != null){
				if(c instanceof InteractiveComponent && c instanceof Groupable && groupThis.contains(c)){
					
				((InteractiveComponent) c).setController(info);
				this.controller.getPane().remove(c);
				if(!info.getPane().isAncestorOf(c))
					info.getPane().add(c);
				}
			}
			
		}
		
		info.getPane().updateView();
	}
	
	public InteractiveController getController(){
		return this.controller;
	}
	
	public void init(){
		
	}
	
	@Override
	public boolean close() {
		return true;

	}

	@Override
	public boolean reOpen() {
		return true;

	}

}
