package test.ifc.jyg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ParseColumnPoint {
	
	private static Column[] column;
	private CoordinateOfPoint[] point; 
	private static int columnNumber = 0;
	private File file;
	private InputStreamReader isr;
	private BufferedReader br; 
	private BufferedWriter bw; 
	
	private void initColumn(String fileName) {
		file = new File(fileName); 
		try ( FileInputStream fin = new FileInputStream(file) )
		{
			
			isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			String line = null; 
			while ((line = br.readLine()) != null) { 
				if (line.startsWith("usemtl")) {
					columnNumber++;					
				}  
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		column = new Column[columnNumber];
		int pointNumber = 8 * columnNumber;
		point = new CoordinateOfPoint[pointNumber];
		for (int i = 0; i < column.length; i++) {
			column[i] = new Column();
		}
	}
	
	public Column[] getColumnPoint(String fileName) {

		initColumn(fileName);
		
		try ( FileInputStream fin = new FileInputStream(file) )
		{
			isr = new InputStreamReader(fin);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(new FileWriter("E:\\IFC\\Column2.obj"));
			String line = null; 
			int i = 0;
			int j = 0; 
			while ((line = br.readLine()) != null) { 
				if (line.startsWith("v ")) { 
					bw.write(line);
					bw.newLine();
					bw.flush();
					System.out.println(line);
					String[] coordinate = line.split(" ");  
					point[i] = new CoordinateOfPoint();
					
					//将 X、Y、Z放在一个column点的坐标里面,总共8个点
					
					point[i].setX(Double.parseDouble(coordinate[1]));
					point[i].setY(Double.parseDouble(coordinate[2]));
					point[i].setZ(Double.parseDouble(coordinate[3]));					
										
					if ((i % 8 ) == 7 && j < columnNumber) { 
						//将柱的八个点存储到columnPoint中
						CoordinateOfPoint[] columnPoint = new CoordinateOfPoint[8]; 
						for (int k = 0; k < columnNumber; k++) { 
							if (k == j) { 
								
								for (int l = 0; l < 8; l++) {   
									columnPoint[l] = new CoordinateOfPoint(); 
									
									int num = j * 8 + l; 
									columnPoint[l].setX(point[num].getX()); 
									columnPoint[l].setY(point[num].getY());
									columnPoint[l].setZ(point[num].getZ());   
								}  
								column[j++].setPoints(columnPoint);
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
		return column;
	} 
	 
}
