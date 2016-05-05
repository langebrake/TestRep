package controller.history.actions;

import java.util.HashMap;
import java.util.LinkedList;

import gui.interactivepane.CablePoint;
import gui.interactivepane.InteractiveCable;
import controller.history.UserAction;
import controller.history.UserActionManager;
import controller.interactivepane.InteractiveController;

public class UserAddConnectionsAction extends UserAction {
	
	private HashMap<InteractiveCable,InteractiveCable[]> cables;
	/**
	 * The Hashmap Keys are the new added Cables, Values are the old replaced Cables
	 * @param UserActionManager m
	 * @param Calbe Map cables
	 */
	public UserAddConnectionsAction(InteractiveController sourceController, HashMap<InteractiveCable,InteractiveCable[]> cables) {
		super(sourceController);
		this.cables = cables;
	}

	

	 
	
	@Override
	public void undo() {
		for(InteractiveCable c:cables.keySet()){
			LinkedList<CablePoint> toDisconnect = c.getCablePoints();
			InteractiveCable[] overrides = cables.get(c);
			if(overrides != null){
				//TODO: reconnect old cable, mind the module connection!
				for(InteractiveCable override:overrides){
					for(CablePoint p:override.getCablePoints()){
						p.setCable(override);
						toDisconnect.remove(p);
					}
					controller.getPane().add(override);
				}
			}
			//TODO: connect new cable and cableendpoints, mind the module connection!
			controller.getPane().remove(c);
			for(CablePoint p:toDisconnect){
				p.disconnect();
			}
			
		}
		controller.getPane().repaint();

	}

	@Override
	public void execute() {
		for(InteractiveCable c:cables.keySet()){
			InteractiveCable[] overrides = cables.get(c);
			if(overrides != null){
				//TODO: disconnect old cable, mind the module connection!
				for(InteractiveCable override:overrides){
					controller.getPane().remove(override);
					for(CablePoint p:override.getCablePoints()){
						
						p.disconnect();
					}
				}
			}
			//TODO: connect new cable and cableendpoints, mind the module connection!
			for(CablePoint p:c.getCablePoints()){
				p.setCable(c);
			}
			controller.getPane().add(c);
		}
		controller.getPane().repaint();

	}

}
