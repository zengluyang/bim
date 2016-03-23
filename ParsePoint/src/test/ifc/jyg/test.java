package test.ifc.jyg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class test {

	private static CoordinateOfPoint[] points = new CoordinateOfPoint[8];
	private static Column[] columns = new Column[6];
	private static Beam[] beams = new Beam[7];
	private static BufferedWriter bw;
	
	public static void main(String[] args) throws IOException {
	 
 		/*bw = new BufferedWriter(new FileWriter("E:\\IFC\\Column3.obj")); 
		ParseColumnPoint parseColumnPoint = new ParseColumnPoint();
		columns = parseColumnPoint.getColumnPoint("E:\\IFC\\Column1.obj"); 
		 
		for (Column column : columns) {
			
			points  = column.getPoints(); 
			for (CoordinateOfPoint point : points) { 
				 
				bw.write("v " + point.getX() + " " + point.getY() + " " + point.getZ() + "\r");
				bw.flush();
			} 
		}
		bw.close();
		 */
		
 		bw = new BufferedWriter(new FileWriter("E:\\IFC\\Beam3.obj")); 
 		ParseBeamPoint parseBeamPoint = new ParseBeamPoint();
 		beams = parseBeamPoint.getBeamPoint("E:\\IFC\\Beam.obj");
 		
 		//System.out.println("beams.length : " + beams.length);
 		
 		for (Beam beam : beams) {
			points = beam.getPoints();
			//System.out.println("points.length : " + points.length);
			for (CoordinateOfPoint point : points) { 
				 
				bw.write("v " + point.getX() + " " + point.getY() + " " + point.getZ() + "\r");
				bw.flush();
			} 
		}
 		bw.close();
	}
}
