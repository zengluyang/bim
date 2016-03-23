package com.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


public class test {

	private static BufferedWriter bw = null;
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\IFCFile\\Whole.obj");
		bw = new BufferedWriter(new FileWriter("E:\\IFC\\objFile\\Whole.obj")); 
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid();
		for (int j = 0; j < listCuboids.size(); j++) {
			Cuboid cuboid = listCuboids.get(j);
			//System.out.println("ID: " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			if(cuboid.getType()!=Cuboid.SLAB) {
				System.out.println(cuboid.toString());
				Set<Rectangle> neededRecs = cuboid.getNeededRectangels();
				if(neededRecs!=null) {
					//System.out.println(neededRecs);
					for(Rectangle r:neededRecs) {
						System.out.println(r);
					}
				}
			}
			bw.write("ID: " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			bw.newLine();
			TreeSet<CoordinateOfPoint> treeSetCop = cuboid.getPoint();
			Iterator<CoordinateOfPoint> iterator = treeSetCop.iterator(); 
			while (iterator.hasNext()) {
				CoordinateOfPoint cp = (CoordinateOfPoint) iterator.next();
				//System.out.println(cp.toString());
				bw.write(cp.toString());
				bw.newLine();
				bw.flush();
			}
			bw.newLine();
			System.out.println();
		}
		bw.close();
	}
}
