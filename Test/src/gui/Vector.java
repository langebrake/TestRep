package gui;

import java.awt.Point;

public class Vector {
	private Point point;

	public Vector(){
		this(new Point(0,0));
	}
	
	public Vector(int x, int y){
		this(new Point(x,y));
	}
	
	public Vector(Point point){
		this.point=point;
	}
	
	/**
	 * Calculate differentiation vector
	 * @param other Vector
	 * @return Differentiation vector from this to other
	 */
	public Vector diffVector(Vector other){
		return new Vector(this.point.x-other.point.x, this.point.y-other.point.y);
	}
	
	/**
	 * @param other Vector
	 * @return Vector addition result
	 */
	public Vector addVector(Vector other){
		return new Vector(this.point.x+other.point.x, this.point.y+other.point.y);
	}
	
	
	/**
	 * Scale this vector by a factor
	 * @param scaleFactor
	 * @return scaled vector
	 */
	public Vector scaleVector(float scaleFactor){
		return new Vector((int)(this.point.x*scaleFactor),(int)(this.point.y*scaleFactor));
	}
	
	/**
	 * 
	 * @return
	 */
	public int getX(){
		return this.point.x;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getY(){
		return this.point.y;
	}
	
	/**
	 * convert vector to point
	 * @return point
	 */
	public Point toPoint(){
		return this.point;
	}
}
