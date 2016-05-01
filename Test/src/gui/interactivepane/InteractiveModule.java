package gui.interactivepane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;

import model.graph.Module;

public class InteractiveModule extends InteractiveComponent implements CablePointHost {
	private Module module;
	private JFrame fullView;
	private JComponent contentPane;
	private boolean inputPopoutActive,outputPopoutActive;
	private Popout inputPopout,outputPopout;
	private CablePointSimple inputPopupConnector,outputPopupConnector;
	public InteractiveModule(InteractivePane parent, Vector origin, Module module) {
		super(parent, origin);
		this.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
		super.setLayout(new BorderLayout());
		this.contentPane = module.getPlugin().getMinimizedView();
		this.setOriginDimension(contentPane.getSize());
		this.add(contentPane,BorderLayout.CENTER);
		this.module = module;
		inputPopupConnector = new CablePointSimple(CablePointType.INPUT);
		outputPopupConnector = new CablePointSimple(CablePointType.OUTPUT);
		this.inputPopout = new Popout(this.getParentPane(),
				this.getOriginLocation().addVector(new Vector(-100,0)),
				CablePointType.INPUT);
		this.outputPopout = new Popout(this.getParentPane(),
				this.getOriginLocation().addVector(new Vector(100+this.getOriginDimension().width,0)),
				CablePointType.OUTPUT);
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
			inputPopout.open();
		} else {
			inputPopout.close();
			this.getParentPane().remove(this.inputPopout);
			
		}
	}
	
	public void outputPopout(boolean set){
		if(set){
			this.getParentPane().add(this.outputPopout);
			outputPopout.open();
		} else {
			outputPopout.close();
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

	private class Popout extends InteractiveComponent
	{
		private CablePointSimple connector;
		private InteractiveCable connectorLine;
		public Popout(InteractivePane parent, Vector origin,CablePointType type) {
			super(parent, origin);
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			int pointcount = 0;
			CablePoint parentConnector = null;
			if(type == CablePointType.INPUT){
				parentConnector = inputPopupConnector;
				pointcount = module.getInputCount();
				
			} else if (type == CablePointType.OUTPUT){
				parentConnector = outputPopupConnector;
				pointcount = module.getOutputCount();
			}
			CablePointPanel p;
			for(int i = 1; i<=pointcount;i++){
				p=new CablePointPanel(this, type);
				p.setBackground(Color.LIGHT_GRAY);
				this.add(p);
			}
			this.setOriginDimension(new Dimension(50,pointcount*50));
			connector = new CablePointSimple(type);
			connectorLine = new InteractiveCable(connector, parentConnector, parent);

			
		}
		
		public void open(){
			this.updateCablePoints();
			this.getParentPane().add(connectorLine);
			this.updateView();
		}
		public void close(){
			this.getParentPane().remove(connectorLine);
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
			}
		}
		
	}
}
