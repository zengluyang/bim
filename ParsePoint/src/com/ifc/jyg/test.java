package com.ifc.jyg;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

class StreamGobbler extends Thread
{
	InputStream is;
	String type;

	StreamGobbler(InputStream is, String type)
	{
		this.is = is;
		this.type = type;
	}

	public void run()
	{
		try
		{
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line=null;
			while ( (line = br.readLine()) != null)
				System.out.println("%"+type + ">" + line);
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}

public class test {
 
	public static void main(String[] args) throws IOException {
		String ifcConvertExeName = "E:\\cygwin64\\home\\ZLY\\IfcConvert.exe";
		String inputIfcFileName = "E:\\IFC\\IFCFile\\YD_S_B04_1F.ifc";
		String outputObjFileName = "E:\\IFC\\IFCFile\\YD_S_B04_1F.obj";

		File inputIfcFile = new File(inputIfcFileName);
		File outputObjFile = new File(outputObjFileName);
		if(outputObjFile.lastModified()<inputIfcFile.lastModified()) {
			System.out.println("Objfile out-of-date, converting using "+ifcConvertExeName);
			StringBuilder ifcConverteExePathSb = new StringBuilder();
			ifcConverteExePathSb
					.append("cmd /c ").append(ifcConvertExeName).append(" ")
					.append(inputIfcFileName).append(" ")
					.append(outputObjFileName).append(" ")
					.append("--use-world-coords ")
					.append("--weld-vertices ")
					.append("--include --entities IfcBeam IfcColumn IfcSlab");

			try {
				String line;
				Process p = Runtime.getRuntime().exec(ifcConverteExePathSb.toString());
				StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");

				// any output?
				StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
				errorGobbler.start();
				int exitVal = p.waitFor();
				p.waitFor();
			} catch (Exception err) {
				err.printStackTrace();
			}
		} else {
			System.out.println("Objfile up-to-date.");
		}
		ParseObjFile parseObjFile = new ParseObjFile(outputObjFileName);
		BufferedWriter br = new BufferedWriter(new FileWriter("E:\\IFC\\IFCFile\\YD_S_B04_1F_out.txt"));
		IntersectRectangle ir = new IntersectRectangle();  
		ArrayList<Cuboid> listCuboids = parseObjFile.getCuboid(); 
//		System.out.println(";listCuboids: " + listCuboids.size());
		ArrayList<Rectangle> recRlt = new ArrayList<Rectangle>();
		ArrayList<Polygon> polyRlt = new ArrayList<Polygon>();

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
			polyRlt.add(p.getDownPolygon());
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
				if(recSet.size()==1) {
					i++;
					recSet.size();
					Rectangle rec = recSet.first();
					recRlt.add(rec);
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
					Rectangle big = recSet.pollFirst();
					ArrayList<Polygon> polygons = Rectangle.contrunctPolygonsUsingBigRectangleAndSmallRectangles(big, new ArrayList<Rectangle>(recSet));
					for(Polygon p: polygons) {
						i++;
						polyRlt.add(p);
						System.out.println(String.format("figure(%d);\n",i));
						System.out.println(String.format("title('%s %f %s');\n",Rectangle.directionString[big.getDirection()],big.getIntersectvalue(),p.Id));
						System.out.println(p.toMatlab2D());
					}
					System.out.println("%###############\n");

				}
			}
			System.out.println("%!!!!!!!!!!!!!!!!!\n");
		}
		System.out.println("%i "+i);
	}
}
