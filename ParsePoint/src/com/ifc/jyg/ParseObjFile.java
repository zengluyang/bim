package com.ifc.jyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ParseObjFile {

	private String fileName; 
	private int triangleNumber = 0;
 
	
	private ArrayList<CoordinateOfPoint> listPoints =  new ArrayList<CoordinateOfPoint>();;
	private ArrayList<Cuboid> listCuboids = new ArrayList<Cuboid>(); 
	private Map<Integer, ArrayList<Triangle>> slabMap = new HashMap<Integer, ArrayList<Triangle>>();
	private ArrayList<MarkLocation> listMarkLocations = new ArrayList<MarkLocation>(); 
	private BufferedReader br;
	private ArrayList<Integer> triangleNumberList = new ArrayList<>();
	public ParseObjFile(String fileName) {
		this.fileName = fileName;
	}


	/*
		COL_____
		BEA_____
		__:___				poly slab
		__:_____-_			cuboid slab

	 */
//	private void initObjParameter() {
//		File file = new File(fileName);
//		try {
//			FileInputStream fin = new FileInputStream(file);
//			InputStreamReader isr = new InputStreamReader(fin);
//			br = new BufferedReader(isr);
//			String content = null;
//			while ((content = br.readLine()) != null) {
//				if (content.startsWith("g COL_____")) {	//Column number
//					columnNumber++;
//				} else if (content.startsWith("g BEA_____")) { //Beam Number
//					beamNumber++;
//				} else if (content.startsWith("g __:_____-_")) {	//cuboid slab
//					cuboidSlabNumber++;
//				} else if (content.startsWith("g __:___")) { 	//Slab Number 
//					slabNumber++;
//				} else if (content.startsWith("v ")) {	//统锟狡碉拷母锟斤拷锟�
//					pointNumber++;
//				} else if (content.startsWith("f ")) {	//统锟斤拷Slab锟侥革拷锟斤拷
//					triangleNumber++;
//				} 
//			}
//		} catch (IOException e) { 
//			e.printStackTrace();
//		} 
//		 
//		listPoints = new ArrayList<CoordinateOfPoint>(pointNumber);
//		listCuboids = new ArrayList<Cuboid>();
//		listSlabs = new ArrayList<Slab>(slabNumber);
//		listMarkLocations = new ArrayList<MarkLocation>();
//		//triNumber = new int[slabNumber];  
//		slabMap = new HashMap<Integer, ArrayList<Triangle>>();
//		System.out.println( " beamNumber: " + beamNumber + 
//							" cuboidSlabNumber: " + cuboidSlabNumber +
//							" columnNumber: " + columnNumber +
//							" slabNumber: " + slabNumber);
//	}
//	
	
	public ArrayList<Cuboid> getCuboid() {
		//initObjParameter();
		try {
			String line = null;
			String ID = null;			
			String[] content = null;
			int i = 0;   
			int type = 0;  
			boolean isSlabType = false; 
			int cuboidNumber = 0;
			File file = new File(fileName);
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin);
			br = new BufferedReader(isr); 
			while ((line = br.readLine()) != null) {
				if (line.startsWith("g BEA_____")) {
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 0;		//Beam 
					if (isSlabType && triangleNumber != 0) { 
						triangleNumberList.add(triangleNumber); 
						isSlabType = false; 
					}
					
				} else if (line.startsWith("g COL_____")) {
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 1;		//Column 
					if (isSlabType && triangleNumber != 0) {  
						triangleNumberList.add(triangleNumber);
						isSlabType = false; 
					}
					
				} else if (line.startsWith("g __:_____-_")) {	//cuboid slab
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 2; 
					if (isSlabType && triangleNumber != 0) {  
						triangleNumberList.add(triangleNumber);
						isSlabType = false; 
					} 
				} else if (line.startsWith("g __:___")) { //poly slab  
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 3;	 
					if (isSlabType && triangleNumber != 0) {  
						triangleNumberList.add(triangleNumber);
						isSlabType = false; 
					} 
				} else if(line.startsWith("v ")) {
					String[] coordinate = line.split(" "); 
					CoordinateOfPoint point = new CoordinateOfPoint();
					point.setX(Double.parseDouble(coordinate[1]));
					point.setY(Double.parseDouble(coordinate[2]));
					point.setZ(Double.parseDouble(coordinate[3])); 
					listPoints.add(point); 
					cuboidNumber++;
					i++;
				} else if (line.startsWith("f ")) {	//锟斤拷取锟斤拷应锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷伪锟绞�
					
					if (cuboidNumber == 8) {	//not poly Slab   
						Cuboid cuboid = new Cuboid(ID, type);
						for (int j = 8; j > 0; j--) {
							cuboid.addPoint(listPoints.get(i - j));
							cuboid.setType(type);
						} 
						cuboid.assignPoints();
						listCuboids.add(cuboid);
					} else if (cuboidNumber > 8) { 
						isSlabType = true;
						String[] location = line.split(" ");
						int a = Integer.parseInt(location[1].split("//")[0]);
						int b = Integer.parseInt(location[2].split("//")[0]);
						int c = Integer.parseInt(location[3].split("//")[0]); 
						MarkLocation markLocation = new MarkLocation(a, b, c);  
						listMarkLocations.add(markLocation);	  				
						triangleNumber++;  
					}   
				}
			} 
			if (isSlabType) {
				System.out.println("errrrrrrrrrrrrr");
				triangleNumberList.add(triangleNumber);
			}  
		} catch (Exception e) {
			// TODO: handle exception 
		}  
//		System.out.println("triangleNumberList size:" + triangleNumberList.size() + 
//						" listCuboids size:" + listCuboids.size() );
		for(CoordinateOfPoint p:listPoints) {
			System.out.println(String.format("v %f %f %f",p.getX(),p.getY(),p.getZ()));
		}
		return listCuboids;
	}
	
	public Map<Integer, ArrayList<Triangle>> getSlabs() {
		
	 
		for (int i = 0; i < triangleNumberList.size(); i++) {
			ArrayList<Triangle> listTriangles = new ArrayList<Triangle>();
			//System.out.println("test triangleNumberList.get(i):" + triangleNumberList.get(i));
			 for (int j = 0; j < triangleNumberList.get(i); j++) {
				MarkLocation markLocation = listMarkLocations.get(j);
				int a = markLocation.getA()-1;
				int b = markLocation.getB()-1;
				int c = markLocation.getC()-1;
 				if(listPoints.get(a).compareTo(listPoints.get(b))==0) {
					break;
				}
				if(listPoints.get(b).compareTo(listPoints.get(c))==0) {
				 	break;
				}
				if(listPoints.get(a).compareTo(listPoints.get(c))==0) {
					break;
				}
				Triangle triangle = new Triangle(listPoints.get(a), listPoints.get(b), listPoints.get(c));
				listTriangles.add(triangle);
//				System.out.println("" +( a + 1) + " " + (b+1) + " " + (c+1)); 
//				System.out.println(listPoints.get(a).toString() + listPoints.get(b ).toString() + listPoints.get(c).toString());
//				System.out.println();
			 }
//			 System.out.println("test listTriangles.size()" + listTriangles.size());
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
			System.out.println(";dmeMap.size()!=2 error!");
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
