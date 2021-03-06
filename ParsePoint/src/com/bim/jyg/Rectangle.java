package com.bim.jyg;

import com.ifc.jyg.CoordinateOfPoint;

import java.util.ArrayList;


public class Rectangle implements Comparable<Object> {
	
	public final static int  FRONT_BOOTOM = 0;
	public final static int  LEFT_RIGHT = 1;
	public final static int  UP_DOWN = 2;

	public CoordinateOfPoint topLeft;
	public CoordinateOfPoint downRight;
	public CoordinateOfPoint topRight;
	public CoordinateOfPoint downLeft;
	private int direction = 0;
	private String Id;
	private double area = 0.0;
	
	private ArrayList<Edge> edgeList;
	private ArrayList<CoordinateOfPoint> pointList;
	
	public Rectangle(CoordinateOfPoint topLeft, CoordinateOfPoint downRight, String Id) {
		this(topLeft,downRight);
		this.Id = Id;
	}
	
	public Rectangle(ArrayList<CoordinateOfPoint> points, String ID) {
		this.Id = ID;
		this.assignPoints(points);
	}
	
	private Rectangle(CoordinateOfPoint topLeft, CoordinateOfPoint downRight) {
		super();
		this.topLeft = topLeft;
		this.downRight = downRight;
		if(this.topLeft.getX()==this.downRight.getX()) {
			this.direction = FRONT_BOOTOM;
		} else if(this.topLeft.getY()==this.downRight.getY()) {
			this.direction = LEFT_RIGHT;
		} else if(this.topLeft.getZ()==this.downRight.getZ()) {
			this.direction = UP_DOWN;
		} else {
			System.out.println("Rectangle(CoordinateOfPoint topLeft, CoordinateOfPoint downRight) error!");
		}
		this.topRight = new CoordinateOfPoint();
		this.downLeft = new CoordinateOfPoint();
		switch (this.direction) {
			case FRONT_BOOTOM:
				this.topRight.setX(this.topLeft.getX());
				this.topRight.setY(this.downRight.getY());
				this.topRight.setZ(this.topLeft.getZ());
				this.downLeft.setX(this.topLeft.getX());
				this.downLeft.setY(this.topLeft.getY());
				this.downLeft.setZ(this.downRight.getZ());
				break;
			case LEFT_RIGHT:
				this.topRight.setX(this.downRight.getX());
				this.topRight.setY(this.topLeft.getY());
				this.topRight.setZ(this.topLeft.getZ());
				this.downLeft.setX(this.topLeft.getX());
				this.downLeft.setY(this.topLeft.getY());
				this.downLeft.setZ(this.downRight.getZ());
				break;
			case UP_DOWN:
				this.topRight.setX(this.topLeft.getX());
				this.topRight.setY(this.downRight.getY());
				this.topRight.setZ(this.topLeft.getZ());
				this.downLeft.setX(this.downRight.getX());
				this.downLeft.setY(this.topLeft.getY());
				this.downLeft.setZ(this.downRight.getZ());
				break;
			default:
				break;
		}

		ArrayList<Edge> listEdges = new ArrayList<Edge>();
		listEdges.add(new Edge(this.topLeft, 	this.topRight,	this.Id));
		listEdges.add(new Edge(this.topRight, 	this.downRight,	this.Id));
		listEdges.add(new Edge(this.downRight, 	this.downLeft,	this.Id));
		listEdges.add(new Edge(this.downLeft, 	this.topLeft,	this.Id));
		this.edgeList = listEdges;

		ArrayList<CoordinateOfPoint> listPoints = new ArrayList<CoordinateOfPoint>();
		listPoints.add(this.topLeft);
		listPoints.add(this.topRight);
		listPoints.add(this.downRight);
		listPoints.add(this.downLeft);
		this.pointList = listPoints;
	}
	
	public ArrayList<Edge> getLengthEdge() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Edge lengthEdge = null;
		Edge widthEdge = null; 
		double temp = 0.0;
		for (Edge edge : edgeList) {
			
			if (edge.getLength() >= temp) {
				lengthEdge = edge;
				temp = lengthEdge.getLength();
			} else {
				widthEdge = edge;
				break;
			}
		}
		edges.add(lengthEdge);
		edges.add(widthEdge);
		return edges;
	}
 
	public Edge getWidthEdge() {
		Edge widthEdge = null;
		return widthEdge;
	}
	
	public String getID() {
		return Id;
	}
	 
	private void assignPoints(ArrayList<CoordinateOfPoint> points) {
		if (points.size() != 4) {
			System.out.println("error!! rectangle:" + Id + " don't have right point!");
			return;
		}
		CoordinateOfPoint a = points.get(0);
		CoordinateOfPoint b = points.get(1);
		CoordinateOfPoint c = points.get(2);
		CoordinateOfPoint d = points.get(3);
		if (a.getX() == b.getX() && b.getX() == c.getX() && c.getX() == d.getX() && d.getX() == a.getX()) { 
			this.direction = FRONT_BOOTOM;
		} else if (a.getY() == b.getY() && b.getY() == c.getY() && c.getY() == d.getY() && d.getY() == a.getY()) {
			this.direction = LEFT_RIGHT;
		} else if (a.getZ() == b.getZ() && b.getZ() == c.getZ() && c.getZ() == d.getZ() && d.getZ() == a.getZ()) {
			this.direction = UP_DOWN;
		} else {
			System.out.println("rectangle ArrayList<CoordinateOfPoint> points error!");
		}
		 
		switch (this.direction) {
			case FRONT_BOOTOM:	
			{ 
				double ymax = 0.0;
				double zmax = 0.0;
				double ymin = 0.0; 
				double zmin = 0.0;
				for (int i = 0; i < points.size(); i++) {
					if (i == 0) {
						ymax = points.get(0).getY();
						zmax = points.get(0).getZ();
						
						ymin = points.get(0).getY();
						zmin = points.get(0).getZ();
					}
					
					if (points.get(i).getY() >= ymax && points.get(i).getZ() >= zmax) {
						ymax = points.get(i).getY();
						zmax = points.get(i).getZ();
						this.topRight = points.get(i);
					}
					
					if (points.get(i).getY() <= ymin && points.get(i).getZ() <= zmin) {
						ymin = points.get(i).getY();
						zmin = points.get(i).getZ();
						this.downLeft = points.get(i);
					} 
				} 
//				System.out.println("this.topLeft:" + this.topLeft);
//				System.out.println("this.downRight:" + this.downRight);
				for (CoordinateOfPoint point : points) { 
				    if (point.getY() == this.topRight.getY() && point.getZ() == this.downLeft.getZ()) {
						this.downRight = point;
					}
				    
				    if (point.getY() == this.downLeft.getY() && point.getZ() == this.topRight.getZ()) {
						this.topLeft = point;
					}
				}
				break;
			}
			case LEFT_RIGHT:
			{
				double xmax = 0.0;
				double zmax = 0.0;
				double xmin = 0.0; 
				double zmin = 0.0;
				for (int i = 0; i < points.size(); i++) {
					if (i == 0) {
						xmax = points.get(0).getX();
						zmax = points.get(0).getZ();
						xmin = points.get(0).getX();
						zmin = points.get(0).getZ();
					}
					
					if (points.get(i).getX() >= xmax && points.get(i).getZ() >= zmax) {
						xmax = points.get(i).getX();
						zmax = points.get(i).getZ();
						this.topLeft = points.get(i);
					}
					
					if (points.get(i).getX() <= xmin && points.get(i).getZ() <= zmin) {
						xmin = points.get(i).getX();
						zmin = points.get(i).getZ();
						this.downRight = points.get(i);
					}
				}
//				System.out.println("this.topLeft:" + this.topLeft);
//				System.out.println("this.downRight:" + this.downRight);
				for (CoordinateOfPoint point : points) {
				    if (point.getX() == this.topLeft.getX() && point.getZ() == this.downRight.getZ()) {
						this.downLeft = point;
					}
				    
				    if (point.getX() == this.downRight.getX() && point.getZ() == this.topLeft.getZ()) {
						this.topRight = point;
					}
				}
				break;
			}
			case UP_DOWN:
			{ 
				double xmax = 0.0; 
				double ymax = 0.0;
				double xmin = 0.0;
				double ymin = 0.0;
				for (int i = 0; i < points.size(); i++) { 
					if (i == 0) {
						xmax = points.get(0).getX();
						ymax = points.get(0).getY();
						xmin = points.get(0).getX();
						ymin = points.get(0).getY();
					}
					
					if (points.get(i).getX() <= xmin && points.get(i).getY() <= ymin) {
						xmin = points.get(i).getX();
						ymin = points.get(i).getY();
						this.topLeft = points.get(i);
					}
					
					if (points.get(i).getX() >= xmax && points.get(i).getY() >= ymax) {
						xmax = points.get(i).getX();
						ymax = points.get(i).getY();
						this.downRight = points.get(i);
					}
				}
				
				for (CoordinateOfPoint point : points) {
//					System.out.println("...." + point.toString());
				    if (point.getX() == this.downRight.getX() && point.getY() == this.topLeft.getY()) {
						this.downLeft = point;
					}
				    
				    if (point.getX() == this.topLeft.getX() && point.getY() == this.downRight.getY()) {
						this.topRight = point;
					}
				}
				break;
			}
			default:
				break;
		}
//		System.out.println("!!!!!!!!");
//		System.out.println("this.topLeft:" + this.topLeft);
//		System.out.println("this.topRight:" + this.topRight);
//		System.out.println("this.downRight:" + this.downRight);
//		System.out.println("this.downLeft:" + this.downLeft);
		ArrayList<Edge> listEdges = new ArrayList<Edge>();
		listEdges.add(new Edge(this.topLeft, 	this.topRight,	this.Id));
		listEdges.add(new Edge(this.topRight, 	this.downRight,	this.Id));
		listEdges.add(new Edge(this.downRight, 	this.downLeft,	this.Id));
		listEdges.add(new Edge(this.downLeft, 	this.topLeft,	this.Id));
		this.edgeList = listEdges;

		ArrayList<CoordinateOfPoint> listPoints = new ArrayList<CoordinateOfPoint>();
		listPoints.add(this.topLeft);
		listPoints.add(this.topRight);
		listPoints.add(this.downRight);
		listPoints.add(this.downLeft);
		this.pointList = listPoints;
	}
	 
	public static boolean  isValid(CoordinateOfPoint a,CoordinateOfPoint b,CoordinateOfPoint c, CoordinateOfPoint d) {
		return true;
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
	
	
	public static boolean isBiggerContainsSmaller(Rectangle bigger, Rectangle smaller) {
		if(bigger.direction!=smaller.direction) {
			return false;
		}
		
		if(bigger.direction==Rectangle.UP_DOWN) {
			return false;
		}
		switch (bigger.direction) {
		case Rectangle.FRONT_BOOTOM:
		{
			double bigValue = bigger.getIntersectvalue();
			double smallValue = smaller.getIntersectvalue();
			if (bigValue != smallValue) {
				return false;
			}
			if ( bigger.topLeft.getZ() == smaller.topLeft.getZ() && 
					bigger.downRight.getZ() > smaller.downRight.getZ() ) {
				return true;
			}
			break;
		}
		case Rectangle.LEFT_RIGHT:
		{
			double bigValue = bigger.getIntersectvalue();
			double smallValue = smaller.getIntersectvalue();
			if (bigValue != smallValue) {
				return false;
			}
			if ( bigger.topLeft.getZ() == smaller.topLeft.getZ() && 
					bigger.downRight.getZ() < smaller.downRight.getZ() ) {
				return true;
			}
			break; 
		}
		default:
			break;
		}
		return false;
	}
	
	/*
	   -----1-----
	   |         |
	   |         |
	   |         |
	   4         2
	   |         |
	   |         |
	   |         |
	   -----3-----

	 */
	public ArrayList<Edge> getEdges() {
		return this.edgeList;
	}
	
	public ArrayList<CoordinateOfPoint> getPoint() {
//		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
//		CoordinateOfPoint topRight = new CoordinateOfPoint();
//		CoordinateOfPoint DownLeft = new CoordinateOfPoint();
//		switch (direction) {
//		case 0:
//
//			topRight.setX(this.topLeft.getX());
//			topRight.setY(this.downRight.getY());
//			topRight.setZ(this.topLeft.getZ());				
//			DownLeft.setX(this.topLeft.getX());
//			DownLeft.setY(this.topLeft.getY());
//			DownLeft.setZ(this.downRight.getZ());
//			break;
//		case 1:
//			topRight.setX(this.downRight.getX());
//			topRight.setY(this.topLeft.getY());
//			topRight.setZ(this.topLeft.getZ());
//			DownLeft.setX(this.topLeft.getX());
//			DownLeft.setY(this.topLeft.getY());
//			DownLeft.setZ(this.downRight.getZ());
//			break;
//		default:
//			break;
//		}
//		points.add(DownLeft);
//		points.add(topRight);
//		return points;
		return pointList;
	}
	
	public boolean isAtTopContainedByBigger (Rectangle bigger) {
		return Rectangle.isBiggerContainsSmaller(bigger,this);
	}
	
	public boolean isAtTopContainsSmaller(Rectangle smaller) {
		return Rectangle.isBiggerContainsSmaller(this,smaller);
	}
	 
	public boolean isAtTopContainedByOrContains(Rectangle other) {
		if(isAtTopContainedByBigger(other)) {
			return true;
		}
		if(isAtTopContainsSmaller(other)) {
			return true;
		}
		return false;
	}

	/*
	return true if the point p is on any edges of this rectangle.
	 */
	public boolean isByPointOnEdges(CoordinateOfPoint p) {
		return this.containsPoint(p)==0;
	}

	public boolean containsBySmallerOnCorner (Rectangle smaller) {
		return false;
	}

	public boolean isIntersectByAnotherRectangle (Rectangle r) {
		if(this.containsPoint(r.topLeft)>=0) {
			return true;
		}
		if(this.containsPoint(r.topRight)>=0) {
			return true;
		}
		if(this.containsPoint(r.downLeft)>=0) {
			return true;
		}
		if(this.containsPoint(r.downRight)>=0) {
			return true;
		}
		return false;
	}

	public boolean containsAnotherRectangleCompletely(Rectangle rec) {
		return Rectangle.biggerContainsSmallerCompletely(this,rec);
	}

	public static boolean biggerContainsSmallerCompletely(Rectangle bigger, Rectangle smaller) {
		int cnt = bigger.insideEdgeByPointsOfAnotherRectangleCount(smaller);
		if(cnt==4) {
			return true;
		} else {
			return false;
		}
	}

	public int compareByConataination(Rectangle r) {
		if(this==r) {
			return 0;
		}
		if(this.compareTo(r)==0) {
			return 0;
		}
		if(!this.isIntersectByAnotherRectangle(r)) {
			return -2;
		}
		if(this.containsAnotherRectangleCompletely(r)) {
			return 2;
		}

		int cnt = this.onEdgeByPointsOfAnotherRectangleCount(r);
		if(this.getArea()>r.getArea()) {

			if(cnt==0) {
				return -2;
			} else if(cnt>=1 && cnt <=3) {
				return 1;
			} else if (cnt==4) {
				if(this.getArea()>r.getArea()) {
					return 1;
				} else {
					return 0;
				}
			}
		} else {
			if(cnt==0) {
				return -2;
			} else if(cnt>=1 && cnt <=3) {
				return -1;
			} else if (cnt==4) {
				if(this.getArea()<r.getArea()) {
					return -1;
				} else {
					return 0;
				}
			}

		}


		System.out.println("Rectangle.compareByConataination() error!");
		return -1;
	}

	public static void testCompareByConataination () {
		CoordinateOfPoint tb = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint db = new CoordinateOfPoint(5,5,0);
		Rectangle Bigger = new Rectangle(tb,db);

		CoordinateOfPoint ts1 = new CoordinateOfPoint(0,3,0);
		CoordinateOfPoint ds1 = new CoordinateOfPoint(2,5,0);
		Rectangle Smaller1 = new Rectangle(ts1,ds1);
		System.out.println("Bigger.testcCompareByConataination(Smaller1) " + Bigger.compareByConataination(Smaller1));

		CoordinateOfPoint ta1 = new CoordinateOfPoint(6,6,0);
		CoordinateOfPoint da1 = new CoordinateOfPoint(7,7,0);
		Rectangle Away = new Rectangle(ta1,da1);
		System.out.println("Smaller1.testcCompareByConataination(Bigger) " + Smaller1.compareByConataination(Bigger));
		System.out.println("Bigger.testcCompareByConataination(Away) " + Bigger.compareByConataination(Away));
		System.out.println("Away.testcCompareByConataination(Bigger) " + Away.compareByConataination(Bigger));

	}

 
	private int onEdgeByPointsOfAnotherRectangleCount (Rectangle r) {
		int rlt = 0;
		if(this.isByPointOnEdges(r.topLeft)) {
			rlt++;
		}
		if(this.isByPointOnEdges(r.topRight)) {
			rlt++;
		}
		if(this.isByPointOnEdges(r.downLeft)) {
			rlt++;
		}
		if(this.isByPointOnEdges(r.downRight)) {
			rlt++;
		}
		return rlt;
	}

	/*
		returns the cnt of points of r that is inside this
	 */

	private int insideEdgeByPointsOfAnotherRectangleCount (Rectangle r) {
		int rlt = 0;
		if(this.containsPoint(r.topLeft)==1) {
			rlt++;
		}
		if(this.containsPoint(r.topRight)==1) {
			rlt++;
		}
		if(this.containsPoint(r.downLeft)==1) {
			rlt++;
		}
		if(this.containsPoint(r.downRight)==1) {
			rlt++;
		}
		return rlt;
	}



 

	public static void testOnEdgeByPointsOfAnotherRectangleCount() {

		CoordinateOfPoint tb = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint db = new CoordinateOfPoint(5,5,0);
		Rectangle Bigger = new Rectangle(tb,db);

		CoordinateOfPoint ts1 = new CoordinateOfPoint(0,3,0);
		CoordinateOfPoint ds1 = new CoordinateOfPoint(2,5,0);
		Rectangle Smaller1 = new Rectangle(ts1,ds1);
		System.out.println("Bigger.onEdgeByPointsOfAnotherRectangleCount(Smaller1) " + Bigger.onEdgeByPointsOfAnotherRectangleCount(Smaller1));

	}


	/*
	if rec contains point return, 1
	if point is on any of the edges for the rec, return 0
	if rec does not contain point (point is outside of the rec) return -1
	 */
	private int containsPoint(CoordinateOfPoint p) {
		switch (this.direction) {
			case FRONT_BOOTOM:
				if(p.getX()==this.topLeft.getX()) {
					if(p.getY()>this.topLeft.getY() && p.getY()<this.downRight.getY() && p.getZ()<this.topLeft.getZ() && p.getZ()>this.downRight.getZ()) {
						return 1;
					} else if(
						  ( p.getY()==this.topLeft.getY() 	&& p.getZ()<=this.topLeft.getZ() && p.getZ()>=this.downRight.getZ() )
						||( p.getY()==this.downRight.getY() && p.getZ()<=this.topLeft.getZ() && p.getZ()>=this.downRight.getZ() )
						||( p.getZ()==this.topLeft.getZ() 	&& p.getY()>=this.topLeft.getY() && p.getY()<=this.downRight.getY() )
						||( p.getZ()==this.downRight.getZ() && p.getY()>=this.topLeft.getY() && p.getY()<=this.downRight.getY() )
							){
						return 0;
					}
				}
				break;
			case LEFT_RIGHT:
				if(p.getY()==this.topLeft.getY()) {
					if(p.getX()<this.topLeft.getX() && p.getX()>this.downRight.getX() && p.getZ()<this.topLeft.getZ() && p.getZ()>this.downRight.getZ())  {
						return 1;
					} else if(
						  ( p.getX()==this.topLeft.getX() 	&& p.getZ()<=this.topLeft.getZ() && p.getZ()>=this.downRight.getZ() )
						||( p.getX()==this.downRight.getX() && p.getZ()<=this.topLeft.getZ() && p.getZ()>=this.downRight.getZ() )
						||( p.getZ()==this.topLeft.getZ() 	&& p.getX()<=this.topLeft.getX() && p.getX()>=this.downRight.getX())
						||( p.getZ()==this.downRight.getZ() && p.getX()<=this.topLeft.getX() && p.getX()>=this.downRight.getX())
							) {
						return 0;
					}
				}
				break;
			case UP_DOWN:
				if(p.getZ()==this.topLeft.getZ()) {
					if(p.getY()>this.topLeft.getY() && p.getY()<this.downRight.getY() && p.getX()>this.topLeft.getX() && p.getX()<this.downRight.getX() ) {
						return 1;
					} else if(
						   ( p.getY()==this.topLeft.getY()		&& p.getX()>=this.topLeft.getX() && p.getX()<=this.downRight.getX() )
						|| ( p.getY()==this.downRight.getY()	&& p.getX()>=this.topLeft.getX() && p.getX()<=this.downRight.getX() )
						|| ( p.getX()==this.topLeft.getX()		&& p.getY()>=this.topLeft.getY() && p.getY()<=this.downRight.getY() )
						|| ( p.getX()==this.downRight.getX()	&& p.getY()>=this.topLeft.getY() && p.getY()<=this.downRight.getY() )
							) {
						return 0;
					}
				}
				break;
			default:
				break;
		}
		return -1;
	}

	public static void testContainsPoint() {
		CoordinateOfPoint t = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint d = new CoordinateOfPoint(5,5,0);
		Rectangle A = new Rectangle(t,d);
		CoordinateOfPoint p1 = new CoordinateOfPoint(0,2,0);
		System.out.println("testContainsPoint " + A.containsPoint(p1));

		CoordinateOfPoint t2 = new CoordinateOfPoint(5,0,5);
		CoordinateOfPoint d2 = new CoordinateOfPoint(0,0,0);
		Rectangle A2 = new Rectangle(t2,d2);
		CoordinateOfPoint p2 = new CoordinateOfPoint(2,0,5);
		System.out.println("testContainsPoint " + A2.containsPoint(p2));

		CoordinateOfPoint t3 = new CoordinateOfPoint(0,0,5);
		CoordinateOfPoint d3 = new CoordinateOfPoint(0,5,0);
		Rectangle A3 = new Rectangle(t3,d3);
		CoordinateOfPoint p3 = new CoordinateOfPoint(0,2,2);
		System.out.println("testContainsPoint " + A3.containsPoint(p3));
	}

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

//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Rectangle ") 
//		.append(Rectangle.directionString[this.direction]).append("\n")
//		.append("\ttopLeft \t").append(this.topLeft.toString()).append('\n')
//		.append("\tdownRight \t").append(this.downRight.toString()).append('\n');
// 		return sb.toString();
//	}

 
}
