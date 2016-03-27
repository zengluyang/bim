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
	private ArrayList<Triangle> listTriangles = new ArrayList<Triangle>();
	private BufferedReader br;
	private ArrayList<Integer> triangleNumberList = new ArrayList<>();
	private Map<String, Integer> idNumberMap = new HashMap<>();
	private TreeMap<String, ArrayList<Triangle>> idTriangleList = new TreeMap<>();
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
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (line.startsWith("g BEA_____")) {
					
					if (triangleNumber != 0) {
						triangleNumberList.add(triangleNumber); 
						idNumberMap.put(ID, triangleNumber);
						isSlabType = false;
						triangleNumber = 0;

					}
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 0;		//Beam
					
				} else if (line.startsWith("g COL_____")) {
					
					if (triangleNumber != 0) {
						triangleNumberList.add(triangleNumber);
						idNumberMap.put(ID, triangleNumber);
						isSlabType = false;
						triangleNumber = 0;

					}
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 1;		//Column 
				} else if (line.startsWith("g __:_____-_")) {	//cuboid slab
					 
					if (triangleNumber != 0) {
						idNumberMap.put(ID, triangleNumber);
						triangleNumberList.add(triangleNumber);
						isSlabType = false;
						triangleNumber = 0;
					} 
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 2;
				} else if (line.startsWith("g __:___")) {
					 
					if (triangleNumber != 0) {
						idNumberMap.put(ID, triangleNumber);
						triangleNumberList.add(triangleNumber); 
						isSlabType = false;
						triangleNumber = 0;
					} 
					cuboidNumber = 0;
					content = line.split(" ");
					ID = content[1];
					type = 3;	
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
						CoordinateOfPoint pa = listPoints.get(a-1); 
						CoordinateOfPoint pb = listPoints.get(b-1); 
						CoordinateOfPoint pc = listPoints.get(c-1);

						if (ID != null && ID.equals("__:___-_120mm:716955:28")) {
							System.out.print("");
						}

						if (pa.compareTo(pb) == 0) {
							continue;
						}
						if (pa.compareTo(pc) == 0) {
							continue;
						}
						if (pb.compareTo(pc) == 0) {
							continue;
						}
						Triangle triangle = new Triangle(pa, pb, pc);
						if(!idTriangleList.containsKey(ID)) {
							ArrayList<Triangle> triList = new ArrayList<Triangle>();
							triList.add(triangle);
							idTriangleList.put(ID, triList);
						} else  {
							ArrayList<Triangle> triList = idTriangleList.get(ID);
							triList.add(triangle);
							idTriangleList.put(ID, triList);
						}
						triangleNumber++;
					}   
				}
			} 
			if (isSlabType) { 
				idNumberMap.put(ID, triangleNumber);
			}  
		} catch (Exception e) {
			// TODO: handle exception 
		}
		
//		for (String ID : idNumberMap.keySet()) {
//			System.out.println("id : "  +ID);
//		}
//		for (int j = 0; j < listTriangles.size(); j++) {
//			System.out.println(listTriangles.get(j).toString());
//		}
//		System.out.println("test listTriangles.zise : " + listTriangles.size());
		return listCuboids;
	}
	
	public Map<String, ArrayList<Triangle>> getSlabs() {
		
		for(String id:idTriangleList.keySet()) {
			ArrayList<Triangle> triList  = idTriangleList.get(id);
			if (triList.size() == 1) {
				System.out.println();
			}
			slabMap.put(id, triList);
		}
//		 int k = 0; 
//		for (String ID : idNumberMap.keySet()) {
//			int triangleNumber = idNumberMap.get(ID);
//			ArrayList<Triangle> Triangles = new ArrayList<Triangle>();
//			int j = 0;
//			while (k < listTriangles.size() && j < triangleNumber) {
//				if (k == 0) {
//					System.out.println("first Triangles :"  +listTriangles.get(j).toString());
//				} else if (k == listTriangles.size() - 1) {
//					System.out.println("finally Triangles :"  +listTriangles.get(j).toString());
//				}
//				Triangles.add(listTriangles.get(k));
//				k++;
//				j++;
//			}
//		 
//			slabMap.put(ID, Triangles);
//			
//		} 
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
//			System.out.println(";dmeMap.size()!=2 error!");
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
		if (ecm == null) {
			System.out.println("");
		}
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

			Polyhedron p = this.constructFromTrianglesOfOneSlab(listTriangles);
			p.Id = id;
			rlt.add(p);
		}
		return rlt;
	}
	
	 
}
