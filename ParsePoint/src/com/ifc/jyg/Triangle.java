package com.ifc.jyg;

import java.util.ArrayList;
import java.util.TreeSet;

public class Triangle implements Comparable<Object>{
	private Edge first;
	private Edge second;
	private Edge third;
	ArrayList<Edge> edgeList;
	
	
	public Triangle(CoordinateOfPoint a, CoordinateOfPoint b, CoordinateOfPoint c) {
		TreeSet<Edge> edges = new TreeSet<>();
		edgeList = new ArrayList<Edge>();
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
				this.second = e;
				break;
			}
			i++;
		}
		
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
}
