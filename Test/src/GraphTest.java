import javax.sound.midi.MidiUnavailableException;

import plugin.Plugin;
import engine.Engine;
import model.graph.Module;


public class GraphTest {
	public static void main(String[] args) {
		try{
			System.out.println("BEGIN");
			Module inputModule = new Module(Engine.load());
			Plugin inputPlugin = new MidiInputPlugin(inputModule);
			inputModule.setPlugin(inputPlugin);
			System.out.println("FINISHED");
			
			
			Module defaultModule = new Module(Engine.load());
			Plugin defaultPlugin = new DefaultPlugin(defaultModule);
			defaultModule.setPlugin(defaultPlugin);
			
			
			
			defaultModule.connectInput(inputModule.getOuput(0));
			
			
			defaultModule.close();
			inputModule.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}
