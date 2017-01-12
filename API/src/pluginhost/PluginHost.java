package pluginhost;

import java.awt.Component;
import java.awt.Container;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;

import engine.Stringer;
import midiengine.MidiEngine;
import defaults.DefaultView;
import defaults.MidiIO;
import defaults.MidiIOThrough;
import engine.Engine;
import plugin.Plugin;
import plugin.events.NewInputRequestEvent;
import plugin.events.NewOutputRequestEvent;
import plugin.events.PluginCopyError;
import plugin.events.PluginError;
import plugin.events.PluginEvent;
import plugin.events.PluginLoadingError;
import plugin.events.PluginSavingError;
import pluginhost.exceptions.*;
import pluginhost.events.*;

public abstract class PluginHost implements Serializable, Cloneable {

	private LinkedList<MidiIOThrough> inputs, outputs;
	private transient Plugin plugin;
	private transient MidiEngine engine;
	private transient PluginStateChangedListener stateChangedListeners;
	private String name;
	// TODO: this must be managed over retrieving the class from the plugins
	// name over a static manager!!!!!
	private Class<? extends Plugin> pluginClass;
	private boolean notifyPlugin;

	public PluginHost() throws MidiUnavailableException {
		this.notifyPlugin = true;
		this.inputs = new LinkedList<MidiIOThrough>();
		this.outputs = new LinkedList<MidiIOThrough>();
		this.engine = Engine.load();
		this.stateChangedListeners = new DefaultStateChangedListener();
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin p, Class<? extends Plugin> m) throws PluginMaxOutputsExceededException {
		this.pluginClass = m;
		if (this.plugin != null) {
			try {
				this.plugin.close();
			} catch (Exception e) {
				// TODO: Handle and tell the exception listener;
				stateChangedListeners.listen(new PluginError(e, this));
			}
		}
		this.plugin = p;

		// fill minimum inputs
		for (int i = this.getInputCount(); i < this.plugin.getMinInputs(); i++) {
			this.newInput();
		}
		// fill minimum outputs
		for (int i = this.getOutputCount(); i < this.plugin.getMinOutputs(); i++) {
			this.newOutput();
		}
		// TODO: delete maximum outputs

		// TODO: delete maximum inputs
		try {
			this.name = plugin.getPluginName();
			if (this.name == null) {
				this.name = "";
			}
		} catch (Exception e) {
			this.name = "";
			stateChangedListeners.listen(new PluginError(e, this));
		}
		try {
			p.load();
		} catch (Exception e) {
			stateChangedListeners.listen(new PluginError(e, this));
		}
	}

	public void notifyPluginStateChangedListener(PluginEvent e) {
		this.stateChangedListeners.listen(e);
	}

	public void setPluginStateChangedListener(PluginStateChangedListener l) {
		this.stateChangedListeners = l;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	// /**
	// * Connect an input source to this host. Connects the source to a not yet
	// connected
	// * input, creates a new host input if no free input is aviable
	// * @param toConnect
	// * @throws PluginMaxInputsExceededException
	// */
	// public void connectInput(MidiIOThrough toConnect) throws
	// PluginMaxInputsExceededException{
	// LinkedList<MidiIOThrough> freeInputs = this.getFreeInputs();
	// if(freeInputs.size() == 0){
	// this.connectNewInput(toConnect);
	// } else {
	// freeInputs.get(0).setInput(toConnect);
	// }
	// }
	// /**
	// * Creates a new input for this host and connects it to the given source.
	// * Than notifies the plugin about this change
	// * @param toConnect
	// * @throws PluginMaxInputsExceededException
	// */
	// public void connectNewInput(MidiIOThrough toConnect) throws
	// PluginMaxInputsExceededException{
	// this.connectNewInput(toConnect, this.getInputCount());
	// }

	// /**
	// * Creates a new input for this host at the specified index id and
	// connects it to the given source
	// * Notifies the plugin about this change
	// * @param toConnect
	// * @param id
	// * @throws PluginMaxInputsExceededException
	// */
	// public void connectNewInput(MidiIOThrough toConnect, int id) throws
	// PluginMaxInputsExceededException{
	// if(plugin.getMaxInputs() >= this.getInputCount()){
	// throw new PluginMaxInputsExceededException();
	// }else if (this.getInputCount() < id) {
	// throw new IllegalArgumentException("Input connection error: illegal id");
	// }else {
	// MidiIOThrough newInput = new MidiIOThrough(this);
	// newInput.setInput(toConnect);
	// this.inputs.add(id,newInput);
	// //TODO: Host Event Implementation of InputEvent
	// this.plugin.notify(new HostEvent());
	// }
	// }

	//
	// public void connectNewInputs(LinkedList<MidiIOThrough> toConnect){
	// this.connectNewInputs(toConnect,this.getInputCount());
	// }

	// public void connectNewInputs(LinkedList<MidiIOThrough> toConnect, int id
	// ){
	// //TODO: Implementation!!
	// }
	// /**
	// * Connects a given Input to a source.
	// * Notifies the plugin about the change.
	// * @param toConnect
	// * @param givenInput
	// * @throws PluginException
	// */
	// public void connectWithInput(MidiIOThrough toConnect, MidiIOThrough
	// givenInput) throws PluginException{
	// int id = this.inputs.indexOf(givenInput);
	// this.connectWithInput(toConnect, id);
	// }
	//
	// /**
	// * connects the input at the given index id to the specified input source.
	// * @param toConnect
	// * @param givenInputID
	// * @throws PluginException
	// */
	// public void connectWithInput(MidiIOThrough source, int givenInputID)
	// throws PluginException{
	// if (this.inputs.size() - 1 < givenInputID || givenInputID<0){
	// throw new PluginInputNotFoundException();
	// } else if (source.hasOutput()){
	// throw new PluginException("Input Source connected to other device!");
	// } else {
	// this.inputs.get(givenInputID).setInput(source);
	// //TODO: specific Host event implementation!
	// this.plugin.notify(new HostEvent());
	// }
	// }
	//
	// /**
	// * Connects the sources to the given inputs in order of appearence in the
	// list
	// * @param sources
	// * @param givenInputs
	// */
	// public void connectWithInputs(LinkedList<MidiIOThrough> sources,
	// LinkedList<MidiIOThrough> givenInputs){
	// //TODO: implement
	// }
	//
	/// **
	// *
	// * @param sources
	// * @param startID
	// * @param enableNewInputs
	// */
	// public void connectWithInputs(LinkedList<MidiIOThrough> sources, int
	// startID, boolean enableNewInputs){
	// //TODO: implement
	// }

	/**
	 * creates a new input and appends it to the input list. notifies the plugin
	 * aboutt the change.
	 * 
	 * @return new input
	 * @throws PluginMaxInputsExceededException
	 */
	public MidiIOThrough newInput() {
		return this.newInput(this.getInputCount());
	}

	/**
	 * creates a new input at the specified index id. notifies the plugin about
	 * the change returns null if no vaild input can be obtained
	 * 
	 * @param id
	 * @return
	 * @throws PluginMaxInputsExceededException
	 */
	public MidiIOThrough newInput(int id) {
		if (this.plugin.getMaxInputs() != -1 && this.plugin.getMaxInputs() <= this.getInputCount()) {
			return null;
		} else {
			MidiIOThrough tmp = new MidiIOThrough(this);
			this.inputs.add(id, tmp);

			// TODO: HostEvent implementation for this event!

			NewInputEvent event = new NewInputEvent(tmp);
			try {
				if (notifyPlugin)
					this.plugin.notify(event);
			} catch (Exception e) {
				this.stateChangedListeners.listen(new PluginError(e, this));
				return null;
			}

			if (this.stateChangedListeners != null)
				this.stateChangedListeners.listen(event);

			return tmp;
		}
	}

	// public void newInputs(int count){
	// this.newInputs(count, this.getInputCount());
	// }

	// public void newInputs(int count, int startID){
	//
	// }

	// /**
	// * disconnects the given input
	// * @param input
	// * @throws PluginInputNotFoundException
	// */
	// public void disconnectInput(MidiIOThrough input) throws
	// PluginInputNotFoundException{
	// this.disconnectInput(this.inputs.indexOf(input));
	// }
	//
	// /**
	// * disconnects the input from the given index id
	// * @param id
	// * @throws PluginInputNotFoundException
	// */
	// public void disconnectInput(int id) throws PluginInputNotFoundException{
	// if(id<0){
	// throw new PluginInputNotFoundException();
	// }else if(id>=this.getInputCount()){
	// throw new IllegalArgumentException("Input index non-existent");
	// } else {
	// this.inputs.get(id).disconnectInput();
	// }
	// }
	//
	// public void disconnectInputs(LinkedList<MidiIOThrough> inputs){
	// //TODO: implement!
	// }
	//
	// public void disconnectInputs(int count, int startID){
	// //TODO: implement!
	// }

	// /**
	// * disconnects all inputs
	// * @throws PluginInputNotFoundException
	// */
	// public void disconnectAllInputs() throws PluginInputNotFoundException{
	// for(int i = 0;i<this.getInputCount();i++){
	// this.disconnectInput(i);
	// }
	// }

	// public void removeInput() throws PluginInputNotFoundException,
	// PluginMinInputsExceededException{
	// this.removeInput(this.getInputCount()-1);
	// }
	// /**
	// * disconnects, than removes the input
	// * @param input
	// * @throws PluginInputNotFoundException
	// * @throws PluginMinInputsExceededException
	// */
	// public void removeInput(MidiIOThrough input) throws
	// PluginInputNotFoundException, PluginMinInputsExceededException{
	// this.removeInput(this.inputs.indexOf(input));
	// }

	// /**
	// * disconnects, than removes the input with give index id
	// * @param inputID
	// * @throws PluginInputNotFoundException
	// * @throws PluginMinInputsExceededException
	// */
	// public void removeInput(int inputID) throws PluginInputNotFoundException,
	// PluginMinInputsExceededException{
	// if (this.getInputCount() -1 < this.plugin.getMinInputs()){
	// throw new PluginMinInputsExceededException();
	// } else if(inputID<0){
	// throw new PluginInputNotFoundException();
	// }else if(inputID>=this.getInputCount()){
	// throw new IllegalArgumentException("Input index non-existent");
	// } else {
	//
	// this.inputs.get(inputID).disconnectInput();
	// //TODO: implement appropriate Event
	// this.plugin.notify(new HostEvent());
	// this.inputs.remove(inputID);
	// }
	// }
	//
	// public void removeInputs(LinkedList<MidiIOThrough> inputs){
	// //TODO: implement, mind the minimal inputs
	// }
	//
	// public void removeInputs(int count, int startID){
	// //TODO: implement, mind the minimal inputs
	// }
	// /**
	// * removes all inputs
	// * @throws PluginInputNotFoundException
	// * @throws PluginMinInputsExceededException
	// */
	//
	// public void removeAllInputs() throws PluginInputNotFoundException,
	// PluginMinInputsExceededException{
	// if(this.plugin.getMinInputs()!=1){
	// throw new PluginMinInputsExceededException();
	// }
	// //TODO: implement good Host Event
	// this.plugin.notify(new HostEvent());
	// this.disconnectAllInputs();
	// this.inputs = new LinkedList<MidiIOThrough>();
	// }

	/**
	 * Returns the input count
	 * 
	 * @return
	 */
	public int getInputCount() {
		return this.inputs.size();
	}

	// /**
	// * Connect an output destination to this host. Connects the destination to
	// a not yet connected
	// * output, creates a new host output if no free output is aviable
	// * @param toConnect
	// * @throws PluginMaxOutputsExceededException
	// */
	// public void connectOutput(MidiIOThrough toConnect) throws
	// PluginMaxOutputsExceededException {
	// LinkedList<MidiIOThrough> freeOutputs = this.getFreeOutputs();
	// if(freeOutputs.size() == 0){
	// this.connectNewOutput(toConnect);
	// } else {
	// freeOutputs.get(0).setOutput(toConnect);
	// }
	// }
	// /**
	// * Creates a new output for this host and connects it to the given
	// destination.
	// * Than notifies the plugin about this change
	// * @param toConnect
	// * @throws PluginMaxOutputsExceededException
	// */
	// public void connectNewOutput(MidiIOThrough toConnect) throws
	// PluginMaxOutputsExceededException{
	// this.connectNewOutput(toConnect, this.getOutputCount());
	// }
	//
	// /**
	// * Creates a new output for this host at the specified index id and
	// connects it to the given destination
	// * Notifies the plugin about this change
	// * @param toConnect
	// * @param id
	// * @throws PluginMaxOutputsExceededException
	// */
	// public void connectNewOutput(MidiIOThrough toConnect, int id) throws
	// PluginMaxOutputsExceededException{
	// if(plugin.getMaxOutputs() >= this.getOutputCount()){
	// throw new PluginMaxOutputsExceededException();
	// }else if (this.getOutputCount() < id) {
	// throw new IllegalArgumentException("Output connection error: illegal
	// id");
	// }else {
	// MidiIOThrough newOutput = new MidiIOThrough(this);
	// newOutput.setOutput(toConnect);
	// this.outputs.add(id,newOutput);
	// //TODO: Host Event Implementation of OutputEvent
	// this.plugin.notify(new HostEvent());
	// }
	// }
	//
	//
	// public void connectNewOutputs(LinkedList<MidiIOThrough> toConnect){
	// this.connectNewOutputs(toConnect,this.getOutputCount());
	// }
	//
	// public void connectNewOutputs(LinkedList<MidiIOThrough> toConnect, int id
	// ){
	// //TODO: Implementation!!
	// }
	// /**
	// * Connects a given Output to a destination.
	// * Notifies the plugin about the change.
	// * @param toConnect
	// * @param givenOutput
	// * @throws PluginException
	// */
	// public void connectWithOutput(MidiIOThrough toConnect, MidiIOThrough
	// givenOutput) throws PluginException{
	// int id = this.outputs.indexOf(givenOutput);
	// this.connectWithOutput(toConnect, id);
	// }
	//
	// /**
	// * connects the output at the given index id to the specified output
	// destination.
	// * @param toConnect
	// * @param givenOutputID
	// * @throws PluginException
	// */
	// public void connectWithOutput(MidiIOThrough destination, int
	// givenOutputID) throws PluginException{
	// if (this.outputs.size() - 1 < givenOutputID || givenOutputID<0){
	// throw new PluginOutputNotFoundException();
	// } else if (destination.hasInput()){
	// throw new PluginException("Output destination connected to other
	// device!");
	// } else {
	// this.outputs.get(givenOutputID).setOutput(destination);
	// //TODO: specific Host event implementation!
	// this.plugin.notify(new HostEvent());
	// }
	// }
	//
	// /**
	// * Connects the destinations to the given outputs in order of appearence
	// in the list
	// * @param destinations
	// * @param givenOutputs
	// */
	// public void connectWithOutputs(LinkedList<MidiIOThrough> destinations,
	// LinkedList<MidiIOThrough> givenOutputs){
	// //TODO: implement
	// }
	//
	/// **
	// * unimplemented
	// * @param destinations
	// * @param startID
	// * @param enableNewOutputs
	// */
	// public void connectWithOutputs(LinkedList<MidiIOThrough> destinations,
	// int startID, boolean enableNewOutputs){
	// //TODO: implement
	// }

	/**
	 * creates a new output and appends it to the output list. notifies the
	 * plugin aboutt the change.
	 * 
	 * @return new outputs
	 * @throws PluginMaxOutputsExceededException
	 */
	public MidiIOThrough newOutput() {
		return this.newOutput(this.getOutputCount());
	}

	/**
	 * creates a new output at the specified index id. notifies the plugin about
	 * the change returns null if max outputs exceeded
	 * 
	 * @param id
	 * @return
	 * @throws PluginMaxOutputsExceededException
	 */
	public MidiIOThrough newOutput(int id) {
		if (this.plugin.getMaxOutputs() != -1 && this.plugin.getMaxOutputs() <= this.getOutputCount()) {
			return null;
		} else {
			MidiIOThrough tmp = new MidiIOThrough(this);
			this.outputs.add(id, tmp);
			// TODO: HostEvent implementation for this event!
			NewOutputEvent event = new NewOutputEvent(tmp);
			try {
				if (notifyPlugin)
					this.plugin.notify(event);
			} catch (Exception e) {
				this.stateChangedListeners.listen(new PluginError(e, this));
				return null;
			}

			if (this.stateChangedListeners != null)
				this.stateChangedListeners.listen(event);
			return tmp;
		}
	}
	// /**
	// * unimplemented
	// * @param count
	// */
	// public void newOutputs(int count){
	// this.newOutputs(count, this.getOutputCount());
	// }
	// /**
	// * unimplemented
	// * @param count
	// * @param startID
	// */
	// public void newOutputs(int count, int startID){
	// //TODO:implement!
	// }
	//
	// /**
	// * disconnects the given output
	// * @param output
	// * @throws PluginOutputNotFoundException
	// */
	// public void disconnectOutput(MidiIOThrough output) throws
	// PluginOutputNotFoundException{
	// this.disconnectOutput(this.outputs.indexOf(output));
	// }
	//
	// /**
	// * disconnects the output from the given index id
	// * @param id
	// * @throws PluginOutputNotFoundException
	// */
	// public void disconnectOutput(int id) throws
	// PluginOutputNotFoundException{
	// if(id<0){
	// throw new PluginOutputNotFoundException();
	// }else if(id>=this.getOutputCount()){
	// throw new IllegalArgumentException("Output index non-existent");
	// } else {
	// this.outputs.get(id).disconnectOutput();
	// }
	// }
	// /**
	// * unimplemented
	// * @param outputs
	// */
	// public void disconnectOutputs(LinkedList<MidiIOThrough> outputs){
	// //TODO: implement!
	// }
	// /**
	// * unimplemented
	// * @param count
	// * @param startID
	// */
	// public void disconnectOutputs(int count, int startID){
	// //TODO: implement!
	// }
	//
	// /**
	// * disconnects all outputs
	// * @throws PluginOutputNotFoundException
	// */
	// public void disconnectAllOutputs() throws PluginOutputNotFoundException{
	// for(int i = 0;i<this.getOutputCount();i++){
	// this.disconnectOutput(i);
	// }
	// }
	//
	// /**
	// * disconnects, than removes the output
	// * @param output
	// * @throws PluginOutputNotFoundException
	// * @throws PluginMinOutputsExceededException
	// */
	// public void removeOutput(MidiIOThrough output) throws
	// PluginOutputNotFoundException, PluginMinOutputsExceededException{
	// this.removeOutput(this.outputs.indexOf(output));
	// }
	//
	// /**
	// * disconnects, than removes the output with give index id
	// * @param outputID
	// * @throws PluginOutputNotFoundException
	// * @throws PluginMinOutputsExceededException
	// */
	// public void removeOutput(int outputID) throws
	// PluginOutputNotFoundException, PluginMinOutputsExceededException{
	// if(outputID<0){
	// throw new PluginOutputNotFoundException();
	// }else if(outputID>=this.getOutputCount()){
	// throw new IllegalArgumentException("Output index non-existent");
	// } else if(this.plugin.getMinOutputs() > this.getOutputCount() - 1){
	// throw new PluginMinOutputsExceededException();
	// }
	// else {
	// this.outputs.get(outputID).disconnectOutput();
	// //TODO: implement appropriate Event
	// this.plugin.notify(new HostEvent());
	// this.outputs.remove(outputID);
	// }
	// }
	//
	// /**
	// * unimplemented
	// * @param outputs
	// */
	// public void removeOutputs(LinkedList<MidiIOThrough> outputs){
	// //TODO: implement, mind the min outputs by plugin
	// }
	// /**
	// * unimplemented
	// * @param count
	// * @param startID
	// */
	// public void removeOutputs(int count, int startID){
	// //TODO: implement, mind the min outputs by plugin
	// }
	// /**
	// * disconnects and removes all outputs
	// * @throws PluginOutputNotFoundException
	// * @throws PluginMinOutputsExceededException
	// */
	//
	// public void removeAllOutputs() throws PluginOutputNotFoundException,
	// PluginMinOutputsExceededException{
	// if(this.plugin.getMinOutputs() != 0){
	// throw new PluginMinOutputsExceededException();
	// }
	// //TODO: implement good Host Event
	// this.plugin.notify(new HostEvent());
	// this.disconnectAllOutputs();
	// this.outputs = new LinkedList<MidiIOThrough>();
	// }

	/**
	 * Returns the output count
	 * 
	 * @return
	 */
	public int getOutputCount() {
		return this.outputs.size();
	}

	public int getMidiIOId(MidiIOThrough m) {
		int tmp = this.inputs.indexOf(m);
		if (tmp != -1) {
			return tmp;
		} else {
			return this.outputs.indexOf(m);
		}
	}

	public LinkedList<MidiIOThrough> getOutputs() {
		return this.outputs;
	}

	public LinkedList<MidiIOThrough> getInputs() {
		return this.inputs;
	}

	public MidiIOThrough getOuput(int indexID) {
		return this.outputs.get(indexID);
	}

	public MidiIOThrough getInput(int indexID) {
		return this.inputs.get(indexID);
	}

	// /**
	// * Get a list of not connected outputs
	// * @return
	// */
	// public LinkedList<MidiIOThrough> getFreeOutputs(){
	// LinkedList<MidiIOThrough> tmp = new LinkedList<MidiIOThrough>();
	// for(MidiIOThrough m: this.outputs){
	// if(!m.hasOutput()){
	// tmp.add(m);
	// }
	// }
	// return tmp;
	// }

	// public LinkedList<MidiIOThrough> getFreeInputs(){
	// LinkedList<MidiIOThrough> tmp = new LinkedList<MidiIOThrough>();
	// for(MidiIOThrough m: this.inputs){
	// if(!m.hasInput()){
	// tmp.add(m);
	// }
	// }
	// return tmp;
	// }

	// public LinkedList<MidiIOThrough> getConnectedOutputs(){
	// LinkedList<MidiIOThrough> tmp = new LinkedList<MidiIOThrough>();
	// for(MidiIOThrough m: this.outputs){
	// if(m.hasOutput()){
	// tmp.add(m);
	// }
	// }
	// return tmp;
	// }
	//
	// public LinkedList<MidiIOThrough> getConnectedInputs(){
	// LinkedList<MidiIOThrough> tmp = new LinkedList<MidiIOThrough>();
	// for(MidiIOThrough m: this.inputs){
	// if(m.hasOutput()){
	// tmp.add(m);
	// }
	// }
	// return tmp;
	// }

	public MidiEngine getEngine() {
		return this.engine;
	}

	public boolean close() {
		boolean res = true;
		try {
			res = this.plugin.close();
		} catch (Exception e) {
			this.stateChangedListeners.listen(new PluginError(e, this));
		}
		return res;
	}

	public boolean reOpen() {
		boolean res = true;
		try {
			res = this.plugin.reOpen();
		} catch (Exception e) {
			this.stateChangedListeners.listen(new PluginError(e, this));
		}
		return res;
	}

	public void notify(PluginEvent e) {
		if (e.getClass() == NewOutputRequestEvent.class) {
			notifyPlugin = false;
			((NewOutputRequestEvent) e).io = this.newOutput();
			notifyPlugin = true;
		} else if (e.getClass() == NewInputRequestEvent.class) {
			notifyPlugin = false;
			((NewInputRequestEvent) e).io = this.newInput();
			notifyPlugin = true;
		} else if (e.getClass() == PluginError.class) {

		}

		if (this.stateChangedListeners != null)
			this.stateChangedListeners.listen(e);
	}

	public Component getFullView() {
		Component tmp = null;
		try {
			tmp = this.plugin.getFullView();
		} catch (Exception e) {
			this.stateChangedListeners.listen(new PluginError(e, this));
			return new DefaultView(this.getName());
		}
		if (tmp == null) {
			return new DefaultView(this.getName());
		}
		return tmp;
	}

	/**
	 * All references to inner Plugin are removed from the MidiIO interfaces.
	 * The Plugin has to take care about the connection.
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		String stringer = Stringer.getString();
		// Remove all References to inner Plugin on the MidiIO endpoints
		LinkedList<MidiIO> oldInputForward = new LinkedList<MidiIO>(), oldOutputForward = new LinkedList<MidiIO>();
		for (MidiIO m : this.inputs) {
			oldInputForward.add(m.getOutput());
			m.disconnectOutput();
		}
		for (MidiIO m : this.outputs) {
			oldOutputForward.add(m.getInput());
			m.disconnectInput();
		}

		out.defaultWriteObject();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		byte[] plugin = null;
		try {
			oos.writeObject(this.plugin);
			plugin = bos.toByteArray();
		} catch (NotSerializableException e) {
			// e.printStackTrace();
			plugin = new byte[0];
			this.stateChangedListeners.listen(new PluginSavingError(e, this));
		}

		out.writeObject(plugin);

		// restore inner References
		Iterator<MidiIOThrough> inputIterator = this.inputs.iterator();
		Iterator<MidiIOThrough> outputIterator = this.outputs.iterator();
		for (MidiIO m : oldInputForward) {
			inputIterator.next().setOutput(m);
		}
		for (MidiIO m : oldOutputForward) {
			outputIterator.next().setInput(m);
		}
		Stringer.minimize();

	}

	public static PluginStateChangedListener stateListener = null;

	public static PluginStateChangedListener getInitStateListener() {
		PluginStateChangedListener tmp = (stateListener == null) ? new DefaultStateChangedListener() : stateListener;
		return tmp;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		try {
			this.engine = Engine.load();
		} catch (MidiUnavailableException e) {
			// TODO LOG!
			e.printStackTrace();
		}
		this.stateChangedListeners = getInitStateListener();
		in.defaultReadObject();
		for (MidiIO m : this.inputs) {
			m.setPluginHost(this);
		}
		for (MidiIO m : this.outputs) {
			m.setPluginHost(this);
		}
		Plugin.waiter = this;
		try {
			byte[] b = (byte[]) in.readObject();
			ByteArrayInputStream bos = new ByteArrayInputStream(b);
			ObjectInputStream oos = new ObjectInputStream(bos);
			this.plugin = (Plugin) oos.readObject();
		} catch (Exception e) {
			e.printStackTrace();

			try {
				Method getInstance = pluginClass.getDeclaredMethod("getInstance", PluginHost.class);
				this.setPlugin((Plugin) getInstance.invoke(null, this), this.pluginClass);
				this.stateChangedListeners.listen(new PluginLoadingError(e, this));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e1) {
				this.stateChangedListeners.listen(new PluginLoadingError(e, this));

			} catch (PluginMaxOutputsExceededException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	/**
	 * returns null if no valid copy can be obtained connections are not cloned!
	 * this would result in very long loading times because MidiIOs are
	 * interconnected and the net gets fully serialized.
	 */
	public PluginHost clone() {

		PluginHost newHost = null;
		try {
			newHost = (PluginHost) super.clone();
			newHost.inputs = new LinkedList<MidiIOThrough>();
			newHost.outputs = new LinkedList<MidiIOThrough>();
		} catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		newHost.stateChangedListeners = this.stateChangedListeners;
		int max = inputs.size();
		for (int i = 0; i < max; i++) {
			newHost.inputs.add(new MidiIOThrough(newHost));
		}
		max = outputs.size();
		for (int i = 0; i < max; i++) {

			newHost.outputs.add(new MidiIOThrough(newHost));
		}

		newHost.setName(this.getName());
		Plugin.waiter = newHost;
		newHost.plugin = this.plugin.clone();
		newHost.pluginClass = this.pluginClass;

		return newHost;

		// LinkedList<MidiIO> oldInputs = new LinkedList<MidiIO>(),
		// oldOutputs = new LinkedList<MidiIO>();
		// for(MidiIO m:this.inputs){
		// oldInputs.add(m.getInput());
		// m.disconnectInput();
		// }
		// for(MidiIO m:this.outputs){
		// oldOutputs.add(m.getOutput());
		// m.disconnectOutput();
		// }
		//
		// PluginHost tmp = null;
		// try{
		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// ObjectOutputStream oos = new ObjectOutputStream(bos);
		// oos.writeObject(this);
		// byte[] copy = bos.toByteArray();
		//
		// ByteArrayInputStream bin = new ByteArrayInputStream(copy);
		// ObjectInputStream oin = new ObjectInputStream(bin);
		// tmp = (PluginHost) oin.readObject();
		// } catch (Exception e){
		// this.stateChangedListeners.listen(new PluginCopyError(e, this));
		// }
		//
		//
		// //restore connections
		// Iterator<MidiIOThrough> inputIterator = this.inputs.iterator();
		// Iterator<MidiIOThrough> outputIterator = this.outputs.iterator();
		// for(MidiIO m:oldInputs){
		// inputIterator.next().setInput(m);
		// }
		// for(MidiIO m:oldOutputs){
		// outputIterator.next().setOutput(m);
		// }
		//
		// return tmp;
	}

}
