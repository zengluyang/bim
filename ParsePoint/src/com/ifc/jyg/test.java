package com.ifc.jyg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class test {
 
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\IFCFile\\YD_S_B04_1F.obj"); 
		IntersectRectangle ir = new IntersectRectangle();  
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid(); 
		System.out.println("listCuboids: " + listCuboids.size());
		for (int j = 0; j < listCuboids.size(); j++) {
			Cuboid cuboid = listCuboids.get(j);
//			/System.out.println("ID: " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			//if(cuboid.getType() != Cuboid.OTHER) {
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
			//}
		}

		ArrayList<Polyhedron> ps = parseObjFile.getSlabPolys();
		int slabNeededRecCnt = 0;
		for (Polyhedron p:ps) {
			ArrayList<Rectangle> recs = p.getNeededRectangles();
			for(Rectangle r:recs) {
				ir.addRectangleTogether(r);
				slabNeededRecCnt++;
			}

		}

		Map<Map<Rectangle, Rectangle>, String> testMap = ir.getIntersectMap();

		System.out.println("testMap.size() : " + testMap.size());
		int cnt = 0;
		for (Map<Rectangle, Rectangle> rectMap : testMap.keySet()) {
			for (Rectangle rectangle : rectMap.keySet()) {
				if(rectMap.get(rectangle)!=null) {
					cnt++;
					Rectangle a = rectangle;
					Rectangle b = rectMap.get(rectangle);
//					System.out.print(a);
//					System.out.print(b);
//
//					Polygon polygon = new Polygon(a,b);
//					System.out.println(polygon.getEdgeList());
//					System.out.println(polygon.getPointList());
//					System.out.println("###############");
				}

			}
		}
		System.out.println(cnt);
		//bw.close();
	}
}
