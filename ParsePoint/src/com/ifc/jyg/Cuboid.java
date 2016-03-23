package com.ifc.jyg;

 

public class Cuboid {
	public final int  BEAM = 0;
	public final int  COLUMN = 1;
	public final int  SLAB = 2;
	
	
	private int type;
	private CoordinateOfPoint[] cuboidPoints;
	private String cuboidID = null;
	
	public Cuboid(String cuboidID, int pointNumber) {
		this.cuboidID = cuboidID;
		cuboidPoints = new CoordinateOfPoint[pointNumber];
	}
	
	
	public void setPoints(CoordinateOfPoint[] cuboidPoints) {
		this.cuboidPoints = cuboidPoints;
	}
	
	public CoordinateOfPoint[] getPoint() {
		return cuboidPoints;
	}
	
	public void setCuboidID(String cuboidID) {
		this.cuboidID = cuboidID;
	}
	
	
	
	public String getCuboidID() {
		return cuboidID;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	

}
