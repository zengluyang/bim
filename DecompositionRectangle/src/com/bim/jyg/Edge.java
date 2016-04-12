package com.bim.jyg;

import java.util.ArrayList;

public class Edge implements Comparable<Object>{

	public final static int  X_AXIS = 0;
	public final static int  Y_AXIS = 1;
	public final static int  Z_AXIS = 2;
	public final static int  OTHER = 3;
	public final static String[] directionString = {"X_AXIS","Y_AXIS","Z_AXIS","OTHER"};
	
	private CoordinateOfPoint first;
	private CoordinateOfPoint second;
	private int direction = 0;
	public String Id;

	public Edge(CoordinateOfPoint first, CoordinateOfPoint second,String Id) {
		this(first,second);
		this.Id = Id;
	}

	public Edge(CoordinateOfPoint first, CoordinateOfPoint second) {
		super();
		if (first.compareTo(second) == 0) {
			System.out.println("edge error!");
		}
		if (first.compareTo(second) < 0) {
			this.first = first;
			this.second = second;
		} else {
			this.first = second;
			this.second = first;
		}
		
		if(first.getY()==second.getY() && first.getZ()==second.getZ()) {
			this.direction = X_AXIS;
		} else if(first.getX()==second.getX() && first.getZ()==second.getZ()) {
			this.direction = Y_AXIS;
		} else if(first.getX()==second.getX() && first.getY()==second.getY()) {
			this.direction = Z_AXIS;
		} else {
			this.direction = OTHER;
		}
	}
	
	@Override
	public int compareTo(Object o) {
		
		if (this == o) {
			return 0;
		} else if (o != null && o instanceof Edge) {
			Edge e = (Edge) o;
			if(this.first.compareTo(e.first) < 0)  {
				return -1;
			} else if (this.first.compareTo(e.first) == 0) {
				return this.second.compareTo(e.second);
			} 
		}
		return 1; 
	}

	/*
		when return true a is connetec to b
	 */

	public static boolean compareByConnection (Edge a, Edge b) {
		if(a.first.compareTo(b.first)==0) {
			return true;
		} else if(a.first.compareTo(b.second)==0) {
			return true;
		} else if(a.second.compareTo(b.first)==0) {
			return true;
		} else if(a.second.compareTo(b.first)==0) {
			return true;
		}
		return false;
	}
 
//	static {
//		//test compareTo
//		CoordinateOfPoint p1 = new CoordinateOfPoint(-12.8362,8.50043,2.775);
//		CoordinateOfPoint p2 = new CoordinateOfPoint(-7.28625,8.50043,2.775);
//		Edge e1 = new Edge(p1,p2);
//
//		CoordinateOfPoint p3 = new CoordinateOfPoint(-12.8362,8.50043,2.775);
//		CoordinateOfPoint p4 = new CoordinateOfPoint(-7.28625,8.50043,2.775);
//		Edge e2 = new Edge(p3,p4);
//		System.out.println("test compareTo"+e1.compareTo(e2));
//		TreeMap<Edge, Integer> emp = new TreeMap<>();
//		emp.put(e1,1);
//		if(emp.containsKey(e2)) {
//			emp.put(e1,2);
//		}
//	}
//


	public CoordinateOfPoint getAxisValue() {
		CoordinateOfPoint rlt = null;
		switch (this.direction) {
			case X_AXIS:
				rlt = new CoordinateOfPoint(this.first.getY(),this.first.getZ(),0);
				break;
			case Y_AXIS:
				rlt = new CoordinateOfPoint(this.first.getX(),this.first.getZ(),0);
				break;
			case Z_AXIS:
				rlt = new CoordinateOfPoint(this.first.getX(),this.first.getY(),0);
				break;
			case OTHER:
			default:
				rlt = new CoordinateOfPoint(0,0,0);
				System.out.println("ArrayList<Double> getAxisValue error!");
				break;
		}
		return rlt;
	}
	
	public static boolean  isOnSameAxis (Edge a, Edge b) {
		if(a.direction!=b.direction) {
			return false;
		}
		switch (a.direction) {
		case X_AXIS:
			return a.first.getY()== b.first.getY() && a.second.getZ()==b.second.getZ();
		case Y_AXIS:
			return a.first.getX()== b.first.getX() && a.second.getZ()==b.second.getZ();
		case Z_AXIS:
			return a.first.getX()== b.first.getX() && a.second.getY()==b.second.getY();
		case OTHER:
			return false;
		default:
			return false;
		}
	}
	
	public static ArrayList<Edge> getNewEgdesFromTwoEdges(Edge longer, Edge shorter){
		ArrayList<Edge> rlt = new ArrayList<>(); 
		if(longer.direction!=shorter.direction) {
			rlt.add(longer);
			rlt.add(shorter);
			return rlt;
		}
		if(!Edge.isOnSameAxis(longer, shorter)) {
			rlt.add(longer);
			rlt.add(shorter);
			return rlt;
		}
		CoordinateOfPoint a = longer.first;
		CoordinateOfPoint b = longer.second;
		CoordinateOfPoint c = shorter.first;
		CoordinateOfPoint d = shorter.second;
		
		double A = longer.getFirstIntersectValue();
		double B = longer.getSecondIntersectValue();
		double C = shorter.getFirstIntersectValue();
		double D = shorter.getSecondIntersectValue();
		if(B < C) {
			rlt.add(longer);
			rlt.add(shorter);
			return rlt;
		} else if (B == C) {
			Edge edge = new Edge(a, d);
			rlt.add(edge);
			return rlt;
		} else if (B > C && C > A && D > B) {
			Edge edge1 = new Edge(a, c);
			Edge edge2 = new Edge(b, d);
			rlt.add(edge1);
			rlt.add(edge2);
			return rlt;
		} else if (B > C && C > A && D == B) {
			Edge edge1 = new Edge(a, c); 
			rlt.add(edge1); 
			return rlt;
		} else if (B > C && C > A && B > D && D > A) {
			Edge edge1 = new Edge(a, c);
			Edge edge2 = new Edge(b, d);
			rlt.add(edge1);
			rlt.add(edge2);
			return rlt;
		} else if (C == A && B > D) {
			Edge edge2 = new Edge(b, d); 
			rlt.add(edge2);
			return rlt;
		} else if (C < A && B > D && D > A) {
			Edge edge1 = new Edge(a, c);
			Edge edge2 = new Edge(b, d);
			rlt.add(edge1);
			rlt.add(edge2);
			return rlt;
		} else if (D == A) { 
			Edge edge2 = new Edge(b, c); 
			rlt.add(edge2);
			return rlt;
		}  else if (D < A) {
			rlt.add(longer);
			rlt.add(shorter);
			return rlt;
		} else if(A==C && B==D){
			//rlt.add(longer);
			return rlt;
		} else {
			System.out.println("getNewEgdesFromTwoEdges error! "+longer.getLength()+" "+shorter.getLength());
		}
		return rlt;
	}
	
	public double getFirstIntersectValue() {
		switch (this.direction) {
		case X_AXIS:
			return this.first.getX();
		case Y_AXIS:
			return this.first.getY();
		case Z_AXIS:
			return this.first.getZ();
		case OTHER:
			return 0.0;
		default:
			return 0.0;
		}
	}
	
	public double getSecondIntersectValue() {
		switch (this.direction) {
		case X_AXIS:
			return this.second.getX();
		case Y_AXIS:
			return this.second.getY();
		case Z_AXIS:
			return this.second.getZ();
		case OTHER:
			return 0.0;
		default:
			return 0.0;
		}
	}
	
	public  ArrayList<Edge> getNewEgdesFromThisAndThatEdges (Edge e) {
		if(this.getLength()>e.getLength()) {
			return Edge.getNewEgdesFromTwoEdges(this, e);
		} else {
			return Edge.getNewEgdesFromTwoEdges(e, this);
		}
	}
	
	public double getLength() {

		switch (this.direction) {
		case X_AXIS:
			return this.second.getX() - this.first.getX();
		case Y_AXIS:
			return this.second.getY() - this.first.getY();
		case Z_AXIS:
			return this.second.getZ() - this.first.getZ();
		case OTHER:
			return 0.0;
		default:
			return 0.0;
		}
	}

	public CoordinateOfPoint getFirst() {
		return first;
	}

	public CoordinateOfPoint getSecond() {
		return second;
	}

	public int getDirection() {
		return direction;
	}

	public static boolean isConnectedByTwoEdges(Edge a, Edge b) {
		if(a.first==b.first) {
			return true;
		} else if(a.first==b.second) {
			return true;
		} else if(a.second==b.first) {
			return true;
		} else if(a.second ==b.second) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isConnectedTo(Edge other) {
		if(Edge.isConnectedByTwoEdges(this,other) || Edge.isConnectedByTwoEdges(other,this) ) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("E ").append(first.getX()).append(" ").append(first.getY()).append(" ").append(first.getZ()).append(" ")
		.append(second.getX()).append(" ").append(second.getY()).append(" ").append(second.getZ()).append("\n");
		return sb.toString();
	}
	
	public CoordinateOfPoint getFirstPoint() {
		return first;
	}
	 
	public CoordinateOfPoint getSecondPoint() {
		return second;
	} 
}
