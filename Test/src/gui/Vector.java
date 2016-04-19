package gui;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Vector {
	private Point2D.Float point;

	public Vector(){
		this(new Point2D.Float(0,0));
	}
	
	public Vector(int x, int y){
		this(new Point2D.Float(x,y));
	}
	
	public Vector(float x, float y){
		this(new Point2D.Float(x, y));
	}
	
	public Vector(Point2D.Float point){
		this.point=point;
	}
	
	public Vector(Point p){
		this.point = new Point2D.Float(p.x,p.y);
	}
	
	/**
	 * Calculate differentiation vector
	 * @param other Vector
	 * @return Differentiation vector from this to other
	 */
	public Vector diffVector(Vector other){
		return new Vector(other.point.x-this.point.x, other.point.y-this.point.y);
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
		return new Vector((this.point.x*scaleFactor),(this.point.y*scaleFactor));
	}
	
	/**
	 * 
	 * @return
	 */
	public int getX(){
		return Math.round(this.point.x);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getY(){
		return Math.round(this.point.y);
	}
	
	/**
	 * convert vector to point
	 * @return point
	 */
	public Point toPoint(){
		return new Point(Math.round(this.point.x),Math.round(this.point.y));
	}
	
	public String toString(){
		return this.point.toString();
	}
}
