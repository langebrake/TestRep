package switcherplugin_v01;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import plugin.Plugin;
import pluginhost.PluginHost;
import pluginhost.events.HostEvent;

public class SwitcherPlugin extends Plugin implements ActionListener {
	private static final int MAXINPUTS = -1;
	private static final int MAXOUTPUTS = -1;
	private static final int MININPUTS = 0;
	private static final int MINOUTPUTS = 0;
	private static final String NAME = "Switcher";
	LinkedList<Routing> routings;
	transient SwitchView fullView;
	
	public SwitcherPlugin(PluginHost host) {
		super(host);
	}
	
	public static SwitcherPlugin getInstance(PluginHost host){
		return new SwitcherPlugin(host);
	}

	@Override
	public String getPluginName() {
		return NAME;
	}

	@Override
	public JComponent getMinimizedView() {
		return null;
	}

	@Override
	public Component getFullView() {
		JPanel t = new JPanel();
		t.setLayout(new BorderLayout());
		t.add(this.fullView, BorderLayout.CENTER);
		return t;
	}
	
	protected SwitchView getSwitchView(){
		return this.fullView;
	}
	
	@Override
	public String getDisplayName() {
		return NAME;
	}

	@Override
	public void setDisplayName() {
	}

	@Override
	public int getMaxInputs() {
		return MAXINPUTS;
	}

	@Override
	public int getMaxOutputs() {
		return MAXOUTPUTS;
	}

	@Override
	public int getMinInputs() {
		return MININPUTS;
	}

	@Override
	public int getMinOutputs() {
		return MINOUTPUTS;
	}

	@Override
	public void notify(HostEvent e) {
		this.fullView.updateView();
	}

	@Override
	public void load() {
		this.routings = new LinkedList<Routing>();
		this.initPlugin();
	}

	@Override
	public boolean close() {
		return true;
	}

	@Override
	public boolean reOpen() {
		return true;
	}

	@Override
	public Plugin clone() {
		SwitcherPlugin tmp = new SwitcherPlugin(Plugin.waitForHost());
		LinkedList<Routing> rClone = new LinkedList<Routing>();
		for(Routing r:this.routings){
			Routing clone = r.clone();
			clone.setSwitcher(this);
			rClone.add(clone);
		}
		tmp.routings = rClone;
		tmp.initPlugin();
		return tmp;
	}
	
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException{
		this.setPluginHost(Plugin.waitForHost());
		in.defaultReadObject();
		this.initPlugin();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	
	private void initPlugin(){
		for(Routing r:this.routings){
			r.init();
		}
		this.fullView = new SwitchView(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.routings.add(new Routing(this));
		this.fullView.updateView();
		
	}

}
