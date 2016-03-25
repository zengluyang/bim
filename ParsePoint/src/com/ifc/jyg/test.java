package com.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class test {

	private static BufferedWriter bw = null;
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\IFCFile\\Whole.obj");
		//bw = new BufferedWriter(new FileWriter("E:\\IFC\\objFile\\Whole1.obj")); 
		Map<Double, Map<Edge, Integer>> dmeMap = new HashMap<Double, Map<Edge,Integer>>();	//double ��ʾzֵ �� Map<Edge,int>��ʾ�ߺͶ�Ӧ����Ŀ
		Map<Edge, Integer> ecmMap = null;
		Map<Double, ArrayList<Triangle>> samevalueMap = new HashMap<>();
		IntersectRectangle ir = new IntersectRectangle();  
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid(); 
		Map<Integer, ArrayList<Triangle>> slabMap = parseObjFile.getSlabs();	//integer ��ʾslab�����е�triangle�ĸ���
		//System.out.println("listSlabs size:" + slabMap.size());
		
		for (int number : slabMap.keySet()) {
			ArrayList<Triangle> listTriangles = slabMap.get(number);
			System.out.println("listTriangles: " + listTriangles.size());
			for (Triangle triangle : listTriangles) {
				//System.out.println(triangle.getEdges());
				if (triangle.getDirection() == Triangle.UP_DOWN) {
					ArrayList<Edge> edges = triangle.getEdges();
					/*for (Edge e : edges) { 
						double z = e.getFirstPoint().getZ();
						if (z < 3) {
							System.out.println("test : " +e.getFirstPoint().toString() + e.getSecondPoint().toString());
						}
						
					}
					System.out.println();*/
					//System.out.println("dmeMap.size() :" + dmeMap.size());
					for (Edge edge : edges) {
						double z = edge.getFirstPoint().getZ();
						if (z < 3) {
							if (dmeMap.get(z) == null) {	 
								dmeMap.put(z, new HashMap<Edge, Integer>()); 
							} 
							ecmMap = dmeMap.get(z);
							boolean isNewEdge = true;
							for (Edge e : ecmMap.keySet()) {
								
								if (e.compareTo(edge) == 0) { 
									int cnt = ecmMap.get(e) + 1;
									ecmMap.put(e, cnt);
									isNewEdge = false;
									break;
								}
							}
							if (isNewEdge) { 
								ecmMap.put(edge, 1);
							}
						}
					} 
					 
//					System.out.println("ecmMap.size: " + ecmMap.size());
//					for (Edge edge : ecmMap.keySet()) { 
//						System.out.println(edge.getFirstPoint().toString() + edge.getSecondPoint().toString() + 
//								" cnt : " + ecmMap.get(edge));
//					} 
//					System.out.println();
				}
			}
		}
		ArrayList<Edge> listNewEdges = new ArrayList<Edge>();
		System.out.println("test : "  + dmeMap.size());
		for (double z : dmeMap.keySet()) {
			System.out.println("z" + z);
			Map<Edge, Integer> ecm = dmeMap.get(z);
			for (Edge edge : ecm.keySet()) {
				//System.out.println(edge.getFirstPoint().toString() + edge.getSecondPoint().toString());
				if (ecm.get(edge) == 1) {
					//listNewEdges = Edge.getNewEgdesFromTwoEdges(longer, shorter)
 
					//System.out.println(edge.getFirstPoint().toString() + edge.getSecondPoint().toString());
					listNewEdges.add(edge);
 
					listNewEdges.add(edge);
					//System.out.println(edge.getFirstPoint().toString() + edge.getSecondPoint().toString());
 
					//ecm.remove(edge);
				}else {
					//System.out.println(edge.getFirstPoint().toString() + edge.getSecondPoint().toString());
				}
			}
			System.out.println(listNewEdges.size());
			System.out.println(listNewEdges);
			Polygon polygon = new Polygon(listNewEdges);
			System.out.println(polygon.getEdgeList());
			System.out.println(polygon.getPointList());
		}
		Map<CoordinateOfPoint, Integer> points = new HashMap<>();
		for (Edge edge : listNewEdges) {
			System.out.println(edge.getFirstPoint().toString() + edge.getSecondPoint().toString());
			CoordinateOfPoint firstPoint = edge.getFirstPoint();
			CoordinateOfPoint secondpPoint = edge.getSecondPoint();
			points.put(firstPoint, 1);
			points.put(secondpPoint, 1);
		}
		System.out.println("@@@@@@@@@@");
		for (CoordinateOfPoint point : points.keySet()) {
			System.out.println(point.toString());
		}
		
		/*//System.out.println("listCuboids.size() : " +listCuboids.size());
		for (int j = 0; j < listCuboids.size(); j++) {
			Cuboid cuboid = listCuboids.get(j);
			//System.out.println("ID: " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			if(cuboid.getType()!=Cuboid.SLAB) {
				//System.out.println(cuboid.toString());
				Set<Rectangle> neededRecs = cuboid.getNeededRectangels();
				//System.out.println("neededRecs : " + neededRecs.size());
				if(neededRecs!=null) {
					//System.out.println(neededRecs);
					for(Rectangle r : neededRecs) {
						ir.addRectangleTogether(r);
						//obtain.addRectangleTogether(r); 
						//System.out.println(r);
					}
				}
			}
//			bw.write("ID: " + cuboid.getCuboidID() + " type : " + cuboid.getType());
//			bw.newLine();
			TreeSet<CoordinateOfPoint> treeSetCop = cuboid.getPoint();
			Iterator<CoordinateOfPoint> iterator = treeSetCop.iterator(); 
			while (iterator.hasNext()) {
				CoordinateOfPoint cp = (CoordinateOfPoint) iterator.next();
				//System.out.println(cp.toString());
//				bw.write(cp.toString());
//				bw.newLine();
//				bw.flush();
			}
			bw.newLine();
			//System.out.println();
		}
		//obtain.test();
		//ir.test();
		Map<Map<Rectangle, Rectangle>, String> testMap = ir.getIntersectMap();
		
		System.out.println("testMap.size() : " + testMap.size());
		int cnt = 0;
		for (Map<Rectangle, Rectangle> rectMap : testMap.keySet()) {
			for (Rectangle rectangle : rectMap.keySet()) {
				if(rectMap.get(rectangle)!=null) {
					cnt++;
				}
				System.out.print(rectangle);
				System.out.print(rectMap.get(rectangle));
				System.out.println(testMap.get(rectMap));
				System.out.println("###############");
			}
		}
		System.out.println(cnt);
		bw.close();*/
	}
}
