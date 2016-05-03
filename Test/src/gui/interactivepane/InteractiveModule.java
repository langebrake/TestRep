package gui.interactivepane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

import defaults.MidiIOThrough;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import pluginhost.PluginHost;
import controller.interactivepane.InteractiveController;
import model.graph.Module;

public class InteractiveModule extends InteractiveComponent implements CablePointHost {
	
	
	private Module module;
	private JFrame fullView;
	private JComponent contentPane;
	private boolean inputPopoutActive, outputPopoutActive,
				inputPopoutPermanent, outputPopoutPermanent;
	private Popout inputPopout,outputPopout;
	private CablePointSimple inputPopupConnector,outputPopupConnector;
	private InteractiveCable inputPopoutCable,outputPopoutCable;
	private InteractiveController controller;
	private ConcurrentSkipListMap<CablePointSimple,MidiIOThrough> inputMap;
	private ConcurrentSkipListMap<CablePointSimple,MidiIOThrough> outputMap;
	
	public InteractiveModule(InteractivePane parent, Vector origin, Module module,InteractiveController controller) {
		super(parent, origin);
		this.controller = controller;
		this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.contentPane = module.getPlugin().getMinimizedView();
		this.setOriginDimension(contentPane.getPreferredSize());
		this.add(new InteractiveModuleHeader(this));
		this.add(contentPane);
		
		this.module = module;
		inputPopupConnector = new CablePointSimple(CablePointType.INPUT);
		outputPopupConnector = new CablePointSimple(CablePointType.OUTPUT);
		this.inputPopout = new Popout(this.getParentPane(),this,
				this.getOriginLocation().addVector(new Vector(-100,0)),
				CablePointType.INPUT);
		this.outputPopout = new Popout(this.getParentPane(),this,
				this.getOriginLocation().addVector(new Vector(100+this.getOriginDimension().width,0)),
				CablePointType.OUTPUT);
		this.inputMap = new ConcurrentSkipListMap<CablePointSimple,MidiIOThrough>();
		this.outputMap = new ConcurrentSkipListMap<CablePointSimple,MidiIOThrough>();

		inputPopout.addListeners(controller.getModuleListener(),controller.getCableCreationListener());
		outputPopout.addListeners(controller.getModuleListener(),controller.getCableCreationListener());
		inputPopoutCable = new InteractiveCable(inputPopout.connector, inputPopupConnector, 2, Color.BLUE, parent);
		outputPopoutCable = new InteractiveCable(outputPopout.connector, outputPopupConnector, 2, Color.MAGENTA, parent);
		
		this.updateIO();
		this.updateView();
		
	}
	
	public MidiIOThrough getMidiIO(CablePoint tmp){
		if(tmp.getType() == CablePointType.INPUT){
			return this.inputMap.get(tmp);
		} else {
			return this.outputMap.get(tmp);
		}
	}
	public void updateIO(){
		LinkedList<MidiIOThrough> inputs = module.getInputs();
		LinkedList<MidiIOThrough> outputs = module.getOutputs();
		boolean newPoint = false;
		for(MidiIOThrough m:inputs){
			if(!inputMap.containsValue(m)){
				CablePointSimple cps = new CablePointSimple(CablePointType.INPUT);
				cps.setHost(this);
				cps.setIndex(inputs.indexOf(m));
				inputMap.put(cps,m);
				newPoint = true;
			}
		}
		if(newPoint){
			this.inputPopout.updateIO();
		}
		newPoint = false;
		for(MidiIOThrough m:outputs){
			if(!outputMap.containsValue(m)){
				CablePointSimple cps = new CablePointSimple(CablePointType.OUTPUT);
				cps.setHost(this);
				cps.setIndex(outputs.indexOf(m));
				outputMap.put(cps,m);
				newPoint = true;
			}
		}
		if(newPoint){
			this.outputPopout.updateIO();
		}
	}
	
	public Module getModule(){
		return this.module;
	}
	
	@Override
	public void setHovered(boolean set){
		super.setHovered(set);
		if(set)
			this.setBorder(BorderFactory.createLineBorder(Color.black));
		else {
			if(this.isSelected()){
				this.setBorder(BorderFactory.createLineBorder(Color.red));
			} else {
				this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
			}
		}	
	}
	
	@Override
	public void setSelected(boolean set){
		super.setSelected(set);
		if(set)
			this.setBorder(BorderFactory.createLineBorder(Color.red));
		else
			this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
	}
	
	public boolean hasInputPopout(){
		return this.inputPopoutActive;
	}
	public void inputPopout(boolean set,boolean permanent){
		if(!this.inputMap.isEmpty() || this.inputPopoutActive){
			this.inputPopoutActive = set;
			this.inputPopoutPermanent = set;
			if(set){
				if(!this.getParentPane().isAncestorOf(this.inputPopout)){
					this.getParentPane().add(this.inputPopout);
					this.getParentPane().addStatic(this.inputPopoutCable);
				}
			} else {
				if(this.getParentPane().isAncestorOf(this.inputPopout)){
					this.getParentPane().removeStatic(this.inputPopoutCable);
					this.getParentPane().remove(this.inputPopout);
				}
				
			}
		}
	}
	
	public boolean hasOutputPopout(){
		return this.outputPopoutActive;
	}
	
	public void outputPopout(boolean set,boolean permanent){
		if(!this.outputMap.isEmpty() || this.outputPopoutActive){
			this.outputPopoutActive = set;
			this.outputPopoutPermanent = set;
			if(set){
				if(!this.getParentPane().isAncestorOf(this.outputPopout)){
					this.getParentPane().add(this.outputPopout);
					this.getParentPane().addStatic(this.outputPopoutCable);
				}
			} else {
				if(this.getParentPane().isAncestorOf(this.outputPopout)){
					this.getParentPane().removeStatic(this.outputPopoutCable);
					this.getParentPane().remove(this.outputPopout);
				}
			}
		}

	}
	
	public void openFullView(){
		if(this.fullView == null){
			this.fullView = this.module.getPlugin().getFullView();
		}
		this.fullView.setVisible(true);
	}
	
	@Override
	public void updateView(){
		super.updateView();
		this.updateCablePoints();
	}
	@Override
	public void translateOriginLocation(Vector translation){
		super.translateOriginLocation(translation);
		this.inputPopout.translateOriginLocation(translation);
		this.outputPopout.translateOriginLocation(translation);
	}
	private void updateCablePoints(){
		if(this.isShowing()){
			inputPopupConnector.setXOnScreen((int) this.getLocationOnScreen().getX());
			inputPopupConnector.setYOnScreen((int) this.getLocationOnScreen().getY()+this.getHeight()/2);
			outputPopupConnector.setXOnScreen((int) (this.getLocationOnScreen().getX()+this.getWidth()));
			outputPopupConnector.setYOnScreen((int) this.getLocationOnScreen().getY()+this.getHeight()/2);
			if(!this.inputPopoutActive){
				Set<CablePointSimple> inputSet = this.inputMap.keySet();
				for(CablePointSimple cps: inputSet){
					cps.setXOnScreen(this.getLocationOnScreen().x + 5);
					cps.setYOnScreen(this.getLocationOnScreen().y + this.getHeight()/(1 + inputSet.size()));
				}
			}
			if(!this.outputPopoutActive){
				Set<CablePointSimple> outputSet = this.outputMap.keySet();
				for(CablePointSimple cps: outputSet){
					cps.setXOnScreen(this.getLocationOnScreen().x + this.getWidth() - 5);
					cps.setYOnScreen(this.getLocationOnScreen().y + this.getHeight()/(1 + outputSet.size()));
				}
			}
		}
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		this.updateCablePoints();
	}



	@Override
	public LinkedList<? extends CablePoint> getCablePoints() {
		LinkedList<CablePointSimple> tmp = new LinkedList<CablePointSimple>();
		tmp.addAll(this.inputMap.keySet());
		tmp.addAll(this.outputMap.keySet());
		return tmp;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<? extends CablePoint> getCablePoints(CablePointType type,
			int... indices) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CablePoint getCablePoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CablePoint getCablePoint(Point sourceInComponent) {
		Set<CablePointSimple> cpsSet;
		if(sourceInComponent.x < this.getWidth() / 2){
			cpsSet = this.inputMap.keySet();
		} else {
			cpsSet = this.outputMap.keySet();
		}
		CablePointSimple res = null;
		for(CablePointSimple cps: cpsSet) {
			if(!cps.isConnected()){
				res = cps;
				break;
			}
		}
		System.out.println(res);
		return res;
	}

	@Override
	public CablePoint getCablePoint(CablePointType type, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CablePoint getCablePoint(CablePointType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CablePoint getFreeCablePoint(CablePointType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean forceExistence(CablePoint... forceThis) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(CablePoint point) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	@Override
	public boolean close() {
		this.inputPopout(false, inputPopoutPermanent);
		this.outputPopout(false, outputPopoutPermanent);
		return true;
	}

	@Override
	public boolean reopen() {
			this.inputPopout(inputPopoutPermanent, inputPopoutPermanent);
			this.outputPopout(outputPopoutPermanent, outputPopoutPermanent);
		return true;
		
	}



	private class Popout extends InteractiveComponent implements CablePoint
	{
		public CablePointSimple connector;
		private CablePointType type;
		private InteractiveModule moduleDisplay;
		public Popout(InteractivePane parent,InteractiveModule moduleDisplay, Vector origin,CablePointType type) {
			super(parent, origin);
			this.moduleDisplay = moduleDisplay;
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.type = type;
			this.connector = new CablePointSimple(type);
		}
		
		public void updateIO(){
			this.removeAll();
			Set<CablePointSimple> cpsSet;
			if(type == CablePointType.INPUT){
				cpsSet = inputMap.keySet();
			} else {
				cpsSet = outputMap.keySet();
			}
			CablePointPanel p;
			for(CablePointSimple cps: cpsSet){
				p=new CablePointPanel(cps);
				if(cps.getIndex() % 2 == 0){
					p.setBackground(Color.LIGHT_GRAY);
				} else {
					p.setBackground(Color.DARK_GRAY);
				}
				this.add(p);
			}
			this.setOriginDimension(new Dimension(50,(cpsSet.size())*50-3));
		}
		
		@Override
		public boolean close(){
			if(this.type == CablePointType.INPUT){
				inputPopout(false, inputPopoutPermanent);
			} else if (this.type == CablePointType.OUTPUT){
				outputPopout(false, outputPopoutPermanent);
			}
			return false;
		}

		
		@Override
		public void updateView(){
			super.updateView();
			this.updateCablePoints();
			
		}
		
		@Override
		public void paint(Graphics g){
			super.paint(g);
			this.updateCablePoints();
		}
		
		private void updateCablePoints(){
			if(this.isShowing()){
				connector.setYOnScreen(this.getLocationOnScreen().y + this.getHeight()/2);
				if(connector.getType() == CablePointType.OUTPUT){
					connector.setXOnScreen(this.getLocationOnScreen().x);
				} else if(connector.getType() == CablePointType.INPUT){
					connector.setXOnScreen(this.getLocationOnScreen().x + this.getWidth());
				} 
				for(Component p:this.getComponents()){
					((CablePointPanel) p).updatePoint();
				}
			}
		}

		@Override
		public boolean reopen() {
			if(this.type == CablePointType.INPUT){
				inputPopout(inputPopoutPermanent, inputPopoutPermanent);
			} else if (this.type == CablePointType.OUTPUT){
				outputPopout(outputPopoutPermanent, outputPopoutPermanent);
			}
			return false;
		}

		@Override
		public int getXOnScreen() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getYOnScreen() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public InteractiveCable getCable() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCable(InteractiveCable cable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isConnected() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CablePointType getType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setHost(CablePointHost host) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public CablePointHost getHost() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void disconnect() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setIndex(int i) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getIndex() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public void setHovered(boolean set){
			super.setHovered(set);
			if(set){
				this.setBorder(new LineBorder(Color.BLACK));
			} else {
				this.setBorder(new EmptyBorder(1, 1, 1, 1));
				
			}
		}


		@Override
		public int compareTo(CablePoint o) {
			return this.getIndex() - o.getIndex();
		}



		
	}



	@Override
	public void changedState(CablePoint point) {
		if(point.isConnected()){
			CablePoint tmp = point.getCable().getSource();
			if(tmp == point){
				tmp = point.getCable().getDestination();
			} 
			CablePointHost p = tmp.getHost();
			if(p instanceof InteractiveModule){
				MidiIOThrough mit = ((InteractiveModule) p).getMidiIO(tmp);;
				System.out.println("CONNECT GRAPH" + mit);
				if(point.getType() == CablePointType.INPUT){
					this.inputMap.get(point).setInput(mit);
				} else {
					this.outputMap.get(point).setOutput(mit);
				}
			}
		} else {
			if(point.getType() == CablePointType.INPUT){
				this.inputMap.get(point).disconnectInput();
			} else {
				this.outputMap.get(point).disconnectOutput();
			}
		}
		
	}
}
