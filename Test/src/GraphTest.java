import java.util.Scanner;

//import defaultplugin.DefaultPlugin;
import plugin.Plugin;
import engine.Engine;
import model.graph.Module;


public class GraphTest {
	public static void main(String[] args) {
		System.out.println("WELCOME!");
		try {
			Module inputModule = new Module();
			Module defaultModule= new Module();
			Module outputModule= new Module();
//			Plugin inputPlugin = new MidiInputPlugin(inputModule);
//			inputModule.setPlugin(inputPlugin);
			
//			Plugin defaultPlugin = new DefaultPlugin(defaultModule);
//			defaultModule.setPlugin(defaultPlugin);
			
//			Plugin outputPlugin = new MidiOutputPlugin(outputModule);
//			outputModule.setPlugin(outputPlugin);
			
//			defaultModule.connectInput(inputModule.getOuput(0));
//			outputModule.connectInput(defaultModule.getOuput(0));
			
			Scanner s = new Scanner(System.in);
			s.nextLine();
			s.close();
			inputModule.close();
			defaultModule.close();
			outputModule.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
