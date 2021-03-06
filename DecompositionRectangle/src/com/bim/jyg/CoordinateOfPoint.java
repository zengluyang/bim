package com.bim.jyg;

public class CoordinateOfPoint implements Comparable<Object>{

	private double x;
	private double y;
	private double z;
	
	private String location;
	
	public CoordinateOfPoint() {
		
	}

	public CoordinateOfPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

	}

	public void setX(double x) {
		this.x = x;
	} 
	 
	public double getX() {
		return x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getY() {
		return y;
	}
	 
	public void setZ(double z) {
		this.z = z;
	}
	
	public double getZ() {
		return z;
	}
	
	public void setDescribe(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	} 
	 
	public String parseLocation(CoordinateOfPoint point) {
		
		if (x == point.getX() && y == point.getY() && z == point.getZ()) {
			location = "topDownRight";			
		} else if (x == point.getX() && y == point.getY() && z < point.getZ()) {
			location = "downDownRight";
		} else if (x == point.getX() && y < point.getY() && z == point.getZ()) {
			location = "topDownLeft";
		} else if (x == point.getX() && y < point.getY() && z < point.getZ()) {
			location = "downDownLeft";
		} else if (x < point.getX() && y == point.getY() && z == point.getZ()) {
			location = "topTopRight";
		} else if (x < point.getX() && y == point.getY() && z < point.getZ()) {
			location = "downTopRight";
		} else if (x < point.getX() && y < point.getY() && z == point.getZ()) {
			location = "topTopLeft";
		} else if (x < point.getX() && y < point.getY() && z < point.getZ()) {
			location = "downTopLeft";
		} 
		setDescribe(location);
		return location;
	}

	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		sb.append("E ").append(x).append(" ").append(y).append(" ").append(z);
		return sb.toString();
	}
 
	@Override
	public int compareTo(Object o) {
		
		if (this == o) {
			return 0;
		} else if (o != null && o instanceof CoordinateOfPoint) {
			//System.out.println("o != null && o instanceof CoordinateOfPoint");
			CoordinateOfPoint point = (CoordinateOfPoint) o;
			if(this.getX() < point.getX())  {
				return -1;
			}
			if(this.getX() == point.getX() ) {
				if(this.getY() < point.getY()) {
					//System.out.println("this.getY() < this.getY()");
					return -1;
				}
				if(this.getY() == point.getY()) {
					//System.out.println("this.getY() == point.getY()");
					if(this.getZ() < point.getZ()) {
						//System.out.println("this.getZ() < point.getZ()");
						return -1;
					}
					if(this.getZ() == point.getZ()) {
						return 0;
					}
				}
			} 
		} 
		return 1; 
	}
}
