package com.bim.jyg;

import java.util.ArrayList;

public class Decomposition {

	private Rectangle bigger;
	private Rectangle smaller;
	private double bigTotalLength;
	private double bigRemainingLength;
	private double bigTotalWidth;
	private double bigRemainingWidth;
	
	private double smallTotalLength;
//	private double smallRemainingLength;
	private double smallTotalWidth;
//	private double smallRemainingWidth;
	
//	private ArrayList<Edge> biggerRectangleEdgeList = new ArrayList<Edge>();
//	private ArrayList<Edge> smallerRectangleEdgeList = new ArrayList<Edge>();
//	private ArrayList<Edge> newPolygonEdges = new ArrayList<Edge>();
	private ArrayList<CoordinateOfPoint> newPolygonPoints = new ArrayList<CoordinateOfPoint>();
	
	public Decomposition(Rectangle bigger, Rectangle smaller) {
		this.bigger = bigger;
		this.smaller = smaller;
//		this.biggerRectangleEdgeList = bigger.getEdges();
//		this.smallerRectangleEdgeList = smaller.getEdges();
	}
	 
	private boolean compareLength() {
		ArrayList<Edge> biggerEdges = bigger.getLengthEdge();
		ArrayList<Edge> smallerEdges = smaller.getLengthEdge();
		bigTotalLength = biggerEdges.get(0).getLength();
		bigTotalWidth = biggerEdges.get(1).getLength();
		smallTotalLength = smallerEdges.get(0).getLength();
		smallTotalWidth = smallerEdges.get(1).getLength();
		
		if (smallerEdges.get(0).getLength() > biggerEdges.get(1).getLength()) {	//smaller length > bigger width
			return true;
		} else {
			return false;
		} 
	}
	
//	private boolean compareBigWidthToSmallLength() {
//		bigTotalLength = biggerRectangleEdgeList.get(0).getLength();
//		bigTotalWidth = biggerRectangleEdgeList.get(1).getLength();
//		smallTotalLength = smallerRectangleEdgeList.get(0).getLength();
//		smallTotalWidth = smallerRectangleEdgeList.get(1).getLength();
//		if (smallTotalLength > bigTotalWidth) {
//			return true;
//		}
//		return false;
//	}
	
	public ArrayList<CoordinateOfPoint> getNewPolygonPoints(int smallerNumbe) {
		int rowPlacedNumber = 0;
		int horizontalPlacedNumber = 0;
		if (compareLength()) {
			System.out.println("bigger length > smaller width, only horizontal rank");

			bigRemainingWidth = bigTotalWidth;
			while (bigRemainingWidth > smallTotalWidth) {
				rowPlacedNumber++;
				bigRemainingWidth = bigRemainingWidth - smallTotalWidth;
			}
			bigRemainingLength = bigTotalLength - smallTotalLength;
			horizontalPlacedNumber++;
			while (bigRemainingLength > smallTotalLength) {
				horizontalPlacedNumber++;
				bigRemainingLength = bigRemainingLength - smallTotalLength;
			}
			
			if ((rowPlacedNumber + horizontalPlacedNumber) == smallerNumbe) {
				System.out.println("placed success");
			} else {
				System.out.println("placed failer");
			}
		}
		return newPolygonPoints;
	}
	
}
