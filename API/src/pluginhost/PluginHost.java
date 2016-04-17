package pluginhost;

import java.util.LinkedList;

import midiengine.MidiEngine;
import defaults.MidiIO;
import plugin.Plugin;
import plugin.events.PluginEvent;
import pluginhost.exceptions.*;
import pluginhost.events.*;


public abstract class PluginHost {
	
	private LinkedList<MidiIO> inputs,outputs;
	private Plugin plugin;
	private MidiEngine engine;
	
	public PluginHost(Plugin p, MidiEngine e){
		this.plugin = p;
		this.inputs = new LinkedList<MidiIO>();
		this.outputs = new LinkedList<MidiIO>();
		this.engine = e;
		p.load();
	}
	
	
	public Plugin getPlugin(){
		return this.plugin;
	}
	
	public void setPlugin(Plugin p){
		p.close();
		this.plugin = p;
		p.load();
	}
	/**
	 * Creates a new input for this host and connects it to the given source.
	 * Than notifies the plugin about this change
	 * @param toConnect
	 * @throws PluginMaxInputsExceededException
	 */
	public void connectNewInput(MidiIO toConnect) throws PluginMaxInputsExceededException{
		this.connectNewInput(toConnect, this.getInputCount());
	}
	
	/**
	 * Creates a new input for this host at the specified index id and connects it to the given source
	 * Notifies the plugin about this change
	 * @param toConnect
	 * @param id
	 * @throws PluginMaxInputsExceededException
	 */
	public void connectNewInput(MidiIO toConnect, int id) throws PluginMaxInputsExceededException{
		if(plugin.getMaxInputs() >= this.getInputCount()){
			throw new PluginMaxInputsExceededException();
		}else if (this.getInputCount() < id) {
			throw new IllegalArgumentException("Input connection error: illegal id");
		}else {
		MidiIO newInput = new MidiIO(this);
		newInput.setInput(toConnect);
		this.inputs.add(id,newInput);
		//TODO: Host Event Implementation of InputEvent
		this.plugin.notify(new HostEvent());
		}
	}
	
	
	public void connectNewInputs(LinkedList<MidiIO> toConnect){
		this.connectNewInputs(toConnect,this.getInputCount());
	}
	
	public void connectNewInputs(LinkedList<MidiIO> toConnect, int id ){
		//TODO: Implementation!!
	}
	/**
	 * Connects a given Input to a source.
	 * Notifies the plugin about the change.
	 * @param toConnect
	 * @param givenInput
	 * @throws PluginException
	 */
	public void connectWithInput(MidiIO toConnect, MidiIO givenInput) throws PluginException{
		int id = this.inputs.indexOf(givenInput);
		this.connectWithInput(toConnect, id);
	}
	
	/**
	 * connects the input at the given index id to the specified input source.
	 * @param toConnect
	 * @param givenInputID
	 * @throws PluginException
	 */
	public void connectWithInput(MidiIO source, int givenInputID) throws PluginException{
		if (this.inputs.size() - 1 < givenInputID || givenInputID<0){
			throw new PluginInputNotFoundException();
		} else if (source.hasOutput()){
			throw new PluginException("Input Source connected to other device!");
		} else {
			this.inputs.get(givenInputID).setInput(source);
			//TODO: specific Host event implementation!
			this.plugin.notify(new HostEvent());
		}
	}
	
	/**
	 * Connects the sources to the given inputs in order of appearence in the list
	 * @param sources
	 * @param givenInputs
	 */
	public void connectWithInputs(LinkedList<MidiIO> sources, LinkedList<MidiIO> givenInputs){
		//TODO: implement
	}
	
/**
 * 
 * @param sources
 * @param startID
 * @param enableNewInputs
 */
	public void connectWithInputs(LinkedList<MidiIO> sources, int startID, boolean enableNewInputs){
		//TODO: implement
	}
	
	/**
	 * creates a new input and appends it to the input list.
	 * notifies the plugin aboutt the change.
	 * @return new input
	 * @throws PluginMaxInputsExceededException
	 */
	public MidiIO newInput() throws PluginMaxInputsExceededException{
		return this.newInput(this.getInputCount());
	}
	
	/**
	 * creates a new input at the specified index id.
	 * notifies the plugin about the change
	 * @param id
	 * @return
	 * @throws PluginMaxInputsExceededException
	 */
	public MidiIO newInput(int id) throws PluginMaxInputsExceededException{
		if(this.plugin.getMaxInputs() <= this.getInputCount()){
			throw new PluginMaxInputsExceededException();
		} else{
			MidiIO tmp = new MidiIO(this);
			this.inputs.add(id, tmp);
		//TODO: HostEvent implementation for this event!
			this.plugin.notify(new HostEvent());
			return tmp;
		}
	}
	
	public void newInputs(int count){
		this.newInputs(count, this.getInputCount());
	}
	
	public void newInputs(int count, int startID){
		
	}
	
	/**
	 * disconnects the given input
	 * @param input
	 * @throws PluginInputNotFoundException 
	 */
	public void disconnectInput(MidiIO input) throws PluginInputNotFoundException{
		this.disconnectInput(this.inputs.indexOf(input));
	}
	
	/**
	 * disconnects the input from the given index id
	 * @param id
	 * @throws PluginInputNotFoundException 
	 */
	public void disconnectInput(int id) throws PluginInputNotFoundException{
		if(id<0){
			throw new PluginInputNotFoundException();
		}else if(id>=this.getInputCount()){
			throw new IllegalArgumentException("Input index non-existent");
		} else {
			this.inputs.get(id).disconnectInput();
		}
	}
	
	public void disconnectInputs(LinkedList<MidiIO> inputs){
		//TODO: implement!
	}
	
	public void disconnectInputs(int count, int startID){
		//TODO: implement!
	}
	
	/**
	 * disconnects all inputs
	 * @throws PluginInputNotFoundException
	 */
	public void disconnectAllInputs() throws PluginInputNotFoundException{
		for(int i = 0;i<this.getInputCount();i++){
			this.disconnectInput(i);
		}
	}
		
	/**
	 * disconnects, than removes the input
	 * @param input
	 * @throws PluginInputNotFoundException
	 */
	public void removeInput(MidiIO input) throws PluginInputNotFoundException{
		this.removeInput(this.inputs.indexOf(input));
	}
	
	/**
	 * disconnects, than removes the input with give index id
	 * @param inputID
	 * @throws PluginInputNotFoundException
	 */
	public void removeInput(int inputID) throws PluginInputNotFoundException{
		if(inputID<0){
			throw new PluginInputNotFoundException();
		}else if(inputID>=this.getInputCount()){
			throw new IllegalArgumentException("Input index non-existent");
		} else {
			this.inputs.get(inputID).disconnectInput();
			//TODO: implement appropriate Event
			this.plugin.notify(new HostEvent());
			this.inputs.remove(inputID);
		}
	}
	
	public void removeInputs(LinkedList<MidiIO> inputs){
		//TODO: implement
	}
	
	public void removeInputs(int count, int startID){
		//TODO: implement
	}
	/**
	 * removes all inputs
	 * @throws PluginInputNotFoundException 
	 */
	
	public void removeAllInputs() throws PluginInputNotFoundException{
		//TODO: implement good Host Event
		this.plugin.notify(new HostEvent());
		this.disconnectAllInputs();
		this.inputs = new LinkedList<MidiIO>();
	}
	
	/**
	 * Returns the input count
	 * @return
	 */
	public int getInputCount(){
		return this.inputs.size();
	}
	
	/**
	 * Creates a new output for this host and connects it to the given destination.
	 * Than notifies the plugin about this change
	 * @param toConnect
	 * @throws PluginMaxOutputsExceededException
	 */
	public void connectNewOutput(MidiIO toConnect) throws PluginMaxOutputsExceededException{
		this.connectNewOutput(toConnect, this.getOutputCount());
	}
	
	/**
	 * Creates a new output for this host at the specified index id and connects it to the given destination
	 * Notifies the plugin about this change
	 * @param toConnect
	 * @param id
	 * @throws PluginMaxOutputsExceededException
	 */
	public void connectNewOutput(MidiIO toConnect, int id) throws PluginMaxOutputsExceededException{
		if(plugin.getMaxOutputs() >= this.getOutputCount()){
			throw new PluginMaxOutputsExceededException();
		}else if (this.getOutputCount() < id) {
			throw new IllegalArgumentException("Output connection error: illegal id");
		}else {
		MidiIO newOutput = new MidiIO(this);
		newOutput.setOutput(toConnect);
		this.outputs.add(id,newOutput);
		//TODO: Host Event Implementation of OutputEvent
		this.plugin.notify(new HostEvent());
		}
	}
	
	
	public void connectNewOutputs(LinkedList<MidiIO> toConnect){
		this.connectNewOutputs(toConnect,this.getOutputCount());
	}
	
	public void connectNewOutputs(LinkedList<MidiIO> toConnect, int id ){
		//TODO: Implementation!!
	}
	/**
	 * Connects a given Output to a destination.
	 * Notifies the plugin about the change.
	 * @param toConnect
	 * @param givenOutput
	 * @throws PluginException
	 */
	public void connectWithOutput(MidiIO toConnect, MidiIO givenOutput) throws PluginException{
		int id = this.outputs.indexOf(givenOutput);
		this.connectWithOutput(toConnect, id);
	}
	
	/**
	 * connects the output at the given index id to the specified output destination.
	 * @param toConnect
	 * @param givenOutputID
	 * @throws PluginException
	 */
	public void connectWithOutput(MidiIO destination, int givenOutputID) throws PluginException{
		if (this.outputs.size() - 1 < givenOutputID || givenOutputID<0){
			throw new PluginOutputNotFoundException();
		} else if (destination.hasInput()){
			throw new PluginException("Output destination connected to other device!");
		} else {
			this.outputs.get(givenOutputID).setOutput(destination);
			//TODO: specific Host event implementation!
			this.plugin.notify(new HostEvent());
		}
	}
	
	/**
	 * Connects the destinations to the given outputs in order of appearence in the list
	 * @param destinations
	 * @param givenOutputs
	 */
	public void connectWithOutputs(LinkedList<MidiIO> destinations, LinkedList<MidiIO> givenOutputs){
		//TODO: implement
	}
	
/**
 * unimplemented
 * @param destinations
 * @param startID
 * @param enableNewOutputs
 */
	public void connectWithOutputs(LinkedList<MidiIO> destinations, int startID, boolean enableNewOutputs){
		//TODO: implement
	}
	
	/**
	 * creates a new output and appends it to the output list.
	 * notifies the plugin aboutt the change.
	 * @return new outputs
	 * @throws PluginMaxOutputsExceededException
	 */
	public MidiIO newOutput() throws PluginMaxOutputsExceededException{
		return this.newOutput(this.getOutputCount());
	}
	
	/**
	 * creates a new output at the specified index id.
	 * notifies the plugin about the change
	 * @param id
	 * @return
	 * @throws PluginMaxOutputsExceededException
	 */
	public MidiIO newOutput(int id) throws PluginMaxOutputsExceededException{
		if(this.plugin.getMaxOutputs() <= this.getOutputCount()){
			throw new PluginMaxOutputsExceededException();
		} else{
			MidiIO tmp = new MidiIO(this);
			this.outputs.add(id, tmp);
		//TODO: HostEvent implementation for this event!
			this.plugin.notify(new HostEvent());
			return tmp;
		}
	}
	/**
	 * unimplemented
	 * @param count
	 */
	public void newOutputs(int count){
		this.newOutputs(count, this.getOutputCount());
	}
	/**
	 * unimplemented
	 * @param count
	 * @param startID
	 */
	public void newOutputs(int count, int startID){
		//TODO:implement!
	}
	
	/**
	 * disconnects the given output
	 * @param output
	 * @throws PluginOutputNotFoundException 
	 */
	public void disconnectOutput(MidiIO output) throws PluginOutputNotFoundException{
		this.disconnectOutput(this.outputs.indexOf(output));
	}
	
	/**
	 * disconnects the output from the given index id
	 * @param id
	 * @throws PluginOutputNotFoundException 
	 */
	public void disconnectOutput(int id) throws PluginOutputNotFoundException{
		if(id<0){
			throw new PluginOutputNotFoundException();
		}else if(id>=this.getOutputCount()){
			throw new IllegalArgumentException("Output index non-existent");
		} else {
			this.outputs.get(id).disconnectOutput();
		}
	}
	/**
	 * unimplemented
	 * @param outputs
	 */
	public void disconnectOutputs(LinkedList<MidiIO> outputs){
		//TODO: implement!
	}
	/**
	 * unimplemented
	 * @param count
	 * @param startID
	 */
	public void disconnectOutputs(int count, int startID){
		//TODO: implement!
	}
	
	/**
	 * disconnects all outputs
	 * @throws PluginOutputNotFoundException
	 */
	public void disconnectAllOutputs() throws PluginOutputNotFoundException{
		for(int i = 0;i<this.getOutputCount();i++){
			this.disconnectOutput(i);
		}
	}
		
	/**
	 * disconnects, than removes the output
	 * @param output
	 * @throws PluginOutputNotFoundException
	 */
	public void removeOutput(MidiIO output) throws PluginOutputNotFoundException{
		this.removeOutput(this.outputs.indexOf(output));
	}
	
	/**
	 * disconnects, than removes the output with give index id
	 * @param outputID
	 * @throws PluginOutputNotFoundException
	 */
	public void removeOutput(int outputID) throws PluginOutputNotFoundException{
		if(outputID<0){
			throw new PluginOutputNotFoundException();
		}else if(outputID>=this.getOutputCount()){
			throw new IllegalArgumentException("Output index non-existent");
		} else {
			this.outputs.get(outputID).disconnectOutput();
			//TODO: implement appropriate Event
			this.plugin.notify(new HostEvent());
			this.outputs.remove(outputID);
		}
	}
	
	/**
	 * unimplemented
	 * @param outputs
	 */
	public void removeOutputs(LinkedList<MidiIO> outputs){
		//TODO: implement
	}
	/**
	 * unimplemented
	 * @param count
	 * @param startID
	 */
	public void removeOutputs(int count, int startID){
		//TODO: implement
	}
	/**
	 * disconnects and removes all outputs
	 * @throws PluginOutputNotFoundException 
	 */
	
	public void removeAllOutputs() throws PluginOutputNotFoundException{
		//TODO: implement good Host Event
		this.plugin.notify(new HostEvent());
		this.disconnectAllOutputs();
		this.outputs = new LinkedList<MidiIO>();
	}
	
	/**
	 * Returns the output count
	 * @return
	 */
	public int getOutputCount(){
		return this.outputs.size();
	}
	
	public int getMidiIOId(MidiIO m){
		int tmp = this.inputs.indexOf(m);
		if(tmp!=-1){
			return tmp;
		} else {
			return this.outputs.indexOf(m);
		}
	}
	
	public LinkedList<MidiIO> getOutputs(){
		return this.outputs;
	}
	
	public LinkedList<MidiIO> getInputs(){
		return this.inputs;
	}
	
	public MidiIO getOuput(int indexID){
		return this.outputs.get(indexID);
	}
	
	public MidiIO getInput(int indexID){
		return this.inputs.get(indexID);
	}
	
	public LinkedList<MidiIO> getConnectedOutputs(){
		LinkedList<MidiIO> tmp = new LinkedList<MidiIO>();
		for(MidiIO m: this.outputs){
			if(m.hasOutput()){
				tmp.add(m);
			}
		}
		return tmp;
	}
	
	public LinkedList<MidiIO> getConnectedInputs(){
		LinkedList<MidiIO> tmp = new LinkedList<MidiIO>();
		for(MidiIO m: this.inputs){
			if(m.hasOutput()){
				tmp.add(m);
			}
		}
		return tmp;
	}
	
	public MidiEngine getEngine(){
		return this.engine;
	}
	
	public abstract void notify(PluginEvent e);
	
}
