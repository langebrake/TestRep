package gui.interactivepane;

import java.awt.Point;

public enum CablePointType {
		INPUT,
		OUTPUT,
		THROUGH,
		UNKNOWN;
		
		private Point position;
		
		CablePointType(){
			this.position = null;
		}
		
		CablePointType(Point position){
			this.position = position;
		}
		
		public Point getPosition(){
			return this.position;
		}
		
		public CablePointType getCounterPart(){
			switch(this){
			case INPUT:
				return OUTPUT;
			case OUTPUT:
				return INPUT;
			case THROUGH:
				return THROUGH;
			default:
				return UNKNOWN;
			}
		}
}
