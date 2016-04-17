package engine;

import java.util.LinkedList;

import javax.sound.midi.*;

public class SystemMidiDevices {
	
	public static LinkedList<MidiDevice> getInputDevices() throws MidiUnavailableException{
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
		LinkedList<MidiDevice> tmp = new LinkedList<MidiDevice>();
		for(MidiDevice.Info i:devices){
			MidiDevice device = MidiSystem.getMidiDevice(i);
			if(device.getMaxTransmitters()>0){
				tmp.add(device);
			}
		}
		return tmp;
	}
	
	public static LinkedList<MidiDevice> getOutputDevices() throws MidiUnavailableException{
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
		LinkedList<MidiDevice> tmp = new LinkedList<MidiDevice>();
		for(MidiDevice.Info i:devices){
			MidiDevice device = MidiSystem.getMidiDevice(i);
			if(device.getMaxReceivers()>0){
				tmp.add(device);
			}
		}
		return tmp;
	}
}
