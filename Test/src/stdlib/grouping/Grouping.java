package stdlib.grouping;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComponent;
import javax.swing.JFrame;

import controller.history.actions.UserAddModuleAction;
import controller.interactivepane.InteractiveController;
import defaults.DefaultView;
import defaults.MidiIO;
import model.graph.Module;
import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;
import pluginhost.exceptions.PluginMaxOutputsExceededException;

public class Grouping extends Plugin {
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "GROUP";
	private String msg = "group";
	private DefaultView view;
	private HashMap<MidiIO,MidiIO> ioMap;
	private InteractiveController controller;
	private InteractiveModule groupInput,groupOutput;
	
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
		
		try {
			Module m = new Module();
			Plugin p = new GroupInput(m);
			m.setPlugin(p);
			groupInput = new InteractiveModule(controller.getLastMouseGridLocation(), m, controller);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PluginMaxOutputsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Module m = new Module();
			Plugin p = new GroupOutput(m);
			m.setPlugin(p);
			groupOutput = new InteractiveModule(controller.getLastMouseGridLocation(), m, controller);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PluginMaxOutputsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.controller.getPane().add(groupInput);
		this.controller.getPane().add(groupOutput);
		
		
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
	
	
	public void addGroupInput(){

	}
	
	public void addGroupOutput(){
		
	}
	
	
	private class GroupInput extends Plugin {
		
		public GroupInput(PluginHost host) {
			super(host);
			// TODO Auto-generated constructor stub
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
		public Component getFullView() {
			// TODO Auto-generated method stub
			return null;
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean close() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean reOpen() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
	private class GroupOutput extends Plugin{

		public GroupOutput(PluginHost host) {
			super(host);
			// TODO Auto-generated constructor stub
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
		public Component getFullView() {
			// TODO Auto-generated method stub
			return null;
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean close() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean reOpen() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
