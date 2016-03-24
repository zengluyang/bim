package com.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class test {

	private static BufferedWriter bw = null;
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\IFCFile\\Whole.obj");
		bw = new BufferedWriter(new FileWriter("E:\\IFC\\objFile\\Whole1.obj")); 
		IntersectRectangle ir = new IntersectRectangle();
		ObtainIntersectRectangle obtain = new ObtainIntersectRectangle();
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid(); 
		//System.out.println("listCuboids.size() : " +listCuboids.size());
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
		bw.close();
	}
}
