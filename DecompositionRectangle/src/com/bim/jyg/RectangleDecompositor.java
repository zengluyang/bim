package com.bim.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ZLY on 2016/4/21.
 */
public class RectangleDecompositor {
    private BufferedWriter bw = null;
    private PrintWriter out;
    private Map<Integer, Integer> totalNumberMap = new TreeMap<Integer, Integer>();
    private ParseRectangleResult rectangleResult;
    private double standardLength;

    RectangleDecompositor(String inputFileName,String outputFileName,double length) throws IOException {
        rectangleResult = new ParseRectangleResult(inputFileName);
        bw = new BufferedWriter(new FileWriter(outputFileName));
        out = new PrintWriter(bw);
        standardLength=length;
    }

    public void doDecompose () {
        Map<Integer, Map<String, ArrayList<CoordinateOfPoint>>> rectangleIDAndPointsMap = rectangleResult.getRectangleIDAndPoints();
        for (Integer rectangleNumber : rectangleIDAndPointsMap.keySet()) {
//				System.out.println(rectangleNumber);
            Map<String, ArrayList<CoordinateOfPoint>> IDAndPointsMap = rectangleIDAndPointsMap.get(rectangleNumber);
            for (String ID : IDAndPointsMap.keySet()) {
                System.out.println(ID);
//					bw.write(ID);
//					bw.newLine();
                out.println(ID);
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

//						bw.write(totalNumber);
//						bw.newLine();
                    out.println(totalNumber);
                    ArrayList<CoordinateOfPoint> newPolygonPoints = standardNumberAndPolygonPointsMap.get(totalNumber);
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
                                Edge edge = new Edge(newPolygonPoints.get(i), newPolygonPoints.get(i + 1));
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
                        System.out.println(ID + " is just sharing!");
                    }
                    System.out.println("totalStandardRectangleNumber:" + totalNumber);
                    if (!totalNumberMap.containsKey(Integer.valueOf(totalNumber))) {
                        totalNumberMap.put(totalNumber, 1);
                    } else {
                        int count = totalNumberMap.get(totalNumber) + 1;
                        totalNumberMap.put(totalNumber, count);
                    }
                }

                Map<Integer,ArrayList<CoordinateOfPoint>> standardRectangleIDAndPointsMap = decompositionRectangle.getStandardRectangleIDAndPoints();
                for (Integer location : standardRectangleIDAndPointsMap.keySet()) {
                    //bw.write(location.intValue() + ": ");
                    out.println("REC " + location);
                    ArrayList<CoordinateOfPoint> points = standardRectangleIDAndPointsMap.get(location);
                    for (CoordinateOfPoint point : points) {
//							bw.write(point.toString());
//							bw.newLine();
                        out.println(point.toString());
                    }

                }
                //bw.flush();
                out.flush();
                System.out.println();
            }
        }
//        int totalCount = 0;
//        for (Integer totalNumber : totalNumberMap.keySet()) {
//            int count = totalNumberMap.get(totalNumber);
//            totalCount += count;
//            System.out.println("totalNumber:" + totalNumber + "; count:" + count);
//        }
//        System.out.println("totalCount:" + totalCount);
    }

}
