package com.ifc.jyg;

public class Rectangle implements Comparable<Object>{
	public final static int  FRONT_BOOTOM = 0;
	public final static int  LEFT_RIGHT = 1;
	public final static int  UP_DOWN = 2;
	public final static String[] directionString = {"FRONT_BOOTOM","LEFT_RIGHT","UP_DOWN"};
	
	
	
	public CoordinateOfPoint topLeft;
	public CoordinateOfPoint downRight;
	public int direction = 0;
	
	public Rectangle(CoordinateOfPoint topLeft, CoordinateOfPoint downRight) {
		super();
		this.topLeft = topLeft;
		this.downRight = downRight;
	}
	

	public Rectangle(CoordinateOfPoint topLeft,
			CoordinateOfPoint downRight,int direction) {
		super();
		this.direction = direction;
		this.topLeft = topLeft;
		this.downRight = downRight;
	}


	@Override
	public int compareTo(Object o) {
		if(this==o) {
			return 0;
		} else if (o != null && o instanceof Rectangle) {
			Rectangle rec = (Rectangle)o;
			if(topLeft.compareTo(o)<0) {
				return -1;
			}
			if(topLeft.compareTo(o)==0) {
				return downRight.compareTo(o);
			}
		}
		return 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rectangle ")//.append(this.getCuboidID()).append(' ')
		.append(Rectangle.directionString[this.direction]).append("\n")
		.append("\ttopLeft \t").append(this.topLeft.toString()).append('\n')
		.append("\tdownRight \t").append(this.downRight.toString()).append('\n');
 		return sb.toString();
	}
	
	
}
