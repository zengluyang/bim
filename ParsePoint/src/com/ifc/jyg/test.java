package com.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class test {

	private static BufferedWriter bw = null;
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\Whole2.obj");
		bw = new BufferedWriter(new FileWriter("E:\\IFC\\Whole3.obj")); 
		Cuboid[] cuboids = parseObjFile.getCuboid();
		//System.out.println(cuboids.length);
		for (Cuboid cuboid : cuboids) {
			System.out.println("cuboid.getCuboidID() : " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			bw.write("cuboid.getCuboidID() : " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			bw.newLine();
			CoordinateOfPoint[] points = cuboid.getPoint();
			/*boolean eqs = points[0].getX() == points[1].getX();
			System.out.println(eqs);*/
			for (CoordinateOfPoint point : points) {
				System.out.println("x:" + point.getX() + " y:" + point.getY() + " z:" + point.getZ());
				bw.write("x:" + point.getX() + " y:" + point.getY() + " z:" + point.getZ());
				bw.newLine();
				bw.flush();
			}
			bw.newLine();
		}
		bw.close();
	}
}
