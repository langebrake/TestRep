package controller.history.actions;

import java.util.HashMap;

import gui.interactivepane.CablePoint;
import gui.interactivepane.InteractiveCable;
import controller.history.UserAction;
import controller.history.UserActionManager;

public class UserAddConnectionsAction extends UserAction {
	
	private HashMap<InteractiveCable,InteractiveCable[]> cables;
	/**
	 * The Hashmap Keys are the new added Cables, Values are the old replaced Cables
	 * @param UserActionManager m
	 * @param Calbe Map cables
	 */
	public UserAddConnectionsAction(UserActionManager m, HashMap<InteractiveCable,InteractiveCable[]> cables) {
		super(m);
		this.cables = cables;
	}

	
	//TODO: Something isn't right yet! Seems like the cablepoint connection status is not always correct!
	//it once was possible to draw 2 lines from 1 point!
	 
	
	@Override
	public void undo() {
		for(InteractiveCable c:cables.keySet()){
			InteractiveCable[] overrides = cables.get(c);
			if(overrides != null){
				//TODO: reconnect old cable, mind the module connection!
				for(InteractiveCable override:overrides){
					for(CablePoint p:override.getCablePoints()){
						p.setCable(override);
					}
					controller.getPane().add(override);
				}
			}
			//TODO: connect new cable and cableendpoints, mind the module connection!
			controller.getPane().remove(c);
			for(CablePoint p:c.getCablePoints()){
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
