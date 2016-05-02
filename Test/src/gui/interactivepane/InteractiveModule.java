package gui.interactivepane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.interactivepane.InteractiveController;
import model.graph.Module;

public class InteractiveModule extends InteractiveComponent implements CablePointHost {
	
	
	private Module module;
	private JFrame fullView;
	private JComponent contentPane;
	private boolean inputPopoutActive,outputPopoutActive;
	private Popout inputPopout,outputPopout;
	private CablePointSimple inputPopupConnector,outputPopupConnector;
	private LinkedList<CablePointSimple> inputCablePoints,outputCablePoints;
	private InteractiveCable inputPopoutCable,outputPopoutCable;
	private InteractiveController controller;
	
	public InteractiveModule(InteractivePane parent, Vector origin, Module module,InteractiveController controller) {
		super(parent, origin);
		this.controller = controller;
		this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		super.setLayout(new BorderLayout());
		this.contentPane = module.getPlugin().getMinimizedView();
		this.setOriginDimension(contentPane.getSize());
		this.add(contentPane,BorderLayout.CENTER);
		this.module = module;
		inputPopupConnector = new CablePointSimple(CablePointType.INPUT);
		outputPopupConnector = new CablePointSimple(CablePointType.OUTPUT);
		this.inputPopout = new Popout(this.getParentPane(),this,
				this.getOriginLocation().addVector(new Vector(-100,0)),
				CablePointType.INPUT);
		this.outputPopout = new Popout(this.getParentPane(),this,
				this.getOriginLocation().addVector(new Vector(100+this.getOriginDimension().width,0)),
				CablePointType.OUTPUT);
		inputPopout.addListeners(controller.getModuleListener(),controller.getCableCreationListener());
		outputPopout.addListeners(controller.getModuleListener(),controller.getCableCreationListener());
		inputPopoutCable = new InteractiveCable(inputPopout.connector, inputPopupConnector, 2, Color.BLUE, parent);
		outputPopoutCable = new InteractiveCable(outputPopout.connector, outputPopupConnector, 2, Color.MAGENTA, parent);
		
		this.inputCablePoints = new LinkedList<CablePointSimple>();
		this.outputCablePoints = new LinkedList<CablePointSimple>();
		this.updateView();
		
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
	
	public void inputPopout(boolean set){
		if(set){
			
			this.getParentPane().add(this.inputPopout);
			this.getParentPane().addStatic(this.inputPopoutCable);
		} else {
			this.getParentPane().removeStatic(this.inputPopoutCable);
			this.getParentPane().remove(this.inputPopout);
			
		}
	}
	
	public void outputPopout(boolean set){
		if(set){
			this.getParentPane().add(this.outputPopout);
			this.getParentPane().addStatic(this.outputPopoutCable);
		} else {
			this.getParentPane().removeStatic(this.outputPopoutCable);
			this.getParentPane().remove(this.outputPopout);

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
		}
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		this.updateCablePoints();
	}



	@Override
	public LinkedList<? extends CablePoint> getCablePoints() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
		this.inputPopout(false);
		this.outputPopout(false);
		return true;
	}

	@Override
	public boolean reopen() {
	
		
		return true;
		
	}



	private class Popout extends InteractiveComponent implements CablePoint
	{
		public CablePointSimple connector;
		private CablePointType type;
		private InteractiveModule moduleDisplay;
		private LinkedList<CablePointPanel> cablePointPanels;
		public Popout(InteractivePane parent,InteractiveModule moduleDisplay, Vector origin,CablePointType type) {
			super(parent, origin);
			this.moduleDisplay = moduleDisplay;
			this.cablePointPanels = new LinkedList<CablePointPanel>();
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			int pointcount = 0;
			CablePoint parentConnector = null;
			this.type = type;
			if(type == CablePointType.INPUT){
				parentConnector = inputPopupConnector;
				pointcount = module.getInputCount();
				
			} else if (type == CablePointType.OUTPUT){
				parentConnector = outputPopupConnector;
				pointcount = module.getOutputCount();
			}
			CablePointPanel p;
			for(int i = 1; i<=pointcount+3;i++){
				p=new CablePointPanel(new CablePointSimple(type));
				p.setBackground(Color.LIGHT_GRAY);
				cablePointPanels.add(p);
				this.add(p);
			}
			this.setOriginDimension(new Dimension(50,(3+pointcount)*50));
			connector = new CablePointSimple(type);

			
		}
		

		@Override
		public boolean close(){
			if(this.type == CablePointType.INPUT){
				inputPopout(false);
			} else if (this.type == CablePointType.OUTPUT){
				outputPopout(false);
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
				for(CablePointPanel p:this.cablePointPanels){
					p.updatePoint();
				}
			}
		}

		@Override
		public boolean reopen() {
			if(this.type == CablePointType.INPUT){
				inputPopout(true);
			} else if (this.type == CablePointType.OUTPUT){
				outputPopout(true);
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



		
	}
}
