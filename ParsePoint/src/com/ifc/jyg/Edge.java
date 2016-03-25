package com.ifc.jyg;

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
		} else if(first.getY()==second.getX() && first.getZ()==second.getZ()) {
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
	
	
	
	public static boolean  isOnSameAxis (Edge a, Edge b) {
		if(a.direction!=b.direction) {
			return false;
		}
		switch (a.direction) {
		case X_AXIS:
			return a.first.getY()== b.first.getY() && a.second.getY()==b.second.getY();
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
			return rlt;
		}
		if(!Edge.isOnSameAxis(longer, shorter)) {
			return rlt;
		}
		CoordinateOfPoint a = longer.first;
		CoordinateOfPoint b = longer.second;
		CoordinateOfPoint c = shorter.first;
		CoordinateOfPoint d = shorter.second;
		
		double A = longer.getFirstIntersectValue();
		double B = longer.getSecondIntersectValue();
		double C = longer.getFirstIntersectValue();
		double D = longer.getSecondIntersectValue();
		if(B > C) {
			return rlt;
		} else if (B == C) {
			Edge edge = new Edge(a, d);
			rlt.add(edge);
			return rlt;
		} else if (B < C && C < A && D < B) {
			Edge edge1 = new Edge(a, c);
			Edge edge2 = new Edge(b, d);
			rlt.add(edge1);
			rlt.add(edge2);
			return rlt;
		} else if (B < C && C < A && D == B) {
			Edge edge1 = new Edge(a, c); 
			rlt.add(edge1); 
			return rlt;
		} else if (B < C && C < A && B < D && D < A) {
			Edge edge1 = new Edge(a, c);
			Edge edge2 = new Edge(b, d);
			rlt.add(edge1);
			rlt.add(edge2);
			return rlt;
		} else if (C == A && B < D) { 
			Edge edge2 = new Edge(b, d); 
			rlt.add(edge2);
			return rlt;
		} else if (C > A && B < D && D < A) {
			Edge edge1 = new Edge(a, c);
			Edge edge2 = new Edge(b, d);
			rlt.add(edge1);
			rlt.add(edge2);
			return rlt;
		} else if (D == A) { 
			Edge edge2 = new Edge(b, c); 
			rlt.add(edge2);
			return rlt;
		}  else if (D > A) { 
			return rlt;
		} else {
			System.out.println("getNewEgdesFromTwoEdges error!");
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
			return this.second.getX() - this.second.getX();
		case Y_AXIS:
			return this.second.getY() - this.second.getY();
		case Z_AXIS:
			return this.second.getZ() - this.second.getZ();
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
		sb.append("Edge ").append(first).append(second).append("\n");
		return sb.toString();
	}
	
	public CoordinateOfPoint getFirstPoint() {
		return first;
	}
	 
	public CoordinateOfPoint getSecondPoint() {
		return second;
	}
	
}
