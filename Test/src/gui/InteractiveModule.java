package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import model.graph.Module;

public class InteractiveModule extends InteractiveComponent {
	private Module module;
	private JFrame fullView;
	private JComponent contentPane;
	public InteractiveModule(InteractivePane parent, Vector origin, Module module) {
		super(parent, origin);
		super.setLayout(new BorderLayout());
		this.contentPane = module.getPlugin().getMinimizedView();
		this.setOriginDimension(contentPane.getSize());
		this.add(contentPane,BorderLayout.CENTER);
		this.module = module;
		
	}
	
	public Module getModule(){
		return this.module;
	}
	
	@Override
	public void setHover(boolean set){
		super.setHover(set);
		if(set)
			this.setBorder(BorderFactory.createLineBorder(Color.black));
		else {
			if(this.isSelected()){
				this.setBorder(BorderFactory.createLineBorder(Color.red));
			} else {
				this.setBorder(BorderFactory.createEmptyBorder());
			}
		}	
	}
	
	@Override
	public void setSelected(boolean set){
		super.setSelected(set);
		if(set)
			this.setBorder(BorderFactory.createLineBorder(Color.red));
		else
			this.setBorder(BorderFactory.createEmptyBorder());
	}
	
	public void inputPopout(boolean set){
		this.getParentPane();
	}
	
	public void outputPopout(boolean set){
		
	}
	
	public void openFullView(){
		if(this.fullView == null){
			this.fullView = this.module.getPlugin().getFullView();
		}
		this.fullView.setVisible(true);
	}


}
