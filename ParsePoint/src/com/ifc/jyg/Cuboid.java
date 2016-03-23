package com.ifc.jyg;

import java.util.TreeSet; 

public class Cuboid {
	public final int  BEAM = 0;
	public final int  COLUMN = 1;
	public final int  SLAB = 2;
	
	TreeSet<CoordinateOfPoint>  treeSetCop = new TreeSet<CoordinateOfPoint>();
	public CoordinateOfPoint topTopLeft;
	public CoordinateOfPoint topTopRight;
	public CoordinateOfPoint topdownLeft;
	public CoordinateOfPoint topdownRight;
	public CoordinateOfPoint downTopLeft;
	public CoordinateOfPoint downTopRight;
	public CoordinateOfPoint downDownLeft;
	public CoordinateOfPoint downDownRight;
	
	private int type;
	private CoordinateOfPoint[] cuboidPoints;
	private String cuboidID = null;
	
	public Cuboid(String cuboidID, int pointNumber) {
		this.cuboidID = cuboidID;
		cuboidPoints = new CoordinateOfPoint[pointNumber];
	}
	
	
	public void setPoints(CoordinateOfPoint[] cuboidPoints) {
		this.cuboidPoints = cuboidPoints;
		treeSetCop.add
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
