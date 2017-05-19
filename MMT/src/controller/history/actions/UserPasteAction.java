package controller.history.actions;

import gui.interactivepane.CablePoint;
import gui.interactivepane.InteractiveCable;
import gui.interactivepane.InteractiveComponent;
import gui.interactivepane.InteractiveModule;
import gui.interactivepane.Vector;

import java.util.HashMap;
import java.util.LinkedList;

import controller.history.UserAction;
import controller.interactivepane.InteractiveController;

public class UserPasteAction extends UserAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4138976856613389145L;
	private LinkedList<InteractiveComponent> components;
	private LinkedList<InteractiveCable> cables;
	private Vector offset;
	private LinkedList<InteractiveComponent> clipboard;
	private LinkedList<InteractiveCable> cableClipboard;

	public UserPasteAction(InteractiveController controller,
			LinkedList<InteractiveComponent> clipboard,
			LinkedList<InteractiveCable> cableClipboard, Vector offset) {
		super(controller);
		this.clipboard = clipboard;
		this.components = new LinkedList<InteractiveComponent>();
		cables = new LinkedList<InteractiveCable>();
		this.offset = offset;
		this.cableClipboard = cableClipboard;
		this.populate();

	}

	@Override
	public void undo() {
		for (InteractiveCable c : cables) {
			controller.getPane().remove(c);
		}
		for (InteractiveComponent c : components) {
			c.close();
			controller.remove(c);
		}

	}

	private boolean firstTime = true;

	@Override
	public void execute() {
		this.controller.clearSelection();
		for (InteractiveComponent c : components) {
			if (!firstTime) {
				c.reopen();
			}
			controller.add(c);
			controller.selectComponent(c, true);
		}
		if (firstTime)
			firstTime = false;
		for (InteractiveCable c : cables) {
			controller.getPane().add(c);
		}

	}

	private void populate() {
		HashMap<InteractiveModule, InteractiveModule> modMap = new HashMap<InteractiveModule, InteractiveModule>();
		for (InteractiveComponent mod : clipboard) {
			if (mod instanceof InteractiveModule) {
				InteractiveModule cMod = ((InteractiveModule) mod).cloneTo(
						offset, controller);
				modMap.put((InteractiveModule) mod, cMod);
				this.components.add(cMod);
			}
		}
		;

		this.cables = new LinkedList<InteractiveCable>();

		for (InteractiveCable cable : this.cableClipboard) {

			CablePoint source = cable.getSource();
			CablePoint dest = cable.getDestination();
			InteractiveModule sourceMapMod = modMap.get(source.getHost());
			InteractiveModule destMapMod = modMap.get(dest.getHost());
			CablePoint cSource = sourceMapMod.getCablePoint(source.getType(),
					source.getIndex());
			CablePoint cDest = destMapMod.getCablePoint(dest.getType(),
					dest.getIndex());
			InteractiveCable cCable = new InteractiveCable(cSource, cDest,
					controller);
			cSource.setCable(cCable);
			cDest.setCable(cCable);
			this.cables.add(cCable);
		}
		;
	}

}
