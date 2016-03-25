package com.ifc.jyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseObjFile {

	private String fileName;
	private int pointNumber = 0;
	private int cuboidNumber = 0;
	private int triangleNumber = 0;
	private int slabNumber = 0;
	private int[] triNumber;
	
	private ArrayList<CoordinateOfPoint> listPoints;
	private ArrayList<Cuboid> listCuboids;
	private ArrayList<Triangle> listTriangles;
	private ArrayList<Slab> listSlabs; 
	private Map<Integer, ArrayList<Triangle>> slabMap;
	private ArrayList<MarkLocation> listMarkLocations; 
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
				if (content.startsWith("g ____-")) {	//统计长方体的个数
					cuboidNumber++;
				} else if (content.startsWith("v ")) {	//统计点的个数
					pointNumber++;
				} else if (content.startsWith("g __:")) {	//统计Slab的个数
					slabNumber++;
				} else if (content.startsWith("f ")) {	//统计Slab的个数
					//triangleNumber++;
				} 
			}
		} catch (IOException e) { 
			e.printStackTrace();
		} 
		listPoints = new ArrayList<CoordinateOfPoint>(pointNumber);
		listCuboids = new ArrayList<Cuboid>(cuboidNumber);
		listSlabs = new ArrayList<Slab>(slabNumber);
		listMarkLocations = new ArrayList<MarkLocation>();
		listTriangles = new ArrayList<Triangle>();
		triNumber = new int[slabNumber];  
		slabMap = new HashMap<Integer, ArrayList<Triangle>>();
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
			int slabNum = 0;
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
					System.out.println(triangleNumber);
					if (triangleNumber != 0) {
						triNumber[slabNum] = triangleNumber; 
						triangleNumber = 0;
						slabNum++;
					}
					
				} else if(line.startsWith("v ") && isNewCuboid) {
					String[] coordinate = line.split(" "); 
					CoordinateOfPoint point = new CoordinateOfPoint();
					point.setX(Double.parseDouble(coordinate[1]));
					point.setY(Double.parseDouble(coordinate[2]));
					point.setZ(Double.parseDouble(coordinate[3])); 
					listPoints.add(point); 
					i++;
				} else if (line.startsWith("usemt")) {
					
					if (type != 2) {	//not Slab 
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
				}  else if (line.startsWith("f ") && type == 2) {	//获取对应顶点的三角形表示
					
					String[] location = line.split(" ");
					int a = Integer.parseInt(location[1].split("//")[0]);
					int b = Integer.parseInt(location[2].split("//")[0]);
					int c = Integer.parseInt(location[3].split("//")[0]);  
					MarkLocation markLocation = new MarkLocation(a, b, c);
					listMarkLocations.add(markLocation);
					
					triangleNumber++; 
				}
			}    
			triNumber[slabNumber-1] = triangleNumber;
		} catch (Exception e) {
			// TODO: handle exception 
		}  
		return listCuboids;
	}
	
	public Map<Integer, ArrayList<Triangle>> getSlabs() {
		
	 
		for (int i = 0; i < triNumber.length; i++) {
			 for (int j = 0; j < triNumber[i]; j++) {
				MarkLocation markLocation = listMarkLocations.get(j);
				int a = markLocation.getA()- 1;
				int b = markLocation.getB()-1;
				int c = markLocation.getC()-1;
				Triangle triangle = new Triangle(listPoints.get(a), listPoints.get(b), listPoints.get(c));
				listTriangles.add(triangle);
//				System.out.println("" +( a + 1) + " " + (b+1) + " " + (c+1)); 
//				System.out.println(listPoints.get(a).toString() + listPoints.get(b ).toString() + listPoints.get(c).toString());
//				System.out.println();
			 }
			 slabMap.put(triNumber[i], listTriangles); 
			 break;
		} 
		 return slabMap;
	}
	
	 
}
