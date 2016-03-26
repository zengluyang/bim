package com.ifc.jyg;

import java.util.*;


public class Rectangle implements Comparable<Object> {
	
	public final static int  FRONT_BOOTOM = 0;
	public final static int  LEFT_RIGHT = 1;
	public final static int  UP_DOWN = 2;
	public final static String[] directionString = {"FRONT_BOOTOM","LEFT_RIGHT","UP_DOWN"}; 
	
	public CoordinateOfPoint topLeft;
	public CoordinateOfPoint downRight;
	public CoordinateOfPoint topRight;
	public CoordinateOfPoint downLeft;
	public String Id;
	private int direction = 0;
	private double area = 0.0;
	public Rectangle(CoordinateOfPoint topLeft, CoordinateOfPoint downRight, String Id) {
		this(topLeft,downRight);
		this.Id = Id;
	}
	public Rectangle(CoordinateOfPoint topLeft, CoordinateOfPoint downRight) {
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
	}

	static {
		testcontrunctPolygonsUsingBigRectangleAndSmallRectangles();
	}

	public static void testcontrunctPolygonsUsingBigRectangleAndSmallRectangles() {
		CoordinateOfPoint tb = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint db = new CoordinateOfPoint(5,5,0);
		Rectangle Bigger = new Rectangle(tb,db);

		CoordinateOfPoint ts1 = new CoordinateOfPoint(0,3,0);
		CoordinateOfPoint ds1 = new CoordinateOfPoint(2,5,0);
		Rectangle Smaller1 = new Rectangle(ts1,ds1);
		ArrayList<Rectangle> smallRecs = new ArrayList<>();
		smallRecs.add(Smaller1);
		System.out.println("contrunctPolygonsUsingBigRectangleAndSmallRectangles(Bigger,Smaller1) " + contrunctPolygonsUsingBigRectangleAndSmallRectangles(Bigger,smallRecs));

		CoordinateOfPoint ta1 = new CoordinateOfPoint(6,6,0);
		CoordinateOfPoint da1 = new CoordinateOfPoint(7,7,0);
		Rectangle Away = new Rectangle(ta1,da1);
	}

	public static ArrayList<Polygon> contrunctPolygonsUsingBigRectangleAndSmallRectangles(Rectangle bigRec, ArrayList<Rectangle> smallRecs) {
		ArrayList<Polygon> rlt = new ArrayList<Polygon>();
		TreeSet<Edge> edges = new TreeSet<Edge>();
		ArrayList<Edge> bigRecEdges = bigRec.getEdges();
		Edge A = bigRecEdges.get(0);
		Edge B = bigRecEdges.get(1);
		Edge C = bigRecEdges.get(2);
		Edge D = bigRecEdges.get(3);
		for(int i=0;i<smallRecs.size();i++) {
			Rectangle ri = smallRecs.get(i);
			ArrayList<Edge> esi = ri.getEdges();
			Edge ai = esi.get(0);
			Edge bi = esi.get(1);
			Edge ci = esi.get(2);
			Edge di = esi.get(3);
			if(i==0) {
				ArrayList<Edge> esAai = ai.getNewEgdesFromThisAndThatEdges(A);
				ArrayList<Edge> esBbi = bi.getNewEgdesFromThisAndThatEdges(B);
				ArrayList<Edge> esCci = ci.getNewEgdesFromThisAndThatEdges(C);
				ArrayList<Edge> esDdi = di.getNewEgdesFromThisAndThatEdges(D);

				edges.addAll(esAai);
				edges.addAll(esBbi);
				edges.addAll(esCci);
				edges.addAll(esDdi);

			} else {
				TreeSet<Edge> localEdges = new TreeSet<Edge>();
				TreeMap<Integer,TreeMap<CoordinateOfPoint,ArrayList<Edge>>> intDoubleEdgeSet = new TreeMap();
				intDoubleEdgeSet.put(Edge.X_AXIS,new TreeMap<>());
				intDoubleEdgeSet.put(Edge.Y_AXIS,new TreeMap<>());
				intDoubleEdgeSet.put(Edge.Z_AXIS,new TreeMap<>());
				edges.addAll(ri.getEdges());
				for(Edge e : edges) {
					Integer direction  = e.getDirection();
					TreeMap<CoordinateOfPoint,ArrayList<Edge>> doubleEdgeSet = intDoubleEdgeSet.get(direction);
					if(direction==null) {
						//System.out.println("contrunctPolygonsUsingBigRectangleAndSmallRectangles error");
						break;
					}
					CoordinateOfPoint axisValue = e.getAxisValue();
					if(!doubleEdgeSet.containsKey(axisValue)) {
						doubleEdgeSet.put(axisValue,new ArrayList<>());
					}
					ArrayList<Edge> edgeSet  = doubleEdgeSet.get(axisValue);
					edgeSet.add(e);
				}

				for(Integer direction:intDoubleEdgeSet.keySet()) {
					TreeMap<CoordinateOfPoint,ArrayList<Edge>> doubleEdgeSet = intDoubleEdgeSet.get(direction);
					for(CoordinateOfPoint axisValue : doubleEdgeSet.keySet()) {
						ArrayList<Edge> edgeSet  = doubleEdgeSet.get(axisValue);
						if(edgeSet.size()==1) {
							localEdges.add(edgeSet.get(0));
						} else if(edgeSet.size()==2){
							ArrayList<Edge> es = edgeSet.get(0).getNewEgdesFromThisAndThatEdges(edgeSet.get(1));
							localEdges.addAll(es);
						} else {
							System.out.print("contrunctPolygonsUsingBigRectangleAndSmallRectangles error! not implemented");
						}

					}
				}

				edges = localEdges;
			}
		}
		ArrayList<ArrayList<Edge>> edgeListListRlt = new ArrayList<ArrayList<Edge>> ();
		for(Edge e:edges) {

			boolean isInsert = false;
			for(ArrayList<Edge> onePolyEdgeList : edgeListListRlt) {
				for(Edge onePolyEdge:onePolyEdgeList) {
					if(Edge.isConnectedByTwoEdges(onePolyEdge,e)) {
						isInsert = true;
						break;
					}
				}
				if(isInsert) {
					onePolyEdgeList.add(e);
				}
			}
			if(!isInsert) {
				ArrayList<Edge> onePolyEdgeList = new ArrayList<Edge>();
				onePolyEdgeList.add(e);
				edgeListListRlt.add(onePolyEdgeList);
			}
		}
		for(ArrayList<Edge> onePolyEdgeList:edgeListListRlt) {
			Polygon p = new Polygon(onePolyEdgeList,bigRec.Id);
			rlt.add(p);
		}

		return rlt;
	}
	
//
//	public Rectangle(CoordinateOfPoint topLeft,
//			CoordinateOfPoint downRight,int direction) {
//		super();
//		this.direction = direction;
//		this.topLeft = topLeft;
//		this.downRight = downRight;
//	}
//
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
		ArrayList<Edge> listEdges = new ArrayList<Edge>();
		listEdges.add(new Edge(this.topLeft, 	this.topRight,	this.Id));
		listEdges.add(new Edge(this.topRight, 	this.downRight,	this.Id));
		listEdges.add(new Edge(this.downRight, 	this.downLeft,	this.Id));
		listEdges.add(new Edge(this.downLeft, 	this.topLeft,	this.Id));
		return listEdges; 
	}
	
	public ArrayList<CoordinateOfPoint> getPoint() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint topRight = new CoordinateOfPoint();
		CoordinateOfPoint DownLeft = new CoordinateOfPoint();
		switch (direction) {
		case 0:

			topRight.setX(this.topLeft.getX());
			topRight.setY(this.downRight.getY());
			topRight.setZ(this.topLeft.getZ());				
			DownLeft.setX(this.topLeft.getX());
			DownLeft.setY(this.topLeft.getY());
			DownLeft.setZ(this.downRight.getZ());
			break;
		case 1:
			topRight.setX(this.downRight.getX());
			topRight.setY(this.topLeft.getY());
			topRight.setZ(this.topLeft.getZ());
			DownLeft.setX(this.topLeft.getX());
			DownLeft.setY(this.topLeft.getY());
			DownLeft.setZ(this.downRight.getZ());
			break;
		default:
			break;
		}
		points.add(DownLeft);
		points.add(topRight);
		return points;
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

	public static ArrayList<Polygon> generateFormBiggerAndSmallers (Rectangle bigger, ArrayList<Rectangle> smallers) {
		ArrayList<Polygon> rlt= new ArrayList<Polygon>();
		return rlt;
	}

	public static void testIsIntersectByAnotherRectangle() {

		CoordinateOfPoint tb = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint db = new CoordinateOfPoint(5,5,0);
		Rectangle Bigger = new Rectangle(tb,db);

		CoordinateOfPoint ts1 = new CoordinateOfPoint(0,3,0);
		CoordinateOfPoint ds1 = new CoordinateOfPoint(2,5,0);
		Rectangle Smaller1 = new Rectangle(ts1,ds1);
		System.out.println("Bigger.testIsIntersectByAnotherRectangle(Smaller1) " + 
					Bigger.isIntersectByAnotherRectangle(Smaller1));

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



	static {
//		testContainsPoint();
		testOnEdgeByPointsOfAnotherRectangleCount();
		testIsIntersectByAnotherRectangle();
		testCompareByConataination();
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rectangle ") 
		.append(Rectangle.directionString[this.direction]).append("\n")
		.append("\ttopLeft \t").append(this.topLeft.toString()).append('\n')
		.append("\tdownRight \t").append(this.downRight.toString()).append('\n');
 		return sb.toString();
	}

	static {
		testtoMatlab2d();
	}
	public static void testtoMatlab2d() {

		CoordinateOfPoint t = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint d = new CoordinateOfPoint(5,5,0);
		Rectangle A = new Rectangle(t,d);
		System.out.println("testtoMatlab2d "+A.toMatlab2d());
	}

	public String toMatlab2d() {
		Polygon p = new Polygon(this.getEdges());
		return p.toMatlab2D();
	}

	public static void testGetEdges () {
		CoordinateOfPoint t = new CoordinateOfPoint(0,0,0);
		CoordinateOfPoint d = new CoordinateOfPoint(5,5,0);
		Rectangle A = new Rectangle(t,d);
		ArrayList<Edge> edges = A.getEdges();
		System.out.println("testGetEdges "+edges);

	}
}
