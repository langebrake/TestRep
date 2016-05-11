package engine;

public class Stringer {
	public static String stringer = "";
	
	public static String getString(){
		stringer = stringer + "~~|";
		return stringer;
	}
	
	public static void minimize(){
		stringer = stringer.substring(0, stringer.length()-3);
	}
}
