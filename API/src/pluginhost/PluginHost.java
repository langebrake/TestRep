package pluginhost;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;

import midiengine.MidiEngine;
import defaults.DefaultView;
import defaults.MidiIO;
import defaults.MidiIOThrough;
import dev.MidiIOCommunicator;
import dev.Plugin;
import dev.PluginHostCommunicator;
import dev.hostevents.*;
import dev.pluginevents.NewInputRequestEvent;
import dev.pluginevents.NewOutputRequestEvent;
import dev.pluginevents.PluginError;
import dev.pluginevents.PluginEvent;
import dev.pluginevents.PluginSavingError;
import engine.Engine;
import pluginhost.exceptions.*;

public abstract class PluginHost implements Serializable, Cloneable, PluginHostCommunicator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2021581852556325825L;
	private LinkedList<MidiIOCommunicator> inputs, outputs;
	private transient Plugin plugin;
	private transient MidiEngine engine;
	private transient PluginStateChangedListener stateChangedListeners;
	private String name;
	private boolean notifyPlugin;
	//errormanagement on loading
	private transient byte[] errorByte;
	private transient String errorString;
	private transient int ins,outs;

	public PluginHost() throws MidiUnavailableException {
		this.notifyPlugin = true;
		this.inputs = new LinkedList<MidiIOCommunicator>();
		this.outputs = new LinkedList<MidiIOCommunicator>();
		this.engine = Engine.load();
		this.stateChangedListeners = new DefaultStateChangedListener();
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin p, Class<? extends Plugin> m) throws PluginMaxOutputsExceededException {
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

	/**
	 * Returns the input count
	 * 
	 * @return
	 */
	public int getInputCount() {
		return this.inputs.size();
	}

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

	public LinkedList<MidiIOCommunicator> getOutputs() {
		return this.outputs;
	}

	public LinkedList<MidiIOCommunicator> getInputs() {
		return this.inputs;
	}

	public MidiIO getOutput(int indexID) {
		return (MidiIO) this.outputs.get(indexID);
	}

	public MidiIO getInput(int indexID) {
		return (MidiIO) this.inputs.get(indexID);
	}


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
		// Remove all References to inner Plugin on the MidiIO endpoints
		LinkedList<MidiIO> oldInputForward = new LinkedList<MidiIO>(), oldOutputForward = new LinkedList<MidiIO>();
		for (MidiIOCommunicator m : this.inputs) {
			oldInputForward.add(((MidiIO) m).getOutput());
			((MidiIO) m).disconnectOutput();
		}
		for (MidiIOCommunicator m : this.outputs) {
			oldOutputForward.add(((MidiIO) m).getInput());
			((MidiIO) m).disconnectInput();
		}
		if(errorString != null){
			this.setName(errorString);
		}
		out.defaultWriteObject();
		if(errorString != null){
			this.setName("LOADING ERROR");
		}
		if(errorByte == null){
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
		}
		else{
			out.writeObject(errorByte);
		}

		// restore inner References
		Iterator<MidiIOCommunicator> inputIterator = this.inputs.iterator();
		Iterator<MidiIOCommunicator> outputIterator = this.outputs.iterator();
		for (MidiIO m : oldInputForward) {
			((MidiIO) inputIterator.next()).setOutput(m);
		}
		for (MidiIO m : oldOutputForward) {
			((MidiIO) outputIterator.next()).setInput(m);
		}

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
		for (MidiIOCommunicator m : this.inputs) {
			((MidiIO) m).setPluginHost(this);
			ins++;
		}
		for (MidiIOCommunicator m : this.outputs) {
			((MidiIO) m).setPluginHost(this);
			outs++;
		}
		Plugin.waiter = this;
		byte[] b = (byte[]) in.readObject();
		try {
			
			ByteArrayInputStream bos = new ByteArrayInputStream(b);
			ObjectInputStream oos = new ObjectInputStream(bos);
			this.plugin = (Plugin) oos.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			String errorMSG = "Loading this plugin failed!";
			if(e instanceof ClassNotFoundException){
				errorMSG = "Plugin not found: "+e.getLocalizedMessage();
			}
			this.plugin = new ErrorPlugin(this,errorMSG,ins,outs);
			this.errorByte = b;
			this.errorString = this.name;
			this.setName("LOADING ERROR");

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
			newHost.inputs = new LinkedList<MidiIOCommunicator>();
			newHost.outputs = new LinkedList<MidiIOCommunicator>();
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
		if(newHost.plugin == null){
			newHost.plugin = new ErrorPlugin(this, "This plugin does not allow duplicates",0,0);
			newHost.setName("DUPLICATION ERROR");
		}

		return newHost;
	}

}
