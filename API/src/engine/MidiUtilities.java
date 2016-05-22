package engine;

import javax.sound.midi.MidiMessage;

public class MidiUtilities {
	public static final byte NOTE_ON = 0b1001;
	public static final byte NOTE_OFF = 0b1000;
	public static final byte CONTROL_CHANGE = 0b1011;
	
	public static byte getStatus(MidiMessage msg){
		return (byte) ((msg.getMessage()[0] & 0xf0) >> 4);
	}
	
	public static byte getChannel(MidiMessage msg){
		return (byte)(msg.getMessage()[0] & 0x0f);
	}
	
	public static byte getData1(MidiMessage msg){
		return msg.getMessage()[1];
	}
	
	public static byte getData2(MidiMessage msg){
		return msg.getMessage()[2];
	}
}
