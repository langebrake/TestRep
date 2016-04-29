package gui.interactivepane;

import java.awt.Point;

public enum CablePointType {
		INPUT,
		OUTPUT,
		THROUGH,
		UNKNOWN;
		
		
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
		
		public boolean compatibleWith(CablePointType type){
			switch(this){
				case INPUT:
					if(type == OUTPUT || type == THROUGH)
						return true;
				case OUTPUT:
					if(type == INPUT || type == THROUGH)
						return true;
				case THROUGH:
					if(type == INPUT || type == OUTPUT || type == THROUGH)
						return true;
				case UNKNOWN:
					return false;
				default:
					return false;
			
			}
		}
}
