package stdlib.grouping;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import gui.interactivepane.CablePoint;
import gui.interactivepane.CablePointHost;
import gui.interactivepane.CablePointType;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.InteractivePane;
import gui.interactivepane.Vector;

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
import pluginhost.events.NewInputEvent;
import pluginhost.events.NewOutputEvent;
import pluginhost.exceptions.PluginInputNotFoundException;
import pluginhost.exceptions.PluginMaxOutputsExceededException;
import pluginhost.exceptions.PluginMinInputsExceededException;
import pluginhost.exceptions.PluginMinOutputsExceededException;
import pluginhost.exceptions.PluginOutputNotFoundException;

public class Grouping extends Plugin {
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "GROUP";
	private transient String msg = "group";
	private transient DefaultView view;
	private InteractiveController controller;
	private transient InteractiveModule groupOutput;
	private transient InteractiveModule groupInput;
	private transient boolean block;
	
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
		if(!block){
			if(e instanceof NewInputEvent){
				((GroupInput)groupInput.getModule().getPlugin()).block = true;
				MidiIO externalInput = ((NewInputEvent) e).getNewInput();
				MidiIO internalOutput = this.groupInput.getModule().getPlugin().getPluginHost().newOutput();
				//TODO: use listener to make persistence efficient and safe
				externalInput.setOutput(internalOutput);
				internalOutput.setInput(externalInput);
				((GroupInput)groupInput.getModule().getPlugin()).block = false;
			} else if (e instanceof NewOutputEvent){
				((GroupOutput)groupOutput.getModule().getPlugin()).block = true;
				MidiIO externalOutput = ((NewOutputEvent) e).getNewOutput();
				MidiIO internalInput = this.groupOutput.getModule().getPlugin().getPluginHost().newInput();
				internalInput.setOutput(externalOutput);
				externalOutput.setInput(internalInput);
				((GroupOutput)groupOutput.getModule().getPlugin()).block = false;
			}
		}

	}

	@Override
	public void load() {
		controller = new InteractiveController();
		view = new DefaultView(this.msg);
		
		try {
			Module m = new Module();
			Plugin p = new GroupInput(m,this);
			m.setPlugin(p);
			groupInput = new InteractiveModule(new Vector(0,0), m, controller);
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
			Plugin p = new GroupOutput(m,this);
			m.setPlugin(p);
			groupOutput = new InteractiveModule(new Vector(0,500), m, controller);
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
		
		this.controller.add(groupInput);
		this.controller.add(groupOutput);
		
	}
	
	public void group(InteractiveModule groupModule, LinkedList<InteractiveComponent> groupThis){

		this.controller.setUserActionManager(groupModule.getController().getActionManager());
		InteractiveController oldController = groupModule.getController();
		InteractiveController newController = this.controller;
		InteractivePane oldPane = groupModule.getController().getPane();
		InteractivePane newPane = this.controller.getPane();
		Vector resetTranslation = groupModule.getOriginLocation().scaleVector(-1);
		oldPane.clearSelection();
		int inputCounter = 0, outputCounter = 0;
		for(InteractiveComponent c:groupThis){
			if(c instanceof Groupable){
				c.setController(this.controller);
				c.translateOriginLocation(resetTranslation);
				if(c instanceof CablePointHost){
					for(CablePoint p:((CablePointHost) c).getCablePoints()){
						if(p.isConnected()){
							InteractiveCable cable = p.getCable();
							if(cable.getController() != this.controller){
								oldPane.remove(cable);
								cable.setController(controller);
								CablePoint otherEnd = (cable.getSource() == p) ? cable.getDestination():cable.getSource();
								if(!groupThis.contains(otherEnd.getHost())){
									//TODO: manage external Inputs
									if(otherEnd.getType() == CablePointType.INPUT){
										//external output
										//TODO: Error Management, if null
										CablePoint newExternalEndpoint = groupModule.getCablePoint(CablePointType.OUTPUT, outputCounter++);
										if(newExternalEndpoint == null){
											this.getPluginHost().newOutput();
											//generating new Cable on original Pane, connect with groupmodule output
											newExternalEndpoint = groupModule.getCablePoint(CablePointType.OUTPUT, groupModule.getCablePoints(CablePointType.OUTPUT).size() - 1);
										}
										InteractiveCable newExternalCable = new InteractiveCable(newExternalEndpoint,otherEnd,groupModule.getController());
										oldPane.add(newExternalCable);
										newExternalEndpoint.setCable(newExternalCable);
										otherEnd.setCable(newExternalCable);
										
										//internal Cable gets new endpoint
										cable.setSource(p);
										CablePoint newInternalEndpoint = this.groupOutput.getCablePoint(CablePointType.INPUT, outputCounter-1);
										cable.setDestination(newInternalEndpoint);
										p.setCable(cable);
										newInternalEndpoint.setCable(cable);
										
										
									} else if(otherEnd.getType() == CablePointType.OUTPUT){
										//external input
										//TODO: Error Management, if null
										
										CablePoint newExternalEndpoint = groupModule.getCablePoint(CablePointType.INPUT, inputCounter++);
										if(newExternalEndpoint == null){
											this.getPluginHost().newInput();
											//generating new Cable on original Pane, connect with groupmodule output
											newExternalEndpoint = groupModule.getCablePoint(CablePointType.INPUT, groupModule.getCablePoints(CablePointType.INPUT).size() - 1);
										}
										InteractiveCable newExternalCable = new InteractiveCable(newExternalEndpoint,otherEnd,groupModule.getController());
										oldPane.add(newExternalCable);
										newExternalEndpoint.setCable(newExternalCable);
										otherEnd.setCable(newExternalCable);
										
										//internal Cable gets new endpoint
										cable.setSource(p);
										CablePoint newInternalEndpoint = this.groupInput.getCablePoint(CablePointType.OUTPUT, inputCounter-1);
										cable.setDestination(newInternalEndpoint);
										p.setCable(cable);
										newInternalEndpoint.setCable(cable);
										
									}
								}
								newPane.add(cable);
							}
							
							
						}
						
					}
					
					//handle external Connections
					
				}
				//TODO: ClassCastExceptions!!
				oldController.remove((InteractiveModule) c);
				if(!newPane.isAncestorOf(c))
					newController.add((InteractiveModule) c);
			}
		}
		newPane.updateView();
		//TODO: mind the translation: the modules in the new pane need to be translated relative to their old position! best: 
		// where left-click for grouping was detected is the new (0|0)
	}
	
	public void ungroup(InteractiveModule groupModule){
		
		InteractivePane oldPane = this.controller.getPane();
		InteractivePane newPane = groupModule.getController().getPane();
		InteractiveController newController = groupModule.getController();
		InteractiveController oldController = this.controller;
		oldPane.clearSelection();
		Vector restoreTranslation = groupModule.getOriginLocation();
		for(Component comp:this.controller.getPane().getComponents()){
			if(comp instanceof InteractiveComponent && comp instanceof Groupable && ungroupable((InteractiveComponent) comp)){
				InteractiveComponent c = (InteractiveComponent) comp;
				c.setController(groupModule.getController());
				c.translateOriginLocation(restoreTranslation);
				
				if(c instanceof CablePointHost){
					for(CablePoint p:((CablePointHost) c).getCablePoints()){
						if(p.isConnected()){
							InteractiveCable cable = p.getCable();
							if(cable.getController() != groupModule.getController()){
								oldPane.remove(cable);
								cable.setController(groupModule.getController());
								CablePoint otherEnd = (cable.getSource() == p) ? cable.getDestination():cable.getSource();
								
								//TODO: better codemanagement possible, these cases are very similar
								if(otherEnd.getHost() == this.groupOutput){
										//external output
										
										CablePoint externalOutput = groupModule.getCablePoint(CablePointType.OUTPUT, otherEnd.getIndex());
										if(externalOutput.isConnected()){
											// p must be connected to externalOutputs other End
											InteractiveCable externalCable = externalOutput.getCable();
											newPane.remove(externalCable);
											CablePoint externalEndpoint = externalCable.getSource() == externalOutput ? externalCable.getDestination():externalCable.getSource();
											cable.setSource(p);
											cable.setDestination(externalEndpoint);
											p.setCable(cable);
											externalEndpoint.setCable(cable);
											externalOutput.disconnect();
											newPane.add(cable);
										} else {
											// remove unused connection
											p.disconnect();
											otherEnd.disconnect();
										}
										
									} else if(otherEnd.getHost() == this.groupInput){
										//external input
										CablePoint externalInput = groupModule.getCablePoint(CablePointType.INPUT, otherEnd.getIndex());
										if(externalInput.isConnected()){
											// p must be connected to externalOutputs other End
											InteractiveCable externalCable = externalInput.getCable();
											newPane.remove(externalCable);
											CablePoint externalEndpoint = externalCable.getSource() == externalInput ? externalCable.getDestination():externalCable.getSource();
											cable.setSource(p);
											cable.setDestination(externalEndpoint);
											p.setCable(cable);
											externalEndpoint.setCable(cable);
											externalInput.disconnect();
											newPane.add(cable);
										} else {
											// remove unused connection
											p.disconnect();
											otherEnd.disconnect();
										}
									} else {
										newPane.add(cable);
									}
								
								
							}
							
							
						}
						
					}
				}
				
				
				oldController.remove((InteractiveModule) c);
				if(!newPane.isAncestorOf(c))
					newController.add((InteractiveModule) c);
				}
			
			
		}
		
		for(CablePoint p: groupModule.getCablePoints()){
			if(p.isConnected()){
				InteractiveCable tmp = p.getCable();
				tmp.getSource().disconnect();
				tmp.getDestination().disconnect();
				newPane.remove(tmp);
			}
		}
		
		
		groupModule.getController().getPane().updateView();
	}
	
	public LinkedList<InteractiveComponent> getContent(){
		LinkedList<InteractiveComponent> content = new LinkedList<InteractiveComponent>();
		for(Component c:this.controller.getPane().getComponents()){
			if(c instanceof InteractiveComponent && this.ungroupable((InteractiveComponent) c)){
				content.add((InteractiveComponent) c);
			}
		}
		return content;
	}
	
	private boolean ungroupable(InteractiveComponent c){
		return (c!=this.groupInput && c!=this.groupOutput);
	}
	
	public InteractiveController getController(){
		return this.controller;
	}
	
	public void init(){
		
	}
	
	@Override
	public boolean close() {
		return this.controller.close();
	}

	@Override
	public boolean reOpen() {
		
		return this.controller.reOpen();

	}
	
	
	public void addGroupInput(){

	}
	
	public void addGroupOutput(){
		
	}
	
	
	private static class GroupInput extends Plugin {
		private final int MAXINPUTS = 0;
		private final int MAXOUTPUTS = -1;
		private final int MININPUTS = 0;
		private final int MINOUTPUTS = 0;
		private final String NAME = "Group Input";
		private String msg = "groupinput";
		private DefaultView view;
		private transient boolean block;
		private transient Grouping grouping;
	
		public GroupInput(PluginHost host, Grouping grouping) {
			super(host);
			this.grouping = grouping;
		}
		

		@Override
		public String getPluginName() {
		
			return NAME;
		}

		@Override
		public JComponent getMinimizedView() {
			return view;
		}

		@Override
		public Component getFullView() {
			
			return view;
		}

		@Override
		public String getDisplayName() {
		
			return NAME;
		}

		@Override
		public void setDisplayName() {
			
			
		}

		@Override
		public int getMaxInputs() {
			return MAXINPUTS;
		}

		@Override
		public int getMaxOutputs() {
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
			if(!block){
				if(e instanceof NewInputEvent){
					//TODO: unexpected Error
				} else if (e instanceof NewOutputEvent){
					grouping.block = true;
					MidiIO internalOutput = ((NewOutputEvent) e).getNewOutput();
					MidiIO externalInput = grouping.getPluginHost().newInput();
					internalOutput.setInput(externalInput);
					externalInput.setOutput(internalOutput);
					grouping.block=false;
				}
			}
			
		}

		@Override
		public void load() {
			this.view = new DefaultView(this.msg);
			
		}

		@Override
		public boolean close() {
			return true;
		}

		@Override
		public boolean reOpen() {
			return true;
		}
		private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
			in.defaultReadObject();
		}
	}
	
	private static class GroupOutput extends Plugin {
		private final int MAXINPUTS = -1;
		private final int MAXOUTPUTS = 0;
		private final int MININPUTS = 0;
		private final int MINOUTPUTS = 0;
		private final String NAME = "Group Output";
		private String msg = "groupoutput";
		private DefaultView view;
		private transient boolean block;
		private transient Grouping grouping;
		
		public GroupOutput(PluginHost host, Grouping grouping) {
			super(host);
			this.grouping = grouping;
		}
		

		@Override
		public String getPluginName() {
		
			return NAME;
		}

		@Override
		public JComponent getMinimizedView() {
			return view;
		}

		@Override
		public Component getFullView() {
			
			return view;
		}

		@Override
		public String getDisplayName() {
		
			return NAME;
		}

		@Override
		public void setDisplayName() {
			
			
		}

		@Override
		public int getMaxInputs() {
			return MAXINPUTS;
		}

		@Override
		public int getMaxOutputs() {
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
			if(!block){
				if(e instanceof NewInputEvent){
					grouping.block = true;
					MidiIO internalInput = ((NewInputEvent) e).getNewInput();
					MidiIO externalOutput = grouping.getPluginHost().newOutput();
					internalInput.setOutput(externalOutput);
					externalOutput.setInput(internalInput);
					grouping.block=false;
				} else if (e instanceof NewOutputEvent){
					//TODO: unexpected Error
				}
			}
			
		}

		@Override
		public void load() {
			this.view = new DefaultView(this.msg);
			
		}

		@Override
		public boolean close() {
	
			return true;
		}

		@Override
		public boolean reOpen() {
			
			return true;
		}
		private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
			in.defaultReadObject();
		}
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		in.defaultReadObject();
		for(Component c: this.controller.getPane().getComponents()){
			
			if(((InteractiveModule) c).getModule().getPlugin() instanceof GroupInput){
				this.groupInput = (InteractiveModule) c;
				((GroupInput)this.groupInput.getModule().getPlugin()).grouping = this;
			} 
			if (((InteractiveModule) c).getModule().getPlugin() instanceof GroupOutput){
				this.groupOutput = (InteractiveModule) c;
				((GroupOutput)this.groupOutput.getModule().getPlugin()).grouping = this;
			}
		}
		//just reads the MIDIGraph, needs to be populated TODO
	}

}
