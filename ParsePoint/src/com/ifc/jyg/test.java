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
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\IFCFile\\whole.obj");
		Map<Double, ArrayList<Triangle>> samevalueMap = new HashMap<>();
		IntersectRectangle ir = new IntersectRectangle();  
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid(); 

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
		}

		ArrayList<Polyhedron> ps = parseObjFile.getSlabPolys();
		int slabNeededRecCnt = 0;
		for (Polyhedron p:ps) {
			String matlab = p.getDownPolygon().toMatlab2D();
			//System.out.println(matlab);
			ArrayList<Rectangle> recs = p.getNeededRectangles();
			for(Rectangle r:recs) {
				ir.addRectangleTogether(r);
				slabNeededRecCnt++;
			}

		}

		Map<Map<Rectangle, Rectangle>, String> testMap = ir.getIntersectMap();
		ArrayList<ArrayList<TreeSet<Rectangle>>> intersectResult = ir.getPartitionResult();
		//System.out.println("intersectResult "+intersectResult);
		System.out.println("intersectResult.size() : " + intersectResult.size());
		int i=0;
		for(ArrayList<TreeSet<Rectangle>> recSetList:intersectResult) {
			for(TreeSet<Rectangle> recSet:recSetList) {
				i++;
				System.out.println(String.format("figure(%d);\n",i));
				System.out.println(String.format("title('%s %f');\n",Rectangle.directionString[recSet.first().getDirection()],recSet.first().getIntersectvalue()));
				for(Rectangle r:recSet) {
					System.out.println(r.toMatlab2d());
				}
				System.out.println("%###############\n");
			}
			System.out.println("%!!!!!!!!!!!!!!!!!\n");
		}
	}
}
