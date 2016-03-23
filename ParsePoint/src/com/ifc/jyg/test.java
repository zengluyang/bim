package com.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class test {

	private static BufferedWriter bw = null;
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\Beam.obj");
		bw = new BufferedWriter(new FileWriter("E:\\IFC\\Beam1.obj")); 
		Cuboid[] cuboids = parseObjFile.getCuboid();
		//System.out.println(cuboids.length);
		for (Cuboid cuboid : cuboids) {
			System.out.println("cuboid.getCuboidID() : " + cuboid.getCuboidID());
			bw.write("cuboid.getCuboidID() : " + cuboid.getCuboidID());
			bw.newLine();
			CoordinateOfPoint[] points = cuboid.getPoint();
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
