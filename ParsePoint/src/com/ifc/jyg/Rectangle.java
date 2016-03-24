package com.ifc.jyg;


public class Rectangle implements Comparable<Object> {
	
	public final static int  FRONT_BOOTOM = 0;
	public final static int  LEFT_RIGHT = 1;
	public final static int  UP_DOWN = 2;
	public final static String[] directionString = {"FRONT_BOOTOM","LEFT_RIGHT","UP_DOWN"}; 
	
	public CoordinateOfPoint topLeft;
	public CoordinateOfPoint downRight;
	private int direction = 0;
	private double area = 0.0;
	
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

	public int getDirection() {
		return direction;
	}
	
	public double getArea() {
		double length = 0.0;
		double width = 0.0;
		switch (direction) {
		case 0:
			length = Math.abs(topLeft.getY() - downRight.getY());
			width = Math.abs(topLeft.getZ() - downRight.getZ());
			area = length * width;
			break;
		case 1:
			length = Math.abs(topLeft.getX()- downRight.getX());
			width = Math.abs(topLeft.getZ() - downRight.getZ());
			area = length * width;
			break;
		case 2:
			length = Math.abs(topLeft.getY() - downRight.getY());
			width = Math.abs(topLeft.getX() - downRight.getX());
			area = length * width;
			break;
		default:
			break;
		}
		return area;
	}
	/*
	public boolean compareArea(Object other) {
		if (this.) {
			
		}
		
		return false;
	}
	*/
	public double getIntersectvalue() {
		double value = 0.0;
		switch (direction) {
		case 0:
			value = topLeft.getX();
			break;
		case 1:
			value = topLeft.getY();
			break;
		case 2:
			value = topLeft.getZ();
			break;
		default:
			break;
		}
		return value;
	}
	
	@Override
	public int compareTo(Object o) {
		if(this == o) {
			return 0;
		} else if (o != null && o instanceof Rectangle) {
			Rectangle rec = (Rectangle) o;
			if (this.getArea() < rec.getArea()) {
				return -1;
			} else if (this.getArea() == rec.getArea()) {
				if(topLeft.compareTo(rec) < 0) {
					return -1;
				} else if(topLeft.compareTo(rec) == 0) {
					return downRight.compareTo(rec);
				} 
			} 
			
		}
		return 1;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rectangle ") 
		.append(Rectangle.directionString[this.direction]).append("\n")
		.append("\ttopLeft \t").append(this.topLeft.toString()).append('\n')
		.append("\tdownRight \t").append(this.downRight.toString()).append('\n');
 		return sb.toString();
	} 
}
