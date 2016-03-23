package test.ifc.jyg;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Cuboid {

	CoordinateOfPoint[] points = new CoordinateOfPoint[8];	//一个长方体八个顶点
	private static Beam[] beams = new Beam[7];
	public CoordinateOfPoint topTopLeft;
	public CoordinateOfPoint topTopRight;
	public CoordinateOfPoint topdownLeft;
	public CoordinateOfPoint topdownRight;
	public CoordinateOfPoint downTopLeft;
	public CoordinateOfPoint downTopRight;
	public CoordinateOfPoint downDownLeft;
	public CoordinateOfPoint downDownRight;
	private RectangularArea[] rectangularArea = new RectangularArea[4];
	
	private Map<RectangularArea, Integer> map = new HashMap<>();	//String 表示 长 * 宽，Integer 表示 该尺寸的个数
	
	
	public Cuboid() {
		
	}
	
	private void getPointLocation() {
		ParseBeamPoint parseBeamPoint = new ParseBeamPoint();
		beams = parseBeamPoint.getBeamPoint("E:\\IFC\\Beam.obj");
		
		boolean isFirstPoint = true;
		for (Beam beam : beams) {
			points = beam.getPoints();	//获取每个Beam的八个顶点
			//遍历一个长方体中所有的点，找到topDownRight点
			double x = 0.0;
			double y = 0.0;
			double z = 0.0;
			CoordinateOfPoint markPoint = new CoordinateOfPoint();
			for (CoordinateOfPoint point : points) {
				//search max topDownRightPoint
				
				if (isFirstPoint) {
					x = point.getX();
					y = point.getY();
					z = point.getZ();
					markPoint = point; 
					isFirstPoint = false;
					 
					
				} else if (point.getX() >= x && point.getY() >= y && point.getZ() >= z ) {
					x = point.getX();
					y = point.getY();
					z = point.getZ(); 
					markPoint = point;
				}  
			}
			
			for (CoordinateOfPoint point : points) { 
				point.setDescribe(point.compareTo(markPoint)); 
			}  
			isFirstPoint = true;
			markPoint = null; 
		}
	}
	
	private void calculateArea() {
		
		getPointLocation();
		double[] width = new double[8]; 
		double[] length = new double[8]; 
		
		for (Beam beam : beams) {
			
			points = beam.getPoints();
			
			for (CoordinateOfPoint point : points) { 
				String location = point.getLocation();
				switch (location) {
					case "downTopLeft":
						width[0] = point.getY(); 
						length[0] = point.getZ();
						
						width[4] = point.getX();
						length[4] = point.getZ();
						break;				
					case "topTopRight":
						width[1] = point.getY();
						length[1] = point.getZ();
						break;
					case "downDownLeft":
						width[2] = point.getY();
						length[2] = point.getZ();
						break;
					case "topDownRight":
						width[3] = point.getY();
						length[3] = point.getZ(); 
						
						width[7] = point.getX();
						length[7] = point.getZ(); 
						break; 
					case "topDownLeft":
						width[5] = point.getX();
						length[5] = point.getZ();
						break; 
					case "downTopRight":
						width[6] = point.getX();
						length[6] = point.getZ();
						break;
				} 
			}
			
			//计算柱各个面的面积
			for (int i = 0; i < rectangularArea.length; i++) {
				
				double wid = Math.abs(length[2 * i +1] - length[2 * i]);
				double len = Math.abs(width[2 * i + 1] - width[2 * i]);
				rectangularArea[i] = new RectangularArea(wid, len); 
				rectangularArea[i].setArea(wid, len);
				
				boolean isNew = true;
				if(map.size() == 0) {
					map.put(rectangularArea[i], Integer.valueOf(1));
				} else {
					 
					for (RectangularArea rec : map.keySet()) {
						if (rec.equals(rectangularArea[i])) {
							//取出相应的value 
							Integer num = map.get(rec); 
							map.put(rec, Integer.valueOf(num.intValue() + 1)); 
							isNew  = false;
							break;
						} 
					} 
					
					if (isNew) {
						map.put(rectangularArea[i], Integer.valueOf(1));
					}
				} 
			} 
		}
		
		int k = 0;
		for (RectangularArea rec : map.keySet()) { 
			System.out.println(rec.getArea() + " number : " + map.get(rec).intValue());
		}
	}
	
	public static void main(String[] args) {
		new Cuboid().calculateArea();
	}
}
