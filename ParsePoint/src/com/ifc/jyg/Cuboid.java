package com.ifc.jyg;

import java.util.Set;
import java.util.TreeSet;

public class Cuboid {
	public final static int BEAM = 0;
	public final static int COLUMN = 1;
	public final static int CUBOIDSLAB = 2;
	public final static int POLYSLAB = 3;
	public final static String[] typeString = {"BEAM","COLUMN","CUBOIDSLAB", "POLYSLAB"};
	
	private CoordinateOfPoint topTopLeft;
	private CoordinateOfPoint topTopRight;
	private CoordinateOfPoint topDownLeft;
	private CoordinateOfPoint topDownRight;
	private CoordinateOfPoint downTopLeft;
	private CoordinateOfPoint downTopRight;
	private CoordinateOfPoint downDownLeft;
	private CoordinateOfPoint downDownRight;
	
	private int type; 
	private String cuboidID;
	
	private TreeSet<CoordinateOfPoint>  treeSetCop = new TreeSet<CoordinateOfPoint>();
	
	public Cuboid(String cuboidID, int type) {
		this.cuboidID = cuboidID;
		this.type = type;
	}
	
	public void assignPoints() {
		int i = 0;
		if(treeSetCop.size() != 8) { 
			return ;
		}
		
		for(CoordinateOfPoint p : treeSetCop){
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
				this.topDownLeft = p;
				break;
			case 6:
				this.downDownRight = p;
				break;
			case 7:
				this.topDownRight = p;
				break; 
			default:
				break;
			}
			i++;
		}
	}
	
	public void addPoint(CoordinateOfPoint p) {
		treeSetCop.add(p); 
	}
	
	public TreeSet<CoordinateOfPoint> getPoint() {
		return treeSetCop;
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
	
	public Set<Rectangle> getNeededRectangels() {
		TreeSet<Rectangle> rlt = null;
		switch (this.getType()) {
		case BEAM:
		{
			rlt = new TreeSet<Rectangle>();
			Rectangle recDown 		= new Rectangle(this.downTopLeft,this.downDownRight,	this.cuboidID);
			Rectangle recLeft 		= new Rectangle(this.topDownLeft,this.downTopLeft,		this.cuboidID);
			Rectangle recRight 		= new Rectangle(this.topDownRight,this.downTopRight,	this.cuboidID);
			Rectangle recFront 		= new Rectangle(this.topDownLeft,this.downDownRight,	this.cuboidID);
			Rectangle recBottom 	= new Rectangle(this.topTopLeft,this.downTopRight,		this.cuboidID);
			rlt.add(recBottom);
			rlt.add(recLeft);
			rlt.add(recRight);
			rlt.add(recDown);
			rlt.add(recFront); 
			break;
		}
		case COLUMN:
		{
			rlt = new TreeSet<Rectangle>();
			Rectangle recFront 		= new Rectangle(this.topDownLeft,	this.downDownRight,	this.cuboidID);
			Rectangle recBottom 	= new Rectangle(this.topTopLeft,	this.downTopRight,	this.cuboidID);
			Rectangle recRight 		= new Rectangle(this.topDownRight,	this.downTopRight,	this.cuboidID);
			Rectangle recLeft		= new Rectangle(this.topDownLeft,	this.downTopLeft,	this.cuboidID);
			rlt.add(recFront);
			rlt.add(recBottom);
			rlt.add(recLeft);
			rlt.add(recRight);
			
			break;
		}
		case CUBOIDSLAB:
		{
			rlt = new TreeSet<Rectangle>();
			Rectangle rectFront 	= new Rectangle(this.topDownLeft,	this.downDownRight,	this.cuboidID);
			Rectangle rectBottom 	= new Rectangle(this.topTopLeft,	this.downTopRight,	this.cuboidID);
			Rectangle rectLeft 		= new Rectangle(this.topDownLeft,	this.downTopLeft,	this.cuboidID);
			Rectangle rectRight 	= new Rectangle(this.topDownRight, 	this.downTopRight,	this.cuboidID);
			Rectangle rectUp 		= new Rectangle(topTopLeft, 		topDownRight,		this.cuboidID);
			Rectangle rectDown 		= new Rectangle(downTopLeft, 		downDownRight,		this.cuboidID);
			
			rlt.add(rectFront);
			rlt.add(rectBottom);
			rlt.add(rectLeft);
			rlt.add(rectRight);
			rlt.add(rectUp);
			rlt.add(rectDown);
			break;
		}
		case POLYSLAB: 
		{
			rlt = new TreeSet<Rectangle>();
			Rectangle rectFront 	= new Rectangle(this.topDownLeft, 	this.downDownRight);
			Rectangle rectBottom 	= new Rectangle(this.topTopLeft, 	this.downTopRight);
			Rectangle rectLeft		= new Rectangle(this.topDownLeft, 	this.downTopLeft);
			Rectangle rectRight 	= new Rectangle(this.topDownRight, 	this.downTopRight);
			Rectangle rectUp 		= new Rectangle(topTopLeft, 		topDownRight);
			Rectangle rectDown		= new Rectangle(downTopLeft, 		downDownRight);
			
			rlt.add(rectFront);
			rlt.add(rectBottom);
			rlt.add(rectLeft);
			rlt.add(rectRight);
			rlt.add(rectUp);
			rlt.add(rectDown);  
			break;
		}
		default:
			break;
		}
		return rlt;
	}
	
	@Override
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		sb.append("Cuboid ").append(this.getCuboidID()).append(' ')
		.append(Cuboid.typeString[this.getType()]).append("\n[\n")
		.append("\ttopTopLeft \t").append(this.topTopLeft.toString()).append('\n')
		.append("\ttopTopRight \t").append(this.topTopRight.toString()).append('\n')
		.append("\ttopDownLeft \t").append(this.topDownLeft.toString()).append('\n')
		.append("\ttopDownRight \t").append(this.topDownRight.toString()).append('\n')
		.append("\tdownTopLeft \t").append(this.downTopLeft.toString()).append('\n')
		.append("\tdownTopRight \t").append(this.downTopRight.toString()).append('\n')
		.append("\tdownDownLeft \t").append(this.downDownLeft.toString()).append('\n')
		.append("\tdownDownRight \t").append(this.downDownRight.toString()).append("\n]\n");
		return sb.toString();
	} 
	
}
