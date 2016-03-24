package com.ifc.jyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ParseObjFile {

	private String fileName;
	private int pointNumber = 0;
	private int cuboidNumber; 
	private ArrayList<CoordinateOfPoint> listPoints;
	private ArrayList<Cuboid> listCuboids;
 
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
		listPoints = new ArrayList<CoordinateOfPoint>(pointNumber);
		listCuboids = new ArrayList<Cuboid>(cuboidNumber);
		//System.out.println("cuboidNumber : " + cuboidNumber + " pointNumber : " + pointNumber);
	}
	
	
	public ArrayList<Cuboid> getCuboid() {
		initObjParameter();
		try {
			String line = null;
			String ID = null;			
			String[] content = null;
			int i = 0; 
			int mark = 0; 
			int type = 0;
			boolean isNewCuboid = false; 
			File file = new File(fileName);
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			Cuboid cuboid = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("g ____-____:")) {
					content = line.split(":");
					ID = content[2];
					type = 0;		//Beam
					isNewCuboid = true;
				} else if (line.startsWith("g ____-____-__:")) {
					content = line.split(":");
					ID = content[2];
					type = 1;		//Column
					isNewCuboid = true;
				} else if (line.startsWith("g __:")) {
					content = line.split(":");
					ID = content[2];
					type = 2;		//Slab
					isNewCuboid = true;
				} else if(line.startsWith("v ") && isNewCuboid) {
					String[] coordinate = line.split(" "); 
					CoordinateOfPoint point = new CoordinateOfPoint();
					point.setX(Double.parseDouble(coordinate[1]));
					point.setY(Double.parseDouble(coordinate[2]));
					point.setZ(Double.parseDouble(coordinate[3])); 
					listPoints.add(point); 
					i++;
				} else if (line.startsWith("usemt")) {
					
					cuboid = new Cuboid(Integer.parseInt(ID), type);
					int numOfcuboidPoint = i - mark;	 //计算该几何体的点数
					for (int k = 0; k < numOfcuboidPoint; k++) {
						cuboid.addPoint(listPoints.get(mark++));
						cuboid.setType(type);
					}
					cuboid.assignPoints();
					listCuboids.add(cuboid); 
					isNewCuboid = false;
				} 
			}
		} catch (Exception e) {
			// TODO: handle exception 
		}  
		return listCuboids;
	}
}
