import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.MidiUnavailableException;

import plugin.Plugin;
import engine.Engine;
import model.graph.Module;


public class GraphTest {
	public static void main(String[] args) {
		System.out.println("WELCOME!");
		try (	Module inputModule = new Module(Engine.load());
				Module defaultModule= new Module(Engine.load());
				Module outputModule= new Module(Engine.load())){
			
			Plugin inputPlugin = new MidiInputPlugin(inputModule);
			inputModule.setPlugin(inputPlugin);
			
			Plugin defaultPlugin = new DefaultPlugin(defaultModule);
			defaultModule.setPlugin(defaultPlugin);
			
			Plugin outputPlugin = new MidiOutputPlugin(outputModule);
			outputModule.setPlugin(outputPlugin);
			
			defaultModule.connectInput(inputModule.getOuput(0));
			outputModule.connectInput(defaultModule.getOuput(0));
			
			Scanner s = new Scanner(System.in);
			s.nextLine();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
