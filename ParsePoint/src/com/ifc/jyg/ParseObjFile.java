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
 
	
	private ArrayList<CoordinateOfPoint> listPoints =  new ArrayList<CoordinateOfPoint>();;
	private ArrayList<Cuboid> listCuboids = new ArrayList<Cuboid>(); 
	private Map<String, ArrayList<Triangle>> slabMap = new HashMap<String, ArrayList<Triangle>>();
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

	
	public ArrayList<Cuboid> getCuboid() {
		//initObjParameter();
		int triangleNumber = 0;
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
					if (triangleNumber != 0) {
						triangleNumberList.add(triangleNumber); 
						isSlabType = false;
						triangleNumber = 0;

					}
					
				} else if (line.startsWith("g COL_____")) {
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 1;		//Column 
					if (triangleNumber != 0) {
						triangleNumberList.add(triangleNumber);
						isSlabType = false;
						triangleNumber = 0;

					}
					
				} else if (line.startsWith("g __:_____-_")) {	//cuboid slab
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 2; 
					if (triangleNumber != 0) {
						triangleNumberList.add(triangleNumber);
						isSlabType = false;
						triangleNumber = 0;
					} 
				} else if (line.startsWith("g __:___")) {
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 3;	 
					if (triangleNumber != 0) {
						triangleNumberList.add(triangleNumber); 
						isSlabType = false;
						triangleNumber = 0;
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
				} else if (line.startsWith("usemt")) {

					if (cuboidNumber == 8) {	//not poly Slab
						Cuboid cuboid = new Cuboid(ID, type);
						if(ID.equals("COL_____:600_x_600mm:566092")) {
							System.out.println("");
						}
						for (int j = 8; j > 0; j--) {
							cuboid.addPoint(listPoints.get(i - j));
							cuboid.setType(type);
						}
						cuboid.assignPoints();
						listCuboids.add(cuboid);
					}
				} else if (line.startsWith("f ")) {	//锟斤拷取锟斤拷应锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷伪锟绞�
					if (cuboidNumber > 8) {
						isSlabType = true;
						String[] location = line.split(" ");
						int a = Integer.parseInt(location[1].split("//")[0]);
						int b = Integer.parseInt(location[2].split("//")[0]);
						int c = Integer.parseInt(location[3].split("//")[0]);
						MarkLocation markLocation = new MarkLocation(a, b, c, ID);  
						//System.out.println("test ID : " + ID);
						if(ID!=null && ID.equals("__:___-_120mm:699191:65")) {
							System.out.println("test ID : " + ID);
						}
						listMarkLocations.add(markLocation);	  				
						triangleNumber++;  
					}   
				}
			} 
			if (isSlabType) { 
				triangleNumberList.add(triangleNumber);
			}  
		} catch (Exception e) {
			// TODO: handle exception 
		}
		return listCuboids;
	}
	
	public Map<String, ArrayList<Triangle>> getSlabs() {
		
		String ID = null;
		for (int i = 0; i < triangleNumberList.size(); i++) {

			ArrayList<Triangle> listTriangles = new ArrayList<Triangle>();
			//System.out.println("test triangleNumberList.get(i):" + triangleNumberList.get(i));
			 for (int j = 0; j < triangleNumberList.get(i); j++) {
				MarkLocation markLocation = listMarkLocations.get(j); 
				ID = markLocation.getID();
				 if(ID!=null && ID.equals("__:___-_120mm:699191:65")) {
					 ID.charAt(0);
				 }
				int a = markLocation.getA()-1;
				int b = markLocation.getB()-1;
				int c = markLocation.getC()-1;
 				if(listPoints.get(a).compareTo(listPoints.get(b))==0) {
					continue;
				}
				if(listPoints.get(b).compareTo(listPoints.get(c))==0) {
					continue;
				}
				if(listPoints.get(a).compareTo(listPoints.get(c))==0) {
					continue;
				}
				Triangle triangle = new Triangle(listPoints.get(a), listPoints.get(b), listPoints.get(c));
				listTriangles.add(triangle);
//				System.out.println("" +( a + 1) + " " + (b+1) + " " + (c+1) + " " + markLocation.getID()); 
//				System.out.println(listPoints.get(a).toString() + listPoints.get(b ).toString() + listPoints.get(c).toString());
//				System.out.println();

			 }
//			 System.out.println("test listTriangles.size()" + listTriangles.size());
			 slabMap.put(ID, listTriangles);
			 //System.out.println("getSlabs id : " + ID);
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
		Map<String, ArrayList<Triangle>> slabMap = this.getSlabs();	//integer 锟斤拷示slab锟斤拷锟斤拷锟叫碉拷triangle锟侥革拷锟斤拷
		//System.out.println("listSlabs size:" + slabMap.size());
		for (String id : slabMap.keySet()) {
			ArrayList<Triangle> listTriangles = slabMap.get(id);
			if(id.equals("__:___-_120mm:699191:65")) {
				id.charAt(0);
			}
			Polyhedron p = this.constructFromTrianglesOfOneSlab(listTriangles);
			p.Id = id;
			rlt.add(p);
		}
		return rlt;
	}
	
	 
}
