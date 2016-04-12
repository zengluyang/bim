package com.bim.jyg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecompositionRectangle {
	
	private Rectangle targetRectangle;
	private double standardWidth;
	private double standardLength;
	private double targetLength;
	private double targetWidth;
	private double remainingLength;
	private double remainingWidth;
	
	private int horizontalPlacedNumber = 0;
	private int rowPlacedNumber = 0;
	private int horizontalPlacedLayer = 0;  
	
//	private String ID;
	private ArrayList<Edge> edges = new ArrayList<Edge>(); 
	private int totalStandardRectangleNumber = 0;
	public DecompositionRectangle(Rectangle target) {
		this.targetRectangle = target; 
//		this.ID = target.getID();
		edges = target.getEdges();
		targetLength = edges.get(0).getLength();
		remainingLength = getBigDecimalDouble(targetLength);
		targetWidth = edges.get(1).getLength();
		remainingWidth = getBigDecimalDouble(targetWidth);
		targetLength = getBigDecimalDouble(targetLength);
		targetWidth = getBigDecimalDouble(targetWidth);
	}
	/*
	 * compare standard rectangle to target rectangle , get rank way
	 */
	private int compareRectangleToStandardLength(double standardLength) {
		standardLength = getBigDecimalDouble(standardLength);
		double standardWidth = getBigDecimalDouble((standardLength / 2));
		targetLength = getBigDecimalDouble(targetLength);
		targetWidth = getBigDecimalDouble(targetWidth);
		System.out.println("standardLength:" + standardLength + "; targetLength:" + targetLength + "; targetWidth:" + targetWidth);
		if (standardWidth <= targetLength && targetLength < standardLength && targetWidth > standardLength) {
			return 1;
		}
		
		if (targetLength > standardLength && standardWidth <= targetWidth  && targetWidth < standardLength) {
			return 2;
		}
		
		if (targetLength >= standardLength && targetWidth >= standardLength) {
			return 0;
		}
		 
		return -1;
	}
	
	/*
	 * width just sharing and depend on Width number getting the location of remainder place
	 */ 
	private ArrayList<CoordinateOfPoint> getRowPlacedAndWidthJustSharingPoints(int standardWidthNumber) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint();
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:
			{
				double x = targetRectangle.getPoint().get(0).getX(); 
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + (standardWidth * standardWidthNumber), 
						targetRectangle.getPoint().get(3).getZ());
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY() + (standardWidth * standardWidthNumber), 
						targetRectangle.getPoint().get(0).getZ());
				point3 = targetRectangle.getPoint().get(1);
				point4 = targetRectangle.getPoint().get(2);
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY(); 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (standardWidth * standardWidthNumber), 
						y, 
						targetRectangle.getPoint().get(3).getZ());
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() - (standardWidth * standardWidthNumber), 
						y,
						targetRectangle.getPoint().get(0).getZ());
				point3 = targetRectangle.getPoint().get(1);
				point4 = targetRectangle.getPoint().get(2);
				break;
			}
			case Rectangle.UP_DOWN:
			{
				double z = targetRectangle.getPoint().get(0).getZ(); 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX(),
						targetRectangle.getPoint().get(0).getY() + (standardWidth * standardWidthNumber), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
						targetRectangle.getPoint().get(1).getY() + (standardWidth * standardWidthNumber), 
						z);
				point3 = targetRectangle.getPoint().get(1); 
				point4 = targetRectangle.getPoint().get(2);
				break;
			}
			default:
				break;
		}
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		return points;
	}
	
	/*
	 * get Row rank remainder place points
	 */
	private ArrayList<CoordinateOfPoint> getRowPlacedPoints() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		rowPlacedNumber = 0; 
		while (remainingWidth >= standardLength) {
			remainingWidth = remainingWidth - standardLength;
			remainingWidth = getBigDecimalDouble(remainingWidth);
			rowPlacedNumber++;
		} 
		totalStandardRectangleNumber = rowPlacedNumber; 
		
		if (remainingWidth == 0 && standardWidth < targetLength) {
			points = getRowPlacedAndWidthJustSharingPoints(1);
			return points;
		} else if (remainingWidth != 0 && standardWidth == targetLength) {
			points = getHorizontalPlacedLengthJustSharingPoints(2 * rowPlacedNumber);
			return points;
		} else if (remainingWidth == 0 && standardWidth == targetLength) {
			return points;
		}
		
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
	/*
	 * Length Just Sharing and depend on width Number getting remainder place points location
	 */
	private ArrayList<CoordinateOfPoint> getHorizontalPlacedLengthJustSharingPoints(int standardWidthNumber) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint();
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:
			{
				double x = targetRectangle.getPoint().get(0).getX(); 
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY(), 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * standardWidthNumber));
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(1).getY(),
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * standardWidthNumber));
				point3 = targetRectangle.getPoint().get(2);
				point4 = targetRectangle.getPoint().get(3);
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY(); 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
						y, 
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * standardWidthNumber));
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(1).getX(), 
						y,
						targetRectangle.getPoint().get(0).getZ() - (standardWidth * standardWidthNumber));
				point3 = targetRectangle.getPoint().get(2);
				point4 = targetRectangle.getPoint().get(3);
				break;
			}
			case Rectangle.UP_DOWN:
			{
				double z = targetRectangle.getPoint().get(0).getZ(); 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * standardWidthNumber),
						targetRectangle.getPoint().get(0).getY(), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardWidth * standardWidthNumber),
						targetRectangle.getPoint().get(1).getY(), 
						z);
				point3 = targetRectangle.getPoint().get(2); 
				point4 = targetRectangle.getPoint().get(3);
				break;
			}
			default:
				break;
		}
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4); 
		return points;
	}
	
	/*
	 * get Horizontal rank location, there are many situation
	 */
	private ArrayList<CoordinateOfPoint> getHorizontalPlacedPoints() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		horizontalPlacedNumber = 0; 
		horizontalPlacedLayer = 1;
		while (remainingLength >= standardLength) {
			remainingLength = remainingLength - standardLength;
			remainingLength = getBigDecimalDouble(remainingLength);
			horizontalPlacedNumber++;
		}
		totalStandardRectangleNumber = horizontalPlacedNumber; 
		if (remainingLength == 0 && standardWidth < targetWidth) {
			System.out.println("getHorizontalPlacedWidthJustSharingPoints");
			points = getHorizontalPlacedLengthJustSharingPoints(1);
			return points;
		} else if(remainingLength != 0 && standardWidth == targetWidth){
			points = getRowPlacedAndWidthJustSharingPoints(2 * horizontalPlacedNumber);
			return points;
		} else if (remainingLength == 0 && standardWidth == targetWidth) { 
			return points;
		} 
		points = getHorizontalPlacedNewPloygonPoints(horizontalPlacedNumber, 1, 0); 
		return points; 
	}
	 /*
	  *get Horizontal Rank NewPloygon specific Points location
	  */
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
		return points;
	}
	
	/*
	 * get Row Placed NewPloygon specific Points location
	 */
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
	/*
	 * get Width Just Sharing And aline specific Points location
	 */
	private ArrayList<CoordinateOfPoint> getWidthJustSharingAndAlinePoints(int horizontalPlacedNumber) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint();
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:	
			{
				double x = targetRectangle.getPoint().get(0).getX(); 
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength), 
						targetRectangle.getPoint().get(3).getZ());
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength),
						targetRectangle.getPoint().get(1).getZ()); 
				point3 = targetRectangle.getPoint().get(1);
				point4 = targetRectangle.getPoint().get(2);  
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength), 
						y, 
						targetRectangle.getPoint().get(3).getZ());
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength), 
						y,
						targetRectangle.getPoint().get(1).getZ()); 
				point3 = targetRectangle.getPoint().get(1);
				point4 = targetRectangle.getPoint().get(2);  
				break;
			}
			case Rectangle.UP_DOWN:			
			{
				double z = targetRectangle.getPoint().get(0).getZ();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX(),
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength),  
						z); 
				point3 = targetRectangle.getPoint().get(1);
				point4 = targetRectangle.getPoint().get(2);  
				break;
			}
			default:
				break;
	} 
	points.add(point1);
	points.add(point2);
	points.add(point3);
	points.add(point4);
		return points;
	}
	
	/*
	 * get Width Just Sharing Not Aline specific Points location
	 */
	private ArrayList<CoordinateOfPoint> getWidthJustSharingNotAlinePoints(int horizontalPlacedNumber, int rowPlacedNumber) {
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
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength), 
						targetRectangle.getPoint().get(2).getZ());
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength),
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point3 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength)); 
				point4 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength + standardWidth),
						targetRectangle.getPoint().get(0).getZ());
				point5 = targetRectangle.getPoint().get(1);
				point6 = targetRectangle.getPoint().get(2);  
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength), 
						y, 
						targetRectangle.getPoint().get(2).getZ());
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength), 
						y,
						targetRectangle.getPoint().get(1).getZ() - (standardLength * rowPlacedNumber));
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength + standardWidth),
						y,
						targetRectangle.getPoint().get(1).getZ() - (standardLength * rowPlacedNumber));
				point4 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength + standardWidth),
						y, 
						targetRectangle.getPoint().get(1).getZ());
				point5 = targetRectangle.getPoint().get(1);
				point6 = targetRectangle.getPoint().get(2);  
				break;
			}
			case Rectangle.UP_DOWN:			
			{
				double z = targetRectangle.getPoint().get(0).getZ();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX(),
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardLength * rowPlacedNumber),
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength),  
						z);
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardLength * rowPlacedNumber),
						targetRectangle.getPoint().get(3).getY() + (standardLength * horizontalPlacedNumber + standardWidth), 
						z); 
				point4 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(),
						targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
						z);
				point5 = targetRectangle.getPoint().get(1);
				point6 = targetRectangle.getPoint().get(2);  
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
		return points;
	}
	
	/*
	 * get Width And Length both are Just Sharing Not Aline specific Points location
	 */
	private ArrayList<CoordinateOfPoint> getWidthAndLengthJustSharingNotAlinePoints(int horizontalPlacedNumber, int rowPlacedNumber) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint();
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:
			{
				double x = targetRectangle.getPoint().get(0).getX(); 
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength), 
						targetRectangle.getPoint().get(2).getZ());
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength),
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point3 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength + standardWidth), 
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point4 = targetRectangle.getPoint().get(2);
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength), 
						y, 
						targetRectangle.getPoint().get(2).getZ());
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength), 
						y,
						targetRectangle.getPoint().get(1).getZ() - (standardLength * rowPlacedNumber));
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX() - (horizontalPlacedNumber * standardLength + standardWidth),
						y,
						targetRectangle.getPoint().get(1).getZ() - (standardLength * rowPlacedNumber));
				point4 = targetRectangle.getPoint().get(2);
				break;
			}
			case Rectangle.UP_DOWN:
			{
				double z = targetRectangle.getPoint().get(0).getZ();
				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(3).getX(),
						targetRectangle.getPoint().get(3).getY() + (horizontalPlacedNumber * standardLength), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardLength * rowPlacedNumber),
						targetRectangle.getPoint().get(3).getY() + (standardLength * horizontalPlacedNumber), 
						z);
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (standardLength * rowPlacedNumber),
						targetRectangle.getPoint().get(0).getY() + (standardLength * horizontalPlacedNumber + standardWidth), 
						z); 
				point4 = targetRectangle.getPoint().get(2);
				break;
			}
			default:
				break;
		}
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		return points;
	}
	
	/*
	 * get Length Just Sharing And Aline specific Points location
	 */
	private ArrayList<CoordinateOfPoint> getLengthJustSharingAndAlinePoints(int horizontalPlacedLayer) {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
		CoordinateOfPoint point1 = new CoordinateOfPoint();
		CoordinateOfPoint point2 = new CoordinateOfPoint();
		CoordinateOfPoint point3 = new CoordinateOfPoint();
		CoordinateOfPoint point4 = new CoordinateOfPoint(); 
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:
			{
				double x = targetRectangle.getPoint().get(0).getX(); 
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY(), 
						targetRectangle.getPoint().get(0).getZ() - (horizontalPlacedLayer * standardWidth));
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(1).getY(),
						targetRectangle.getPoint().get(1).getZ() - (horizontalPlacedLayer * standardWidth));
				point3 = targetRectangle.getPoint().get(2);
				point4 = targetRectangle.getPoint().get(3);
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
						y, 
						targetRectangle.getPoint().get(0).getZ() - (horizontalPlacedLayer * standardWidth));
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(1).getX(), 
						y,
						targetRectangle.getPoint().get(1).getZ() - (horizontalPlacedLayer * standardWidth));
				point3 = targetRectangle.getPoint().get(2);
				point4 = targetRectangle.getPoint().get(3);
				break;
			}
			case Rectangle.UP_DOWN:
			{
				double z = targetRectangle.getPoint().get(0).getZ(); 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (horizontalPlacedLayer * standardWidth),
						targetRectangle.getPoint().get(0).getY(), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (horizontalPlacedLayer * standardWidth),
						targetRectangle.getPoint().get(1).getY(), 
						z);
				point3 = targetRectangle.getPoint().get(2); 
				point4 = targetRectangle.getPoint().get(3);
				break;
			}
			default:
				break;
		}
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4); 
		return points;
	}
	
	/*
	 * get Length Just Sharing Not Aline specific Points location
	 */
	private ArrayList<CoordinateOfPoint> getLengthJustSharingNotAlinePoints(int horizontalPlacedLayer, int rowPlacedNumber) {
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
				point1 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(0).getY(), 
						targetRectangle.getPoint().get(0).getZ() - (horizontalPlacedLayer * standardWidth));
				point2 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(1).getY() + standardWidth,
						targetRectangle.getPoint().get(0).getZ() - (horizontalPlacedLayer * standardWidth));
				point3 = new CoordinateOfPoint(x, 
						targetRectangle.getPoint().get(1).getY() + standardWidth,
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point4 = new CoordinateOfPoint(x,
						targetRectangle.getPoint().get(1).getY(),
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point5 = targetRectangle.getPoint().get(2);
				point6 = targetRectangle.getPoint().get(3);
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double y = targetRectangle.getPoint().get(0).getY();				 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX(), 
						y, 
						targetRectangle.getPoint().get(0).getZ() - (horizontalPlacedLayer * standardWidth));
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(1).getX() + standardWidth, 
						y,
						targetRectangle.getPoint().get(0).getZ() - (horizontalPlacedLayer * standardWidth));
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(1).getX() + standardWidth, 
						y,
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point4 = new CoordinateOfPoint(targetRectangle.getPoint().get(1).getX(), 
						y,
						targetRectangle.getPoint().get(1).getZ() - (rowPlacedNumber * standardLength));
				point5 = targetRectangle.getPoint().get(2);
				point6 = targetRectangle.getPoint().get(3);
				break;
			}
			case Rectangle.UP_DOWN:
			{
				double z = targetRectangle.getPoint().get(0).getZ(); 
				point1 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (horizontalPlacedLayer * standardWidth),
						targetRectangle.getPoint().get(0).getY(), 
						z);
				point2 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (horizontalPlacedLayer * standardWidth),
						targetRectangle.getPoint().get(1).getY() - standardWidth, 
						z);
				point3 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (rowPlacedNumber * standardLength), 
						targetRectangle.getPoint().get(1).getY() - standardWidth,
						z);
				point4 = new CoordinateOfPoint(targetRectangle.getPoint().get(0).getX() + (rowPlacedNumber * standardLength),
						targetRectangle.getPoint().get(1).getY(), 
						z);
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
		return points;
	}
	
	/*
	 * get Horizontal And Row Placed specific Points location
	 */
	private ArrayList<CoordinateOfPoint> getHorizontalAndRowPlacedPoints() {
		ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
 
 		remainingLength = getBigDecimalDouble(remainingLength);
		remainingWidth = getBigDecimalDouble(remainingWidth); 
		while (remainingWidth >= standardWidth) {
			
			horizontalPlacedNumber = 0;
			remainingLength = targetLength;
			while (remainingLength >= standardLength) {
				remainingLength = remainingLength - standardLength;
				remainingLength = getBigDecimalDouble(remainingLength);
				horizontalPlacedNumber++;
			} 
			remainingWidth = remainingWidth - standardWidth;
			remainingWidth = getBigDecimalDouble(remainingWidth);
			horizontalPlacedLayer++;
		}  
//		remainingLength = getBigDecimalDouble(remainingLength);
//		remainingWidth = getBigDecimalDouble(remainingWidth);
//		System.out.println("remainingWidth:" + remainingWidth);
//		System.out.println("remainingLength:" + remainingLength);
		if (remainingLength >= standardWidth && targetWidth >= standardLength) {
			rowPlacedNumber = horizontalPlacedLayer / 2; 
			remainingLength = remainingLength - standardWidth; 
		}
		remainingLength = getBigDecimalDouble(remainingLength);
		remainingWidth = getBigDecimalDouble(remainingWidth); 
		totalStandardRectangleNumber = rowPlacedNumber + (horizontalPlacedLayer * horizontalPlacedNumber);
		if (remainingLength == 0 && remainingWidth == 0) { 
			 if ((horizontalPlacedLayer % 2) == 1 && rowPlacedNumber > 1) {  
//				System.out.println("getWidthAndLengthJustSharingNotAlinePoints(horizontalPlacedNumber, rowPlacedNumber);");
				points = getWidthAndLengthJustSharingNotAlinePoints(horizontalPlacedNumber, rowPlacedNumber); 
			}else {
//				System.out.println("the whole rectangle is just sharing ");
				return points;
			}  
		} else if (remainingLength == 0 && remainingWidth != 0) {
			 if ((horizontalPlacedLayer % 2) == 1 && rowPlacedNumber > 1) { 
//				 System.out.println("getLengthJustSharingNotAlinePoints(horizontalPlacedLayer, rowPlacedNumber)");
				 points = getLengthJustSharingNotAlinePoints(horizontalPlacedLayer, rowPlacedNumber);
			 } else {
//				 System.out.println("getLengthJustSharingAndAlinePoints(horizontalPlacedLayer); ");
				 points = getLengthJustSharingAndAlinePoints(horizontalPlacedLayer); 
			 } 
		} else if (remainingLength != 0 && remainingWidth == 0) {
			if ((horizontalPlacedLayer % 2) == 1 && rowPlacedNumber > 1) { 
//				System.out.println("getWidthJustSharingNotAlinePoints(horizontalPlacedNumber, rowPlacedNumber);");
				points = getWidthJustSharingNotAlinePoints(horizontalPlacedNumber, rowPlacedNumber); 
			} else { 
//				System.out.println("getWidthJustSharingAndAlinePoints(horizontalPlacedNumber);");
				points = getWidthJustSharingAndAlinePoints(horizontalPlacedNumber); 
			}
		} else if (remainingLength != 0 && remainingWidth != 0) {
			if ((horizontalPlacedLayer % 2) == 1 && rowPlacedNumber > 1) { 
//				System.out.println("polygon have eight points!");
				points = getRowPlacedNewPloygonPoints(horizontalPlacedNumber, horizontalPlacedLayer);
			} else {
//				System.out.println("polygon have six points!");	
				points = getHorizontalPlacedNewPloygonPoints(horizontalPlacedNumber, horizontalPlacedLayer, rowPlacedNumber);
			} 
		} 
		
		//System.out.println("totalStandardRectangleNumber : " +  totalStandardRectangleNumber);
 		return points;
	}
	
	public static double getBigDecimalDouble(double number) {
		BigDecimal bigDecimal = new BigDecimal(number);
		double bigNumber = bigDecimal.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		return bigNumber;
	}
	
	/*
	 * get Standard Rectangle number id and every Point location
	 */
	public Map<Integer, ArrayList<CoordinateOfPoint>> getStandardRectangleIDAndPoints() {
		Map<Integer, ArrayList<CoordinateOfPoint>> standardRectangleIDAndPointsMap = new HashMap<Integer, ArrayList<CoordinateOfPoint>>();
		int numberID = 1;
		switch (targetRectangle.getDirection()) {
			case Rectangle.FRONT_BOOTOM:
			{
				double x = targetRectangle.getPoint().get(0).getX();
				double y = 0;
				double z = 0;
				for (int i = 0; i < horizontalPlacedLayer; i++) {
					z = targetRectangle.getPoint().get(0).getZ() - (i * standardWidth);
					for (int j = 0; j < horizontalPlacedNumber; j++) {
						y = targetRectangle.getPoint().get(0).getY() + (j * standardLength);
						ArrayList<CoordinateOfPoint> rectanglePoints = new ArrayList<CoordinateOfPoint>(); 
						CoordinateOfPoint point1 = new CoordinateOfPoint(x,y,z);
						CoordinateOfPoint point2 = new CoordinateOfPoint(x,y + standardLength,z);
						CoordinateOfPoint point3 = new CoordinateOfPoint(x,y + standardLength,z - standardWidth);
						CoordinateOfPoint point4 = new CoordinateOfPoint(x,y,z - standardWidth); 
						rectanglePoints.add(point1);
						rectanglePoints.add(point2);
						rectanglePoints.add(point3);
						rectanglePoints.add(point4); 
						standardRectangleIDAndPointsMap.put(Integer.valueOf(numberID), rectanglePoints);
						numberID++;
					}
				}
				
				for (int k = 1; k <= rowPlacedNumber; k++) {
					ArrayList<CoordinateOfPoint> rectanglePoints = new ArrayList<CoordinateOfPoint>(); 
					y = targetRectangle.getPoint().get(0).getY() + (horizontalPlacedNumber * standardLength);
					z = targetRectangle.getPoint().get(0).getZ() - ((k-1) * standardLength);
					CoordinateOfPoint point1 = new CoordinateOfPoint(x,y,z);
					CoordinateOfPoint point2 = new CoordinateOfPoint(x,y + standardWidth,z);
					CoordinateOfPoint point3 = new CoordinateOfPoint(x,y + standardWidth,z - standardLength);
					CoordinateOfPoint point4 = new CoordinateOfPoint(x,y,z - standardLength); 
					rectanglePoints.add(point1);
					rectanglePoints.add(point2);
					rectanglePoints.add(point3);
					rectanglePoints.add(point4); 
					standardRectangleIDAndPointsMap.put(Integer.valueOf(numberID), rectanglePoints);
					numberID++;
				}
				break;
			}
			case Rectangle.LEFT_RIGHT:
			{
				double x = 0;
				double y = targetRectangle.getPoint().get(0).getY();
				double z = 0;
				for (int i = 0; i < horizontalPlacedLayer; i++) {
					z = targetRectangle.getPoint().get(0).getZ() - (i * standardWidth);
					for (int j = 0; j < horizontalPlacedNumber; j++) {
						x = targetRectangle.getPoint().get(0).getX() - (j * standardLength);
						ArrayList<CoordinateOfPoint> rectanglePoints = new ArrayList<CoordinateOfPoint>(); 
						CoordinateOfPoint point1 = new CoordinateOfPoint(x,y,z);
						CoordinateOfPoint point2 = new CoordinateOfPoint(x - standardLength,y,z);
						CoordinateOfPoint point3 = new CoordinateOfPoint(x - standardLength,y,z - standardWidth);
						CoordinateOfPoint point4 = new CoordinateOfPoint(x,y,z - standardWidth); 
						rectanglePoints.add(point1);
						rectanglePoints.add(point2);
						rectanglePoints.add(point3);
						rectanglePoints.add(point4);
						standardRectangleIDAndPointsMap.put(Integer.valueOf(numberID), rectanglePoints);
						numberID++;
					}
				}
				
				for (int k = 1; k <= rowPlacedNumber; k++) {
					ArrayList<CoordinateOfPoint> rectanglePoints = new ArrayList<CoordinateOfPoint>(); 
					x = targetRectangle.getPoint().get(0).getX() - (horizontalPlacedNumber * standardLength);
					z = targetRectangle.getPoint().get(0).getZ() - ((k-1) * standardLength);
					CoordinateOfPoint point1 = new CoordinateOfPoint(x,y,z);
					CoordinateOfPoint point2 = new CoordinateOfPoint(x - standardWidth,y,z);
					CoordinateOfPoint point3 = new CoordinateOfPoint(x - standardWidth,y,z - standardLength);
					CoordinateOfPoint point4 = new CoordinateOfPoint(x,y,z - standardLength); 
					rectanglePoints.add(point1);
					rectanglePoints.add(point2);
					rectanglePoints.add(point3);
					rectanglePoints.add(point4);
					standardRectangleIDAndPointsMap.put(Integer.valueOf(numberID), rectanglePoints);
					numberID++;
				}
				break;
			}
			case Rectangle.UP_DOWN:
			{
				double x = 0;
				double y = 0;
				double z = targetRectangle.getPoint().get(0).getZ();
				for (int i = 0; i < horizontalPlacedLayer; i++) {
					x = targetRectangle.getPoint().get(0).getX() + (i * standardWidth);
					for (int j = 0; j < horizontalPlacedNumber; j++) {
						y = targetRectangle.getPoint().get(0).getY() + (j * standardLength);
						ArrayList<CoordinateOfPoint> rectanglePoints = new ArrayList<CoordinateOfPoint>(); 
						CoordinateOfPoint point1 = new CoordinateOfPoint(x,y,z);
						CoordinateOfPoint point2 = new CoordinateOfPoint(x,y + standardLength,z);
						CoordinateOfPoint point3 = new CoordinateOfPoint(x + standardWidth,y + standardLength,z);
						CoordinateOfPoint point4 = new CoordinateOfPoint(x + standardWidth,y,z); 
						rectanglePoints.add(point1);
						rectanglePoints.add(point2);
						rectanglePoints.add(point3);
						rectanglePoints.add(point4);
						standardRectangleIDAndPointsMap.put(Integer.valueOf(numberID), rectanglePoints);
						numberID++;
					}
				}
				
				for (int k = 1; k <= rowPlacedNumber; k++) {
					ArrayList<CoordinateOfPoint> rectanglePoints = new ArrayList<CoordinateOfPoint>(); 
					x = targetRectangle.getPoint().get(0).getX() + (horizontalPlacedNumber * standardLength);
					y = targetRectangle.getPoint().get(0).getY() - ((k-1) * standardLength);
					CoordinateOfPoint point1 = new CoordinateOfPoint(x,y,z);
					CoordinateOfPoint point2 = new CoordinateOfPoint(x,y + standardWidth,z);
					CoordinateOfPoint point3 = new CoordinateOfPoint(x + standardLength,y + standardWidth,z);
					CoordinateOfPoint point4 = new CoordinateOfPoint(x + standardLength,y,z); 
					rectanglePoints.add(point1);
					rectanglePoints.add(point2);
					rectanglePoints.add(point3);
					rectanglePoints.add(point4);
					standardRectangleIDAndPointsMap.put(Integer.valueOf(numberID), rectanglePoints);
					numberID++;
				}
				break;
			}
			default:
				break;
		}  
 		for (Integer location : standardRectangleIDAndPointsMap.keySet()) {
			System.out.println("location: " + location.intValue());
//			bw.write(location.intValue() + ": ");
			ArrayList<CoordinateOfPoint> points = standardRectangleIDAndPointsMap.get(location);
			for (CoordinateOfPoint point : points) {
//				bw.write(point.toString()); 
//				bw.newLine();
				point.setX(getBigDecimalDouble(point.getX()));
				point.setY(getBigDecimalDouble(point.getY()));
				point.setZ(getBigDecimalDouble(point.getZ()));
				System.out.println(point.toString());
			}
			
		}
		return standardRectangleIDAndPointsMap;
	}
	
	/*
	 * get Standard Number And New Polygon specific Points location
	 */
	public Map<Integer, ArrayList<CoordinateOfPoint>> getStandardNumberAndNewPolygonPoints(double standardLength) {
		this.standardLength = standardLength;
		this.standardWidth = standardLength / 2;
		Map<Integer, ArrayList<CoordinateOfPoint>> standardNumberAndPolygonPointsMap = new HashMap<Integer, ArrayList<CoordinateOfPoint>>(); 
		ArrayList<CoordinateOfPoint> newPolygonPoints = new ArrayList<CoordinateOfPoint>();
		switch (compareRectangleToStandardLength(standardLength)) {
			case -1:
//				System.out.println("standard length > rectangle width and standard length > rectangle length");
				newPolygonPoints = targetRectangle.getPoint(); 
				break;				
			case 1:
//				System.out.println("standard length > rectangle length, placed row !");
				newPolygonPoints = getRowPlacedPoints();
				break;				
			case 2:
//				System.out.println("standard length > rectangle width, placed horizontal !"); 
				newPolygonPoints = getHorizontalPlacedPoints();
				break;	
			case 0:
//				System.out.println("standard length < rectangle width and rectangle length, placed horizontal !");
				newPolygonPoints = getHorizontalAndRowPlacedPoints();
				break;	
			default:
				break;
		} 
		for (CoordinateOfPoint point : newPolygonPoints) {
			point.setX(getBigDecimalDouble(point.getX()));
			point.setY(getBigDecimalDouble(point.getY()));
			point.setZ(getBigDecimalDouble(point.getZ()));
		}
//		if (newPolygonPoints.size() == 0) {
//			System.out.println("$$$$$$");
//		}
		standardNumberAndPolygonPointsMap.put(Integer.valueOf(totalStandardRectangleNumber), newPolygonPoints);
		System.out.println("remainingLength:" + remainingLength + "; remainingWidth:" + remainingWidth);
		return standardNumberAndPolygonPointsMap;
	}
	
//	public ArrayList<CoordinateOfPoint> getNewPolygonPoints(double standardLength, int smallerNumbe) {
//		this.standardLength = standardLength;
//		this.standardWidth = standardLength / 2;  
//		ArrayList<CoordinateOfPoint> newPolygonPoints = new ArrayList<CoordinateOfPoint>();
//		switch (compareRectangleToStandardLength(standardLength)) {
//			case -1:
//				System.out.println("standard length > rectangle width and standard length > rectangle length");
//				newPolygonPoints = targetRectangle.getPoint();
//				break;				
//			case 1:
//				System.out.println("standard length > rectangle length, placed row!");
////				for (CoordinateOfPoint point : targetRectangle.getPoint()) {
////					System.out.println(point.toString());
////				}
//				newPolygonPoints = getRowPlacedPoints();
//				break;				
//			case 2:
//				System.out.println("standard length > rectangle width, placed horizontal!");
//				newPolygonPoints = getHorizontalPlacedPoints();
//				break;	
//			case 0:
//				System.out.println("standard length < rectangle width and standard length < rectangle length, placed horizontals!");
//				newPolygonPoints = getHorizontalAndRowPlacedPoints();
//				break;	
//			default:
//				break;
//		} 
////		polygonIDPointsMap.put(ID, newPolygonPoints);
//		return newPolygonPoints;
//	}
	
	
}
