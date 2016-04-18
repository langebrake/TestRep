import javax.sound.midi.*;
public class PrimReceive implements Receiver {

	@Override
	public void close() {
		
		
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println(message.getMessage());
		
	}
	
}
