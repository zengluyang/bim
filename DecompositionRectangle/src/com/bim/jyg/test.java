package com.bim.jyg;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

public class test {

	private static Scanner cin;

	public static void main(String[] args) {
		CoordinateOfPoint topLeft = new CoordinateOfPoint(0, 0, 0);
		CoordinateOfPoint downRight = new CoordinateOfPoint(956, 1059, 0);
		Rectangle rectangle = new Rectangle(topLeft, downRight, "123456789"); 
		cin = new Scanner(System.in);
		while (cin.hasNext()) {
			double length = cin.nextDouble();
			DecompositionRectangle decompositionRectangle = new DecompositionRectangle(rectangle);
			ArrayList<Edge> newPolygonEdges = new ArrayList<Edge>();
			ArrayList<CoordinateOfPoint> newPolygonPoints = decompositionRectangle.getNewPolygonPoints(length, 40);
			if (newPolygonPoints != null) {
//				System.out.println("newPolygonPoints.size()" + newPolygonPoints.size());
				for (CoordinateOfPoint coordinateOfPoint : newPolygonPoints) {
					System.out.println(coordinateOfPoint.toString());
				}
			}
			
			 
			for (int i = 0; i < (newPolygonPoints.size() / 2); i++) {
				Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(i+1));
				newPolygonEdges.add(edge);
			}
			
			for (Edge edge : newPolygonEdges) {
				System.out.print(edge.toString());
			}
		}
		
		 
	}
}

