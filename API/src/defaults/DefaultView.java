package defaults;


import javax.swing.JLabel;


public class DefaultView extends JLabel {
	public DefaultView(){
		this("noname");
	}
	public DefaultView(String name){
		this.setText(name);
	}
}
