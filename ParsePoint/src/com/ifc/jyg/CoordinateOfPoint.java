package com.ifc.jyg;

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
	/*
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
	*/
	
	
	 
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
		sb.append("Point (").append(x).append(",").append(y).append(",").append(z).append(") "); 
		return sb.toString();
	}

	public String toMatlab() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(x).append(",").append(y).append(",").append(z).append("]");
		return sb.toString();
	}

	public String toMatlab2D (int direction) {
		StringBuilder sb = new StringBuilder();
		sb.append("point").append(this.hashCode()).append("=");
		switch (direction) {
			case Polygon.FRONT_BOOTOM:
				sb.append("[").append(y).append(",").append(z).append("]");
				break;
			case Polygon.LEFT_RIGHT:
				sb.append("[").append(x).append(",").append(z).append("]");
				break;
			case Polygon.UP_DOWN:
				sb.append("[").append(x).append(",").append(y).append("]");
				break;
		}
		sb.append(";");
		return sb.toString();
	}

	public String toMatlab2DVarName() {
		StringBuilder sb = new StringBuilder();
		sb.append("point").append(this.hashCode());
		return sb.toString();
	}

	public double getX2d(int direction) {
		switch (direction) {
			case Polygon.FRONT_BOOTOM:
				return this.y;
			case Polygon.LEFT_RIGHT:
				return this.x;
			case Polygon.UP_DOWN:
				return this.x;
		}
		return 0.0;
	}

	public double getY2d(int direction) {
		switch (direction) {
			case Polygon.FRONT_BOOTOM:
				return this.z;
			case Polygon.LEFT_RIGHT:
				return this.z;
			case Polygon.UP_DOWN:
				return this.y;
		}
		return 0.0;
	}


	static {
		//testtoMatlab2D();
	}

	public static void testtoMatlab2D() {
		CoordinateOfPoint point = new CoordinateOfPoint(5,5,5);
		System.out.println("testtoMatlab2D "+point.toMatlab2D(Polygon.UP_DOWN));
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
