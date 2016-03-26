package com.ifc.jyg;

import java.util.ArrayList;
import java.util.TreeSet;

public class Triangle implements Comparable<Object>{

	
	public final static int  FRONT_BOOTOM = 0;
	public final static int  LEFT_RIGHT = 1;
	public final static int  UP_DOWN = 2;
	
	private Edge first;
	private Edge second;
	private Edge third;
	private int direction = 0;
	
	ArrayList<Edge> edgeList;
	
	
	
	public Triangle(CoordinateOfPoint a, CoordinateOfPoint b, CoordinateOfPoint c) {
		TreeSet<Edge> edges = new TreeSet<>();
		edgeList = new ArrayList<Edge>();
		
		if (a.getZ() == b.getZ() && a.getZ() == b.getZ() && b.getZ() == c.getZ()) {
			direction = UP_DOWN;
		}
		Edge ab = new Edge(a, b);
		Edge ac = new Edge(a, c);
		Edge bc = new Edge(b, c);
		edges.add(ab);
		edges.add(ac);
		edges.add(bc);
		int i = 0;
		for(Edge e : edges){
			edgeList.add(e);
			switch (i) {
			case 0:
				this.first = e;
				break;
			case 1:
				this.second = e;
				break;
			case 2:
				this.third = e;
				break;
			}
			i++;
		}
		
	}

	public Edge getFirstEdge() {
		return first;
	}
	
	public Edge getSecondEdge() {
		return second;
	}
	
	public Edge getThirdEdge() {
		return third;
	}
 

	@Override
	public int compareTo(Object o) {
		if (this == o) {
			return 0;
		} else if (o != null && o instanceof Triangle) {
			//System.out.println("o != null && o instanceof CoordinateOfPoint");
			Triangle triangle = (Triangle) o;
			if (this.first.compareTo(o) < 0 ) {
				return -1;
			} else if (this.first.compareTo(triangle) == 0) {
				if (this.second.compareTo(triangle) < 0) {
					return -1;
				} else if (this.second.compareTo(triangle) == 0) {
					return this.third.compareTo(triangle);
				}
			}
		} 
		return 1; 
	}
	
	public ArrayList<Edge> getEdges() {
		return edgeList;
	}


	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) { 
		 
		this.direction = direction;
	}
}
