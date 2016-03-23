package test.ifc.jyg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ParseBeamPoint {

	private Beam[] beams;
	private CoordinateOfPoint[] points;
	private int beamNumber = 0;
	private File file;
	private InputStreamReader isr;
	private BufferedReader br; 
	private BufferedWriter bw; 
	
	private void initBeamParameter(String fileName) {
		file = new File(fileName); 
		try ( FileInputStream fin = new FileInputStream(file) )
		{
			
			isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			String line = null; 
			while ((line = br.readLine()) != null) { 
				if (line.startsWith("usemtl")) {
					beamNumber++;					
				}  
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		beams = new Beam[beamNumber];
		for (int i = 0; i < beamNumber; i++) {
			beams[i] = new Beam();	
		}
		int pointNumber = 8 * beamNumber;
		points = new CoordinateOfPoint[pointNumber];
		//System.out.println("beamNumber : " + beamNumber);
	}
	
	public Beam[] getBeamPoint(String fileName) {
	
		initBeamParameter(fileName);
		try ( FileInputStream fin = new FileInputStream(file) )
		{
			isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(new FileWriter("E:\\IFC\\Beam2.obj"));
			String line = null; 
			int i = 0;
			int j = 0; 
			while ((line = br.readLine()) != null) { 
				if (line.startsWith("v ")) { 
					bw.write(line);
					bw.newLine();
					bw.flush();
					//System.out.println(line);
					String[] coordinate = line.split(" ");  
					points[i] = new CoordinateOfPoint();
					
					//将 X、Y、Z放在一个column点的坐标里面,总共8个点
					
					points[i].setX(Double.parseDouble(coordinate[1]));
					points[i].setY(Double.parseDouble(coordinate[2]));
					points[i].setZ(Double.parseDouble(coordinate[3]));					
										
					if ((i % 8 ) == 7 && j < beamNumber) { 
						//将柱的八个点存储到columnPoint中
						CoordinateOfPoint[] columnPoint = new CoordinateOfPoint[8]; 
						for (int k = 0; k < beamNumber; k++) { 
							if (k == j) { 
								
								for (int l = 0; l < 8; l++) {   
									columnPoint[l] = new CoordinateOfPoint(); 
									
									int num = j * 8 + l; 
									columnPoint[l].setX(points[num].getX()); 
									columnPoint[l].setY(points[num].getY());
									columnPoint[l].setZ(points[num].getZ());   
								}  
								beams[j++].setPoints(columnPoint);
								//System.out.println("j : " + j);
								break;
							} 
						}   
					} 
					i++;
				}  
			}
			isr.close();
			br.close();
			bw.close();
		} catch (Exception e) {
			// TODO: handle exception
		} 
		return beams;
	}
}
