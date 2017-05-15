package engine;

import javax.sound.midi.MidiMessage;

public class MidiUtilities {
	public static final byte NOTE_ON = 0b1001;
	public static final byte NOTE_OFF = 0b1000;
	public static final byte POLY_AFTERTOUCH = 0b1010;
	public static final byte CONTROL_CHANGE = 0b1011;
	public static final byte PROGRAM_CHANGE = 0b1100;
	public static final byte MONO_AFTERTOUCH = 0b1101;
	public static final byte PITCH_BEND = 0b1110;
	public static final byte SYS_EXCLUSIVE = 0b1111;
	

	
	
	public static byte getStatus(MidiMessage msg) {
		return (byte) ((msg.getMessage()[0] & 0xf0) >> 4);
	}

	public static byte getChannel(MidiMessage msg) {
		return (byte) (msg.getMessage()[0] & 0x0f);
	}

	public static byte getData1(MidiMessage msg) {
		return msg.getMessage()[1];
	}

	public static byte getData2(MidiMessage msg) {
		return msg.getMessage()[2];
	}
}
