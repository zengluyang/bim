package com.ifc.jyg;

public class CoordinateOfPoint {

	private double x;
	private double y;
	private double z;
	
	private String location;
	
	public CoordinateOfPoint() {
		
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

	public int compareTo(CoordinateOfPoint o) {
		if(this.getX() < o.getX())  {
			return -1;
		}
		if(this.getX() == o.getX() ) {
			if(this.getY() < this.getY()) {
				return -1;
			}
			if(this.getY() == o.getY()) {
				if(this.getZ() < o.getZ()) {
					return -1;
				}
				if(this.getZ() == o.getZ()) {
					return 0;
				}
			}
		}
		return 1;

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
}
