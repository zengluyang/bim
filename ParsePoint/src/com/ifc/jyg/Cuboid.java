package com.ifc.jyg;

import java.util.Set;
import java.util.TreeSet; 
import java.util.Set; 

public class Cuboid {
	public final static int  BEAM = 0;
	public final static int  COLUMN = 1;
	public final static int  SLAB = 2;
	public final static String[] typeString = {"BEAM","COLUMN","SLAB"};
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
	private int cuboidID;
	
	public Cuboid(int cuboidID, int type) {
		this.cuboidID = cuboidID;
		this.type = type;
	}
	
	public void assignPoints() {
		int i=0;
		if(treeSetCop.size()!=8) {
			return ;
		}
		
		for(CoordinateOfPoint p:treeSetCop){
			switch (i) {
			case 0:
				this.downTopLeft = p;
				break;
			case 1:
				this.topTopLeft = p;
				break;
			case 2:
				this.downTopRight = p;
				break;
			case 3:
				this.topTopRight = p;
				break;
			case 4:
				this.downDownLeft = p;
				break;
			case 5:
				this.topdownLeft = p;
				break;
			case 6:
				this.downDownRight = p;
				break;
			case 7:
				this.topdownRight = p;
				break; 
			default:
				break;
			}
			i++;
		}
	}
	 
	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		sb.append("Cuboid ").append(this.getCuboidID()).append(' ')
		.append(Cuboid.typeString[this.getType()]).append("\n[\n")
		.append("\ttopTopLeft \t").append(this.topTopLeft.toString()).append('\n')
		.append("\ttopTopRight \t").append(this.topTopRight.toString()).append('\n')
		.append("\ttopdownLeft \t").append(this.topdownLeft.toString()).append('\n')
		.append("\ttopdownRight \t").append(this.topdownRight.toString()).append('\n')
		.append("\tdownTopLeft \t").append(this.downTopLeft.toString()).append('\n')
		.append("\tdownTopRight \t").append(this.downTopRight.toString()).append('\n')
		.append("\tdownDownLeft \t").append(this.downDownLeft.toString()).append('\n')
		.append("\tdownDownRight \t").append(this.downDownRight.toString()).append("\n]\n");
		return sb.toString();
	}

	public void addPoint(CoordinateOfPoint p) {
		treeSetCop.add(p); 
	}
	
	public TreeSet<CoordinateOfPoint> getPoint() {
		return treeSetCop;
	}
	 
	public void setCuboidID(int cuboidID) {
		this.cuboidID = cuboidID;
	}
	 
	public int getCuboidID() {
		return cuboidID;
	} 
	public int getType() {
		return type;
	}
 
	public void setType(int type) {
		this.type = type;
	}
	
	public Set<Rectangle> getNeededRectangels() {
		TreeSet<Rectangle> rlt = null;
		switch (this.getType()) {
		case BEAM:
		{
			rlt = new TreeSet<Rectangle>();
			Rectangle recBottom = new Rectangle(this.downTopLeft,this.downDownRight,Rectangle.FRONT_BOOTOM); 
			Rectangle recLeft = new Rectangle(this.topdownLeft,this.downTopLeft,Rectangle.LEFT_RIGHT); 
			Rectangle recRight = new Rectangle(this.topdownLeft,this.downTopRight,Rectangle.LEFT_RIGHT);
			rlt.add(recBottom);
			rlt.add(recLeft);
			rlt.add(recRight);
			break;
		}
		case COLUMN:
		{
			rlt = new TreeSet<Rectangle>();
			Rectangle recFront = new Rectangle(this.topdownLeft,this.downDownRight,Rectangle.FRONT_BOOTOM); 
			Rectangle recBottom = new Rectangle(this.topTopLeft,this.downTopRight, Rectangle.FRONT_BOOTOM); 
			Rectangle recRight = new Rectangle(this.topdownRight,this.downTopRight, Rectangle.FRONT_BOOTOM);
			Rectangle recLeft= new Rectangle(this.topdownLeft,this.downTopLeft,Rectangle.LEFT_RIGHT);
			rlt.add(recFront);
			rlt.add(recBottom);
			rlt.add(recLeft);
			rlt.add(recRight);
			break;
		}
		case SLAB:
			
			break;

		default:
			break;
		}
		return rlt;
	}
}
