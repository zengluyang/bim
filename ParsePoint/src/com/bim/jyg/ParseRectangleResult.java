package com.bim.jyg;

import com.ifc.jyg.CoordinateOfPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseRectangleResult {

	private String fileName;
	private BufferedReader br = null;
	public ParseRectangleResult(String fileName) {
		this.fileName = fileName;
	}
	
	public Map<Integer, Map<String, ArrayList<CoordinateOfPoint>>> getRectangleIDAndPoints() {
		Map<Integer, Map<String, ArrayList<CoordinateOfPoint>>> rectangleIDAndPointsMap = 
												new HashMap<Integer, Map<String, ArrayList<CoordinateOfPoint>>>();
		try {
			File file = new File(fileName);
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			String line = null;
			String ID = null;
			boolean Rec = false;
			int rectangleNumber = 0;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("REC")) { 
					rectangleNumber++;
//					System.out.println("rectangleNumber:"+ rectangleNumber);
					ID = line.split(" ")[1];
					Rec = true;
				} else if (line.startsWith("V ") && Rec == true) {
					String[] coordinate = line.split(" ");
					CoordinateOfPoint point = new CoordinateOfPoint();
					point.setX(Double.parseDouble(coordinate[1])); 
					point.setY(Double.parseDouble(coordinate[2]));
					point.setZ(Double.parseDouble(coordinate[3])); 
					if (! rectangleIDAndPointsMap.containsKey(rectangleNumber)) {
						Map<String, ArrayList<CoordinateOfPoint>> IDAndPointsMap = new HashMap<String, ArrayList<CoordinateOfPoint>>();
						ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
						points.add(point); 
						IDAndPointsMap.put(ID, points);
						rectangleIDAndPointsMap.put(rectangleNumber, IDAndPointsMap);
					} else {
						Map<String, ArrayList<CoordinateOfPoint>> IDAndPointsMap = rectangleIDAndPointsMap.get(rectangleNumber);
						ArrayList<CoordinateOfPoint> points = IDAndPointsMap.get(ID); 
						points.add(point);  
						IDAndPointsMap.put(ID, points);
						rectangleIDAndPointsMap.put(rectangleNumber, IDAndPointsMap);
						if (points.size() == 4) {
							Rec = false;
						}
					} 
				}
			}
		} catch (IOException e) {
			 
		}
//		for (Integer rectangleNumber : rectangleIDAndPointsMap.keySet()) {
//			//System.out.println("rectangleNumber:" + rectangleNumber);
//			Map<String, ArrayList<CoordinateOfPoint>> IDAndPointsMap = rectangleIDAndPointsMap.get(rectangleNumber);
//			for (String ID : IDAndPointsMap.keySet()) {
//				System.out.println(ID);
//				ArrayList<CoordinateOfPoint> points = IDAndPointsMap.get(ID); 
//				for (CoordinateOfPoint point : points) {
//					System.out.println("V " + point.getX() + " " + point.getY() + " " + point.getZ());
//				}
//			}
//		}
//		System.out.println("size:" + rectangleIDAndPointsMap.size());
		return rectangleIDAndPointsMap;
	}
	
//	public Map<String, ArrayList<CoordinateOfPoint>> getRectangleIDAndPoints() {
//		
//		Map<String, ArrayList<CoordinateOfPoint>> rectangleIDAndPointsMap = new HashMap<String, ArrayList<CoordinateOfPoint>>();
//		try {
//			File file = new File(fileName);
//			FileInputStream fin = new FileInputStream(file);
//			InputStreamReader isr = new InputStreamReader(fin);
//			br = new BufferedReader(isr);
//			String line = null;
//			String ID = null;
//			int test = 0;
//			while ((line = br.readLine()) != null) {
//				if (line.startsWith("REC")) {
//					System.out.println("test:"+ test++);
//					ID = line; 	
//				} else if (line.startsWith("V ")) {
//					String[] coordinate = line.split(" ");
//					CoordinateOfPoint point = new CoordinateOfPoint();
//					point.setX(Double.parseDouble(coordinate[1])); 
//					point.setY(Double.parseDouble(coordinate[2]));
//					point.setZ(Double.parseDouble(coordinate[3])); 
//					
//					if (! rectangleIDAndPointsMap.containsKey(ID)) {
//						ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
//						points.add(point);
//						rectangleIDAndPointsMap.put(ID, points);
//					} else {
//						ArrayList<CoordinateOfPoint> points = rectangleIDAndPointsMap.get(ID);
//						points.add(point);
//						rectangleIDAndPointsMap.put(ID, points);
//					}
//				}
//			}
//		} catch (IOException e) {
//			 
//		}
//		System.out.println("size:" + rectangleIDAndPointsMap.size());
//		return rectangleIDAndPointsMap;
//	} 
}
