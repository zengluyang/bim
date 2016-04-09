package com.bim.jyg;

import java.util.ArrayList;

public class DecompositionRectangle {
	
	private Rectangle targetRectangle;
	private double standardWidth;
	private double standardLength;
	private double targetLength;
	private double targetWidth;
	private double remainingLength;
	private double remainingWidth;
	private ArrayList<Edge> edges = new ArrayList<Edge>();
//	private ArrayList<CoordinateOfPoint> newPolygonPoints = new ArrayList<CoordinateOfPoint>();
//	private ArrayList<Edge> newPloygonEdges = new ArrayList<Edge>();
	
	public DecompositionRectangle(Rectangle target) {
		this.targetRectangle = target; 
		edges = target.getEdges();
		targetLength = edges.get(0).getLength();
		remainingLength = targetLength;
		targetWidth = edges.get(1).getLength();
		remainingWidth = targetWidth;
	}
	
	private int compareRectangleToStandardLength(double standardLength) {
		
		if (standardLength > targetLength && standardLength > targetWidth) {
			return -1;
		}
		
		if (standardLength > targetLength) {			
			return 1;
		}
		
		if (standardLength > targetWidth) {
			return 2;
		}
		
		return 0;
	}
	
	private ArrayList<CoordinateOfPoint> getRowPlacedPoints() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		int rowPlacedNumber = 0;
		while (remainingWidth > standardLength) {
			remainingWidth = remainingWidth - standardLength;
			rowPlacedNumber++;
		}
		System.out.println("total number:" + rowPlacedNumber);
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:	
			{
				double x = targetRectangle.getPoint().get(0).getX();
				CoordinateOfPoint point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY(), 
						targetRectangle.getPoint().get(0).getZ() - (rowPlacedNumber * standardLength));
				CoordinateOfPoint point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + standardWidth, 
						targetRectangle.getPoint().get(0).getZ() - (rowPlacedNumber * standardLength));
				CoordinateOfPoint point3 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + standardWidth, 
						targetRectangle.getPoint().get(0).getZ());
				CoordinateOfPoint point4 = targetRectangle.getPoint().get(1);
				CoordinateOfPoint point5 = targetRectangle.getPoint().get(2);
				CoordinateOfPoint point6 = targetRectangle.getPoint().get(3); 
				points.add(point1);
				points.add(point2);
				points.add(point3);
				points.add(point4);
				points.add(point5);
				points.add(point6);
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();
				CoordinateOfPoint point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
						y, 
						targetRectangle.getPoint().get(0).getZ() - (rowPlacedNumber * standardLength));
				CoordinateOfPoint point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - standardWidth, 
						y,
						targetRectangle.getPoint().get(0).getZ() - (rowPlacedNumber * standardLength));
				CoordinateOfPoint point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - standardWidth,
						y,
						targetRectangle.getPoint().get(0).getZ());
				CoordinateOfPoint point4 = targetRectangle.getPoint().get(1);
				CoordinateOfPoint point5 = targetRectangle.getPoint().get(2);
				CoordinateOfPoint point6 = targetRectangle.getPoint().get(3); 
				points.add(point1);
				points.add(point2);
				points.add(point3);
				points.add(point4);
				points.add(point5);
				points.add(point6);
				break;
			}
			case Rectangle.UP_DOWN:			
			{
				double z = targetRectangle.getPoint().get(0).getZ();
				CoordinateOfPoint point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (rowPlacedNumber * standardLength),
						targetRectangle.getPoint().get(0).getY(), 
						z);
				CoordinateOfPoint point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (rowPlacedNumber * standardLength),
						targetRectangle.getPoint().get(0).getY() + standardWidth, 
						z);
				CoordinateOfPoint point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
						targetRectangle.getPoint().get(0).getY() + standardWidth,
						z);
				CoordinateOfPoint point4 = targetRectangle.getPoint().get(1);
				CoordinateOfPoint point5 = targetRectangle.getPoint().get(2);
				CoordinateOfPoint point6 = targetRectangle.getPoint().get(3); 
				points.add(point1);
				points.add(point2);
				points.add(point3);
				points.add(point4);
				points.add(point5);
				points.add(point6);
				break;
			}
			default:
				break;
		} 
		return points;
	}
	
	private ArrayList<CoordinateOfPoint> getHorizontalPlacedPoints() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		int horizontalPlacedNumber = 0;
		while (remainingLength > standardLength) {
			remainingLength = remainingLength - standardLength;
			horizontalPlacedNumber++;
		}
		
		System.out.println("horizontalPlacedNumber:" + horizontalPlacedNumber);
		points = getHorizontalPlacedNewPloygonPoints(horizontalPlacedNumber, 1, 0); 
		return points; 
	}
	 
	private ArrayList<CoordinateOfPoint> getHorizontalPlacedNewPloygonPoints(int horizontalPlacedNumber, int horizontalLayer, int rowPlacedNumber) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint();
		CoordinateOfPoint point5 = new CoordinateOfPoint();
		CoordinateOfPoint point6 = new CoordinateOfPoint();
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:	
			{
				double x = targetRectangle.getPoint().get(0).getX();
				 
				if (rowPlacedNumber == 0) {
					point1 = new CoordinateOfPoint(x, 
							targetRectangle.getPoint().get(0).getY(), 
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point2 = new CoordinateOfPoint(x, 
							targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength), 
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point3 = new CoordinateOfPoint(x, 
							targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength), 
							targetRectangle.getPoint().get(0).getZ());
				} else {
					point1 = new CoordinateOfPoint(x, 
							targetRectangle.getPoint().get(0).getY(), 
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point2 = new CoordinateOfPoint(x, 
							targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point3 = new CoordinateOfPoint(x, 
							targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
							targetRectangle.getPoint().get(0).getZ());
				} 
				
				point4 = targetRectangle.getPoint().get(1);
				point5 = targetRectangle.getPoint().get(2);
				point6 = targetRectangle.getPoint().get(3);  
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();
				if (rowPlacedNumber == 0) {
					point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
							y, 
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength), 
							y,
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength), 
							y,
							targetRectangle.getPoint().get(0).getZ());
				} else {
					point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
							y, 
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength + standardWidth), 
							y,
							targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
					point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength + standardWidth), 
							y,
							targetRectangle.getPoint().get(0).getZ());
				}
				
				point4 = targetRectangle.getPoint().get(1);
				point5 = targetRectangle.getPoint().get(2);
				point6 = targetRectangle.getPoint().get(3);  
				break;
			}
			case Rectangle.UP_DOWN:			
			{
				double z = targetRectangle.getPoint().get(0).getZ();
				if (rowPlacedNumber == 0) {
					point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * horizontalLayer),
							targetRectangle.getPoint().get(0).getY(), 
							z);
					point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * horizontalLayer),
							targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber), 
							z);
					point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
							targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber), 
							z);
				} else {
					point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * horizontalLayer),
							targetRectangle.getPoint().get(0).getY(), 
							z);
					point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * horizontalLayer),
							targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber + standardWidth), 
							z);
					point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
							targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber + standardWidth), 
							z);
				}
				
				point4 = targetRectangle.getPoint().get(1);
				point5 = targetRectangle.getPoint().get(2);
				point6 = targetRectangle.getPoint().get(3);  
				break;
			}
			default:
				break;
		} 
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		points.add(point5);
		points.add(point6);
//		System.out.println("points.size:" + points.size());
		return points;
	}
	
	private ArrayList<CoordinateOfPoint> getRowPlacedNewPloygonPoints(int horizontalPlacedNumber, int horizontalLayer) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint();
		CoordinateOfPoint point5 = new CoordinateOfPoint();
		CoordinateOfPoint point6 = new CoordinateOfPoint();
		CoordinateOfPoint point7 = new CoordinateOfPoint();
		CoordinateOfPoint point8 = new CoordinateOfPoint();
		
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:	
			{
				double x = targetRectangle.getPoint().get(0).getX(); 
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY(), 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength), 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
				point3 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength), 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * (horizontalLayer - 1)));
				point4 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * (horizontalLayer - 1)));
				point5 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
						targetRectangle.getPoint().get(0).getZ());
				
				point6 = targetRectangle.getPoint().get(1);
				point7 = targetRectangle.getPoint().get(2);
				point8 = targetRectangle.getPoint().get(3); 
				
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
						y, 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength), 
						y,
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * horizontalLayer));
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength), 
						y,
						targetRectangle.getPoint().get(0).getZ()- (standardWidth * (horizontalLayer - 1)));
				point4 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength + standardWidth), 
						y,
						targetRectangle.getPoint().get(0).getZ()- (standardWidth * (horizontalLayer - 1)));
				point5 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength + standardWidth), 
						y,
						targetRectangle.getPoint().get(0).getZ());
				 
				point6 = targetRectangle.getPoint().get(1);
				point7 = targetRectangle.getPoint().get(2);
				point8 = targetRectangle.getPoint().get(3);  
				break;
			}
			case Rectangle.UP_DOWN:			
			{
				double z = targetRectangle.getPoint().get(0).getZ();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * horizontalLayer),
						targetRectangle.getPoint().get(0).getY(), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * horizontalLayer),
						targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber), 
						z);
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * (horizontalLayer - 1)),
						targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber), 
						z);
				point4 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * (horizontalLayer - 1)),
						targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber + standardWidth), 
						z);
				point5 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
						targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber + standardWidth), 
						z);
				
				point6 = targetRectangle.getPoint().get(1);
				point7 = targetRectangle.getPoint().get(2);
				point8 = targetRectangle.getPoint().get(3);  
				break;
			}
			default:
				break;
		} 
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		points.add(point5);
		points.add(point6);
		points.add(point7);
		points.add(point8);
		return points;
	}
	
	
	private ArrayList<CoordinateOfPoint> getHorizontalAndRowPlacedPoints() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		int horizontalPlacedNumber = 0;
		int rowPlacedNumber = 0;
		int horizontalPlacedLayer = 0;
		while (remainingWidth > standardWidth) {
			
			horizontalPlacedNumber = 0;
			remainingLength = targetLength;
			while (remainingLength > standardLength) {
				remainingLength = remainingLength - standardLength;
				horizontalPlacedNumber++;
			}
//			System.out.println("horizontalPlacedNumber:" + horizontalPlacedNumber);
			
			remainingWidth = remainingWidth - standardWidth;
			horizontalPlacedLayer++;
		} 
//		System.out.println("horizontalPlacedLayer:" + horizontalPlacedLayer);
//		System.out.println("this.targetLength:"+ this.targetLength);
//		System.out.println("this.targetWidth:" + this.targetWidth);
//		System.out.println("this.remainingLength:" + this.remainingLength);
//		System.out.println("this.remainingWidth:"+this.remainingWidth);
//		System.out.println("this.standardLength:"+this.standardLength);
//		System.out.println("this.standardWidth:" + this.standardWidth);
		
		if (remainingLength > standardWidth && targetWidth > standardLength) {
			rowPlacedNumber = horizontalPlacedLayer / 2; 
			if ((horizontalPlacedLayer % 2) == 0) {
				System.out.println("polygon have six points!");				//rowNumber = horizontalLayer / 2;
				points = getHorizontalPlacedNewPloygonPoints(horizontalPlacedNumber, horizontalPlacedLayer, rowPlacedNumber);
			} else {
				System.out.println("polygon have eight points!");
				points = getRowPlacedNewPloygonPoints(horizontalPlacedNumber, horizontalPlacedLayer);
				
			} 
		} else {
			System.out.println("no row placed");
			points = getHorizontalPlacedNewPloygonPoints(horizontalPlacedNumber, horizontalPlacedLayer, 0);
		}
//		System.out.println(this.remainingLength);
//		System.out.println(this.remainingWidth);
		System.out.println("total standard number : " + (rowPlacedNumber + horizontalPlacedLayer * horizontalPlacedNumber));
//		System.out.println("points:" + points.size());
		return points;
	}
	
	public ArrayList<CoordinateOfPoint> getNewPolygonPoints(double standardLength, int smallerNumbe) {
		this.standardLength = standardLength;
		this.standardWidth = standardLength / 2;
		ArrayList<CoordinateOfPoint> newPolygonPoints = new ArrayList<CoordinateOfPoint>();
//		System.out.println(this.standardLength);
//		System.out.println(this.standardWidth);
		switch (compareRectangleToStandardLength(standardLength)) {
			case -1:
				System.out.println("standard length > rectangle width and length");
				newPolygonPoints = targetRectangle.getPoint();
				break;				
			case 1:
				System.out.println("standard length > rectangle length, placed row !");
				newPolygonPoints = getRowPlacedPoints();
				break;				
			case 2:
				System.out.println("standard length > rectangle width, placed horizontal !");
				newPolygonPoints = getHorizontalPlacedPoints();
				break;	
			case 0:
				System.out.println("standard length < rectangle width and rectangle length, placed horizontal !");
				newPolygonPoints = getHorizontalAndRowPlacedPoints();
				break;	
			default:
				break;
		}
//		System.out.println("newPolygonPoints:" + newPolygonPoints.size());
		return newPolygonPoints;
	}
	
	
}
