package com.ifc.jyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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


	/*
		COL_____
		BEA_____
		__:___				poly slab
		__:_____-_			cuboid slab

	 */
	private void initObjParameter() {
		File file = new File(fileName);
		try {
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			String content = null;
			while ((content = br.readLine()) != null) {
				if (content.startsWith("g ____-")) {	//统锟狡筹拷锟斤拷锟斤拷母锟斤拷锟�
					cuboidNumber++;
				} else if (content.startsWith("v ")) {	//统锟狡碉拷母锟斤拷锟�
					pointNumber++;
				} else if (content.startsWith("g __:")) {	//统锟斤拷Slab锟侥革拷锟斤拷
					slabNumber++;
				} else if (content.startsWith("f ")) {	//统锟斤拷Slab锟侥革拷锟斤拷
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
						int numOfcuboidPoint = i - mark;	 //锟斤拷锟斤拷眉锟斤拷锟斤拷锟侥碉拷锟斤拷
						for (int k = 0; k < numOfcuboidPoint; k++) {
							cuboid.addPoint(listPoints.get(mark++));
							cuboid.setType(type);
						}
						cuboid.assignPoints();
						listCuboids.add(cuboid); 
						isNewCuboid = false;
					}  
				}  else if (line.startsWith("f ") && type == 2) {	//锟斤拷取锟斤拷应锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷伪锟绞�
					
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
			ArrayList<Triangle> listTriangles = new ArrayList<Triangle>();
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
			 slabMap.put(i, listTriangles);
		}
		return slabMap;
	}

	private static Polyhedron constructFromTrianglesOfOneSlab (ArrayList<Triangle> listTriangles) {
		Map<Double, Map<Edge, Integer>> dmeMap = new TreeMap<Double, Map<Edge,Integer>>();	//double 锟斤拷示z值 锟斤拷 Map<Edge,int>锟斤拷示锟竭和讹拷应锟斤拷锟斤拷目
 		for (Triangle triangle : listTriangles) {
			if (triangle.getDirection() == Triangle.UP_DOWN) {
				ArrayList<Edge> edges = triangle.getEdges();
				for (Edge edge : edges) {
					Double z = edge.getFirstPoint().getZ();

					if(!dmeMap.containsKey(z)) {
						dmeMap.put(z, new TreeMap<Edge, Integer>());

					}
					Map<Edge, Integer> ecmMap = dmeMap.get(z);
					Integer cnt = ecmMap.get(edge);
					if(cnt!=null) {
						//int cnt = ecmMap.get(edge) + 1;
						ecmMap.put(edge, cnt+1);
					} else {
						ecmMap.put(edge, 1);
					}

				} 
			}
		}
		ArrayList<Edge> listNewEdges = new ArrayList<Edge>();
		if(dmeMap.size()!=2) {
			System.out.println("dmeMap.size()!=2 error!");
		}
		Double lowerZ=0.0;
		Double higherZ=0.0;

		int i=0;
		for(Double d:dmeMap.keySet()) {
			if(i==0) {
				lowerZ = d;
			} else if(i==1) {
				higherZ = d;
			}
			i++;
		}
		Map<Edge, Integer> ecm = dmeMap.get(lowerZ);
		for (Edge edge : ecm.keySet()) {
			if (ecm.get(edge) == 1) {
				listNewEdges.add(edge);
			}
		}
		Polygon polygon = new Polygon(listNewEdges);
		double height = higherZ-lowerZ;
		Polyhedron polyhedron = new Polyhedron(polygon,height);
		return polyhedron;
	}

	public ArrayList<Polyhedron> getSlabPolys() {
		ArrayList<Polyhedron> rlt = new ArrayList<Polyhedron>();
		Map<Integer, ArrayList<Triangle>> slabMap = this.getSlabs();	//integer 锟斤拷示slab锟斤拷锟斤拷锟叫碉拷triangle锟侥革拷锟斤拷
		//System.out.println("listSlabs size:" + slabMap.size());
		for (int number : slabMap.keySet()) {
			ArrayList<Triangle> listTriangles = slabMap.get(number);
			Polyhedron p = this.constructFromTrianglesOfOneSlab(listTriangles);
			rlt.add(p);
		}
		return rlt;
	}
	
	 
}
