package com.bim.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class test {

	private static Scanner cin;
	private static BufferedWriter bw = null;
	private static Map<Integer, Integer> totalNumberMap = new TreeMap<Integer, Integer>();
	private static ArrayList<CoordinateOfPoint> newPolygonPoints = null;
	public static void main(String[] args) throws IOException {
//		CoordinateOfPoint topLeft = new CoordinateOfPoint(0, 0, 0);
//		CoordinateOfPoint downRight = new CoordinateOfPoint(1050, 2375, 0);
//		Rectangle rectangle = new Rectangle(topLeft, downRight, "123456789"); 
		ParseRectangleResult rectangleResult = new ParseRectangleResult("E:\\IFC\\objFile\\YD_S_B04_1F.ifc_final_result.txt");
//		Map<String, ArrayList<CoordinateOfPoint>> rectangleIDAndPointsMap = rectangleResult.getRectangleIDAndPoints();
		Map<Integer, Map<String, ArrayList<CoordinateOfPoint>>> rectangleIDAndPointsMap = rectangleResult.getRectangleIDAndPoints();
//		System.out.println("rectangleIDAndPointsMap.size:" + rectangleIDAndPointsMap.size());
		bw = new BufferedWriter(new FileWriter("E:\\IFC\\objFile\\newPolygonPointsAndEdge_outPut.txt"));
		cin = new Scanner(System.in);
		while (cin.hasNext()) {
			double standardLength = cin.nextDouble();	//input standard length 
			for (Integer rectangleNumber : rectangleIDAndPointsMap.keySet()) {
//				System.out.println(rectangleNumber);
				Map<String, ArrayList<CoordinateOfPoint>> IDAndPointsMap = rectangleIDAndPointsMap.get(rectangleNumber);
				for (String ID : IDAndPointsMap.keySet()) {
					System.out.println(ID);
					bw.write(ID);
					bw.newLine();
					ArrayList<CoordinateOfPoint> rectanglePoints = IDAndPointsMap.get(ID);
					System.out.println("target rectangle!");
					for (CoordinateOfPoint point : rectanglePoints) {
					System.out.println("V " + DecompositionRectangle.getBigDecimalDouble(point.getX()) + " " 
							+ DecompositionRectangle.getBigDecimalDouble(point.getY()) + " " 
							+ DecompositionRectangle.getBigDecimalDouble(point.getZ()));
//					bw.write(point.toString());
//					bw.newLine(); 
					} 
					Rectangle rectangle = new Rectangle(rectanglePoints, ID); 
					DecompositionRectangle decompositionRectangle = new DecompositionRectangle(rectangle);	 
					Map<Integer, ArrayList<CoordinateOfPoint>> standardNumberAndPolygonPointsMap = 
							decompositionRectangle.getStandardNumberAndNewPolygonPoints(standardLength); 
					for (Integer totalNumber : standardNumberAndPolygonPointsMap.keySet()) {
						
						bw.write(totalNumber);
						bw.newLine();
						newPolygonPoints = standardNumberAndPolygonPointsMap.get(totalNumber);
						if (newPolygonPoints.size() != 0) {
							System.out.println("newPolygon:");
							for (CoordinateOfPoint point : newPolygonPoints) {
								System.out.println("V " + DecompositionRectangle.getBigDecimalDouble(point.getX()) + " " 
										+ DecompositionRectangle.getBigDecimalDouble(point.getY()) + " " 
										+ DecompositionRectangle.getBigDecimalDouble(point.getZ()));
//								bw.write(point.toString());
//								bw.newLine(); 
							}
							ArrayList<Edge> newPolygonEdges = new ArrayList<Edge>();
							for (int i = 0; i < newPolygonPoints.size(); i++) {
								if (i == newPolygonPoints.size() - 1) {
									Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(0));
									newPolygonEdges.add(edge);
								} else {
									Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(i+1));
									newPolygonEdges.add(edge);
								} 
							}
							if (newPolygonEdges.size() != 4) {
								for (Edge edge : newPolygonEdges) {
//									bw.write(edge.toString());
									System.out.print(edge.toString());
								}
							} 
						} else {
							System.out.println(ID +" is just sharing!");
						}   
						System.out.println("totalStandardRectangleNumber:" + totalNumber);
						if (!totalNumberMap.containsKey(Integer.valueOf(totalNumber))) {
							totalNumberMap.put(totalNumber, 1);
						} else {
							int count = totalNumberMap.get(totalNumber) + 1;
							totalNumberMap.put(totalNumber, count);
						}
					}
					
					Map<Integer, ArrayList<CoordinateOfPoint>> standardRectangleIDAndPointsMap = decompositionRectangle.getStandardRectangleIDAndPoints();
					for (Integer location : standardRectangleIDAndPointsMap.keySet()) {
						bw.write(location.intValue() + ": ");
						ArrayList<CoordinateOfPoint> points = standardRectangleIDAndPointsMap.get(location);
						for (CoordinateOfPoint point : points) {
							bw.write(point.toString()); 
							bw.newLine();
						}
						
					}
					bw.flush(); 
					System.out.println();
				}
//					ArrayList<CoordinateOfPoint> newPolygonPoints = decompositionRectangle.getNewPolygonPoints(length, 0);
//					bw.write(ID);
//					bw.newLine();
//					if (newPolygonPoints != null) {
//						for (CoordinateOfPoint coordinateOfPoint : newPolygonPoints) {
//							bw.write(coordinateOfPoint.toString());
//							bw.newLine();
////							System.out.println(coordinateOfPoint.toString());
//						}
//					}  
//					
////					System.out.println("newPolygonPoints.size():" + newPolygonPoints.size());
//					ArrayList<Edge> newPolygonEdges = new ArrayList<Edge>();
//					for (int i = 0; i < newPolygonPoints.size(); i++) {
//						if (i == newPolygonPoints.size() - 1) {
//							Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(0));
//							newPolygonEdges.add(edge);
//						} else {
//							Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(i+1));
//							newPolygonEdges.add(edge);
//						} 
//					}
//					
//					for (Edge edge : newPolygonEdges) {
//						bw.write(edge.toString());
////						bw.newLine();
////						System.out.print(edge.toString());
//					}
//					bw.flush(); 
//				}
			
//				Map<String, ArrayList<CoordinateOfPoint>> polygonIDPointsMap = decompositionRectangle.getNewPolygonPoints(length, 0);
//				
//				ArrayList<CoordinateOfPoint> newPolygonPoints = new ArrayList<CoordinateOfPoint>();
//				for (String iD : polygonIDPointsMap.keySet()) {
//					newPolygonPoints = polygonIDPointsMap.get(iD);
//					System.out.println("ID:" + iD);
//					bw.write(iD);
//					bw.newLine();
//					if (newPolygonPoints != null) {
//						for (CoordinateOfPoint coordinateOfPoint : newPolygonPoints) {
//							bw.write(coordinateOfPoint.toString());
//							bw.newLine();
//							System.out.println(coordinateOfPoint.toString());
//						}
//					}   
//					System.out.println("newPolygonPoints.size():" + newPolygonPoints.size());
//					ArrayList<Edge> newPolygonEdges = new ArrayList<Edge>();
//					for (int i = 0; i < newPolygonPoints.size(); i++) {
//						if (i == newPolygonPoints.size() - 1) {
//							Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(0));
//							newPolygonEdges.add(edge);
//						} else {
//							Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(i+1));
//							newPolygonEdges.add(edge);
//						} 
//					}
//					
//					for (Edge edge : newPolygonEdges) {
//						bw.write(edge.toString());
//						//bw.newLine();
//						System.out.print(edge.toString());
//					}
//					bw.flush(); 
//				}  
			} 
			int totalCount = 0;
			for (Integer totalNumber : totalNumberMap.keySet()) {
				int count = totalNumberMap.get(totalNumber);
				totalCount += count; 
				System.out.println("totalNumber:" + totalNumber + "; count:" + count);
			}
			System.out.println("totalCount:" + totalCount);
		}
		bw.close();
		 
	}
}

