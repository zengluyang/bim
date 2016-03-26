package com.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class test {
 
	public static void main(String[] args) throws IOException {
		ParseObjFile parseObjFile = new ParseObjFile("E:\\IFC\\IFCFile\\YD_S_B04_1F.obj"); 
		BufferedWriter br = new BufferedWriter(new FileWriter("E:\\IFC\\IFCFile\\YD_S_B04_1F_out.txt"));
		IntersectRectangle ir = new IntersectRectangle();  
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid(); 
		System.out.println("listCuboids: " + listCuboids.size());
		for (int j = 0; j < listCuboids.size(); j++) {
			Cuboid cuboid = listCuboids.get(j);
			if(cuboid.getCuboidID()!=null && cuboid.getCuboidID().equals("COL_____:600_x_600mm:566092")) {
				cuboid.getPoint();
			}
//			/System.out.println("ID: " + cuboid.getCuboidID() + " type : " + cuboid.getType());
			//if(cuboid.getType() != Cuboid.OTHER) {
				//System.out.println(cuboid.toString());
				Set<Rectangle> neededRecs = cuboid.getNeededRectangels();
				//System.out.println("neededRecs : " + neededRecs.size());
				if(neededRecs!=null) {
					//System.out.println(neededRecs);
					for(Rectangle r : neededRecs) {
						ir.addRectangleTogether(r);
						if(r.Id!=null && r.Id.equals("__:___-_120mm:699191:65")){
							r.toMatlab2d();
						}
						//obtain.addRectangleTogether(r); 
						//System.out.println(r);
					}
				}
			//}
		}

		ArrayList<Polyhedron> ps = parseObjFile.getSlabPolys();
		int slabNeededRecCnt = 0;
		int i=0;
		for (Polyhedron p:ps) {
			i++;
			if(p.Id!=null && p.Id.equals("__:___-_120mm:699191:65")) {
				p.toString();
			}
			System.out.println(String.format("figure(%d);\n",i));
			System.out.println(String.format("title('%s %f %s');\n",Rectangle.directionString[p.getDownPolygon().getDirection()],p.height,p.Id));
			System.out.println(p.getDownPolygon().toMatlab2D());
			System.out.println("%###############\n");
			String matlab = p.getDownPolygon().toMatlab2D();
			//System.out.println(matlab);
			ArrayList<Rectangle> recs = p.getNeededRectangles();

			for(Rectangle r:recs) {
				if(r.Id!=null && r.Id.equals("__:___-_120mm:699191:65")) {
					r.toMatlab2d();
				}
				ir.addRectangleTogether(r);
				slabNeededRecCnt++;
			}

		}



		ArrayList<ArrayList<TreeSet<Rectangle>>> intersectResult = ir.getPartitionResult();
		//System.out.println("intersectResult "+intersectResult);
		System.out.println("intersectResult.size() : " + intersectResult.size());
		int total_cnt=0;
		for(ArrayList<TreeSet<Rectangle>> recSetList:intersectResult) {
			for(TreeSet<Rectangle> recSet:recSetList) {

//				System.out.println(String.format("figure(%d);\n",i));
//				System.out.println(String.format("title('%s %f');\n",Rectangle.directionString[recSet.first().getDirection()],recSet.first().getIntersectvalue()));
//				for(Rectangle r:recSet) {
//					System.out.println(r.toMatlab2d());
//				}
//				System.out.println("%###############\n");
				total_cnt++;
				if(recSet.size()==1) {
					recSet.size();
					Rectangle rec = recSet.first();
					if(rec.Id!=null && rec.Id.equals("BEA_____:200_x350mm:736737")) {
						rec.toMatlab2d();
					}
					System.out.println(String.format("figure(%d);\n",i));
					System.out.println(String.format("title('%s %f %s');\n",Rectangle.directionString[rec.getDirection()],rec.getIntersectvalue(),rec.Id));
					System.out.println(rec.toMatlab2d());
					System.out.println("%###############\n");
				} else {
					Rectangle rec = recSet.first();
					if(rec.Id!=null && rec.Id.equals("COL_____:600_x_600mm:566092")) {
						rec.toMatlab2d();
					}
					i++;
					Rectangle big = recSet.pollFirst();
					ArrayList<Polygon> polygons = Rectangle.contrunctPolygonsUsingBigRectangleAndSmallRectangles(big, new ArrayList<Rectangle>(recSet));
					for(Polygon p: polygons) {
						System.out.println(String.format("figure(%d);\n",i));
						System.out.println(String.format("title('%s %f %s');\n",Rectangle.directionString[big.getDirection()],big.getIntersectvalue(),p.Id));
						System.out.println(p.toMatlab2D());
					}
					System.out.println("%###############\n");

				}
			}
			System.out.println("%!!!!!!!!!!!!!!!!!\n");
		}
		System.out.println("%total_cnt "+total_cnt);
	}
}
