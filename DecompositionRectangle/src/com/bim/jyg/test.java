package com.bim.jyg;

import java.awt.Point;
import java.util.ArrayList;

public class test {

	public static void main(String[] args) {
		CoordinateOfPoint topLeft = new CoordinateOfPoint(1, 0, 0);
		CoordinateOfPoint downRight = new CoordinateOfPoint(956, 1059, 0);
		Rectangle rectangle = new Rectangle(topLeft, downRight, "123456789");
		DecompositionRectangle decompositionRectangle = new DecompositionRectangle(rectangle);
		ArrayList<Edge> newPolygonEdges = new ArrayList<Edge>();
		//100 indiacate standard length width is 50(default), 40 is standard number ,not effect temporary 
		ArrayList<CoordinateOfPoint> newPolygonPoints = decompositionRectangle.getNewPolygonPoints(100, 40);
		if (newPolygonPoints != null) {
//			System.out.println("newPolygonPoints.size()" + newPolygonPoints.size());
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

