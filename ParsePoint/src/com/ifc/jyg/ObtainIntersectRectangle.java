package com.ifc.jyg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class ObtainIntersectRectangle {
	
	
	private Map<Integer, Map<Double, Rectangle>> intersectRectangle = new HashMap<Integer, Map<Double,Rectangle>>();
	//Integer : direct, Double : value, Rectangle example : FRONT_BOOTOM, y = 2.0, rectangle
	
	private Map<Integer, TreeSet<Rectangle>> intersectMap = new HashMap<>();
	private TreeSet<Rectangle> rlt;
	public ObtainIntersectRectangle() {
		
	}
	
//	public void addRectangleTogether(Rectangle rectangle) {
//		double value = 0.0;
//		switch (rectangle.getDirection()) {
//		case Rectangle.FRONT_BOOTOM:
//		{
//			rlt = new TreeSet<>();
//			rlt.add(rectangle);  
//			intersectMap.put(Rectangle.FRONT_BOOTOM, rlt);
//			break;
//		} 
//		case Rectangle.LEFT_RIGHT:
//		{
//			rlt = new TreeSet<>();
//			rlt.add(rectangle);  
//			intersectMap.put(Rectangle.LEFT_RIGHT, rlt);
//			break;
//		}
//		case Rectangle.UP_DOWN:
//		{
//			rlt = new TreeSet<>();
//			rlt.add(rectangle);  
//			intersectMap.put(Rectangle.UP_DOWN, rlt);
//			break;
//		}
//		default:
//			break;
//		}
//	}
//	
	public void addRectangleTogether(Rectangle rectangle) {
		double value = 0.0;
		switch (rectangle.getDirection()) {
		case Rectangle.FRONT_BOOTOM:
		{
			value = rectangle.getIntersectvalue();
			Map<Double, Rectangle> rectMap = intersectRectangle.get(Rectangle.FRONT_BOOTOM);
			if (rectMap == null) {
				rectMap = new HashMap<>();
			}			
			rectMap.put(value, rectangle);
			intersectRectangle.put(Rectangle.FRONT_BOOTOM, rectMap);
			break;
		} 
		case Rectangle.LEFT_RIGHT:
		{
			value = rectangle.getIntersectvalue();
			Map<Double, Rectangle> rectMap = intersectRectangle.get(Rectangle.LEFT_RIGHT);
			if (rectMap == null) {
				rectMap = new HashMap<>();
			}			
			rectMap.put(value, rectangle);
			intersectRectangle.put(Rectangle.LEFT_RIGHT, rectMap);
			break;
		}
		case Rectangle.UP_DOWN:
		{
			value = rectangle.getIntersectvalue();
			Map<Double, Rectangle> rectMap = intersectRectangle.get(Rectangle.UP_DOWN);
			if (rectMap == null) {
				rectMap = new HashMap<>();
			}			
			rectMap.put(value, rectangle);
			intersectRectangle.put(Rectangle.UP_DOWN, rectMap);
			break;
		}
		default:
			break;
		}
	}
	
	public void test() {
		//System.out.println();
		for (int it : intersectRectangle.keySet()) {
			switch (it) {
			case Rectangle.FRONT_BOOTOM:
			{ 
				Map<Double, Rectangle> rectMap = intersectRectangle.get(Rectangle.FRONT_BOOTOM);
				System.out.println("Rectangle.FRONT_BOOTOM size:" + rectMap.size());
				for (double value : rectMap.keySet()) {
					System.out.println("direction:" +Rectangle.FRONT_BOOTOM +  " value:" + value);
					//System.out.println(rectMap.get(value));
				}
				break; 
			}
			case Rectangle.LEFT_RIGHT:
			{
				 
				Map<Double, Rectangle> rectMap = intersectRectangle.get(Rectangle.LEFT_RIGHT);
				System.out.println("Rectangle.LEFT_RIGHT size:" + rectMap.size());
				for (double value : rectMap.keySet()) {
					System.out.println("direction:" +Rectangle.LEFT_RIGHT + " value:" + value);
					//System.out.println(rectMap.get(value));
				} 
				break;
			}
			case Rectangle.UP_DOWN:
			{
				Map<Double, Rectangle> rectMap = intersectRectangle.get(Rectangle.UP_DOWN);
				System.out.println("Rectangle.UP_DOWN size:" + rectMap.size());
				for (double value : rectMap.keySet()) {
					System.out.println("direction:" +Rectangle.UP_DOWN +" value:" + value);
					//System.out.println(rectMap.get(value));
				} 
				break;
			}
			default:
				break;
			}
		}
	}
}
