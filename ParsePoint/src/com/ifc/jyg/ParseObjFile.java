package com.ifc.jyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream; 
import java.io.IOException;
import java.io.InputStreamReader; 

public class ParseObjFile {

	private String fileName;
	private int pointNumber = 0;
	private int cuboidNumber;
	private CoordinateOfPoint[] points;
	private Cuboid[] cuboids;
	private BufferedReader br;
	public ParseObjFile(String fileName) {
		this.fileName = fileName;
	}
	
	private void initObjParameter() {
		File file = new File(fileName);
		try {
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			String content = null;
			while ((content = br.readLine()) != null) {
				if (content.startsWith("g ")) {	//统计长方体的个数
					cuboidNumber++;
				} else if (content.startsWith("v ")) {	//统计点的个数
					pointNumber++;
				} 
			}
		} catch (IOException e) { 
			e.printStackTrace();
		}
		cuboids = new Cuboid[cuboidNumber];
		points = new CoordinateOfPoint[pointNumber];
		//System.out.println("cuboidNumber : " + cuboidNumber + " pointNumber : " + pointNumber);
	}
	
	
	public Cuboid[] getCuboid() {
		initObjParameter();
		try {
			String line = null;
			String ID = null;			
			String[] content = null;
			int i = 0;
			int j = 0;	
			int mark = 0; 
			boolean isNewCuboid = false; 
			File file = new File(fileName);
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				
				if (line.startsWith("g ")) {
					content = line.split(":");
					ID = content[2];
					
					isNewCuboid = true;
				} else if(line.startsWith("v ") && isNewCuboid) {
					String[] coordinate = line.split(" ");  
					points[i] = new CoordinateOfPoint(); 
					points[i].setX(Double.parseDouble(coordinate[1]));
					points[i].setY(Double.parseDouble(coordinate[2]));
					points[i].setZ(Double.parseDouble(coordinate[3]));		
					i++;
				} else if (line.startsWith("usemt")) {
					int numOfcuboidPoint = i - mark;	 //计算该几何体的点数
					CoordinateOfPoint[] cuboidPoints = new CoordinateOfPoint[numOfcuboidPoint]; 
					
					for (int k = 0; k < cuboidPoints.length; k++, mark++) {
						cuboidPoints[k] = new CoordinateOfPoint();
						
						cuboidPoints[k].setX(points[mark].getX());
						cuboidPoints[k].setY(points[mark].getY());
						cuboidPoints[k].setZ(points[mark].getZ());
					}
					//System.out.println("ID : " + ID + " numOfcuboidPoint : " + numOfcuboidPoint);
					cuboids[j] = new Cuboid(ID, numOfcuboidPoint);
					cuboids[j].setPoints(cuboidPoints);
					j++;
					isNewCuboid = false;
				} 
			}
		} catch (Exception e) {
			// TODO: handle exception 
		} 
		return cuboids;
	}
}
