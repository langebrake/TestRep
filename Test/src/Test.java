import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.*;

import model.graph.Module;
import defaults.MidiIOThrough;
import engine.Engine;


public class Test {
	public static void main(String[] args) throws MidiUnavailableException{
		MidiDevice.Info[] myDeviceInfo = null;
		myDeviceInfo = MidiSystem.getMidiDeviceInfo();
		if (myDeviceInfo != null) {
			int i=0;
			for(MidiDevice.Info entry: myDeviceInfo){
				System.out.println(i + entry.toString());
				i++;
			}
		} else {
			System.exit(0);
		}
		
		MidiDevice indevice = null;
		MidiDevice outdevice = null;
		try(Scanner s=new Scanner(System.in)) {
			System.out.print("Input Device: ");
			int i = s.nextInt();
			indevice = MidiSystem.getMidiDevice(myDeviceInfo[i]);
			
			if (!(indevice.isOpen())) {
			    try {
			      indevice.open();
			      System.out.println(indevice.getDeviceInfo() + " Opened!");
			  } catch (MidiUnavailableException e) {
			          e.printStackTrace();
			  }
			}
			System.out.print("\nOutput Device: ");
			i = s.nextInt();
			
			outdevice = MidiSystem.getMidiDevice(myDeviceInfo[i]);
			if (!(outdevice.isOpen())) {
			    try {
			      outdevice.open();
			      System.out.println(outdevice.getDeviceInfo()+" Opened!");
			  } catch (MidiUnavailableException e) {
				  e.printStackTrace();
			  }
			}

		} catch (MidiUnavailableException e) {
			
			 e.printStackTrace();
		}
		
		
		Receiver ewql = null;
		Transmitter midimini = null;
		PrimReceive printreceiver = new PrimReceive();
		MidiIOThrough testio = new MidiIOThrough(new Module());
		try {
			ewql = outdevice.getReceiver();
			midimini = indevice.getTransmitter();
			
			midimini.setReceiver(printreceiver);
		} catch (MidiUnavailableException e) {
			
			e.printStackTrace();
		}
		ShortMessage message = null;
		try {
			message = new ShortMessage(ShortMessage.NOTE_ON,0,60,127);
		} catch (InvalidMidiDataException e) {
			
			e.printStackTrace();
		}
		System.out.println("Message: " + message.getData1());
		ewql.send(message, -1);
		try {
			message = new ShortMessage(ShortMessage.NOTE_OFF,0,60,127);
		} catch (InvalidMidiDataException e) {
			
			e.printStackTrace();
		}
		try {
			TimeUnit.SECONDS.sleep(8);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		ewql.send(message, -1);
		ewql.close();
		midimini.close();
		indevice.close();
		outdevice.close();

		
		
	}
}
