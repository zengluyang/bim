package com.ifc.jyg;

import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map; 

public class IntersectRectangle {
	
	private Map<Integer, ArrayList<Rectangle>> directionMap = new HashMap<Integer, ArrayList<Rectangle>>();
	private Map<Map<Rectangle, Rectangle>, String> intersectMap = new HashMap<>();
	//private TreeSet<Rectangle> rlt = new TreeSet<Rectangle>();
	public IntersectRectangle() {
		
	}
	
	public void addRectangleTogether(Rectangle rectangle) { 
		//System.out.println("rectangle : " + rectangle.toString());
		ArrayList<Rectangle> rlt = null;
		switch (rectangle.getDirection()) {
		case Rectangle.FRONT_BOOTOM:
		{ 
			rlt = directionMap.get(Rectangle.FRONT_BOOTOM);
			if (rlt == null) {
				rlt = new ArrayList<Rectangle>(); 
			}
			rlt.add(rectangle);
			directionMap.put(Rectangle.FRONT_BOOTOM, rlt); 
			break;
		} 
		case Rectangle.LEFT_RIGHT:
		{ 
			rlt = directionMap.get(Rectangle.LEFT_RIGHT);
			if (rlt == null) {
				rlt = new ArrayList<Rectangle>(); 
			}
			rlt.add(rectangle);
			directionMap.put(Rectangle.LEFT_RIGHT, rlt);
			break;
		}
		case Rectangle.UP_DOWN:
		{ 
			rlt = directionMap.get(Rectangle.UP_DOWN);
			if (rlt == null) {
				rlt = new ArrayList<Rectangle>(); 
			}
			rlt.add(rectangle);
			directionMap.put(Rectangle.UP_DOWN, rlt);
			break;
		}
		default:
			break;
		}
	}
	
	
	public Map<Map<Rectangle, Rectangle>, String> getIntersectMap() {
		
		for (int it : directionMap.keySet()) {
			switch (it) {
			case Rectangle.FRONT_BOOTOM:
			{
				ArrayList<Rectangle> list = directionMap.get(Rectangle.FRONT_BOOTOM);
				//System.out.println("FRONT_BOOTOM size:" + list.size()); 
				for (int i = 0; i < list.size(); i++) {
					Rectangle key = list.get(i);
					if (key != null) {
						double value = key.getIntersectvalue();
						for (int j = list.size() - 1; j > i; j--) {
							if (list.get(j) != null && value == list.get(j).getIntersectvalue()) {
								
								if (key.getArea() > list.get(j).getArea()) {
									if ( key.topLeft.getY() <= list.get(j).topLeft.getY() && 
											key.downRight.getZ() <= list.get(j).topLeft.getZ() ) {
										Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
										rectangleMap.put(key, list.get(j));
										intersectMap.put(rectangleMap, "FRONT_BOOTOM " + "value :" + value); 
										list.set(i, null);
										list.set(j, null);
										break;
									}
								} else if(key.getArea() <= list.get(j).getArea()) {
									if ( key.topLeft.getY() <= list.get(j).topLeft.getY() && 
											key.downRight.getZ() <= list.get(j).topLeft.getZ()) {
										Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
										rectangleMap.put(list.get(j), key);
										intersectMap.put(rectangleMap, "FRONT_BOOTOM " + "value :" + value); 
										list.set(i, null);
										list.set(j, null);
										break;
									}
								} 
								
							}
						}
					}
				}
				int k = 0;
				for (Rectangle rectangle : list) {
					if (rectangle != null) {
						k++;
						Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
						rectangleMap.put(rectangle, null);
						intersectMap.put(rectangleMap, "FRONT_BOOTOM " + "value :" + rectangle.getIntersectvalue());
					}
				} 
				//System.out.println(k);
				break; 
			}
			case Rectangle.LEFT_RIGHT:
			{
				ArrayList<Rectangle> list = directionMap.get(Rectangle.LEFT_RIGHT);
				//System.out.println("LEFT_RIGHT size:" + list.size()); 
				for (int i = 0; i < list.size(); i++) {
					Rectangle key = list.get(i);
					if (key != null) {
						double value = key.getIntersectvalue();
						for (int j = list.size() - 1; j > i; j--) {
							if (list.get(j) != null && value == list.get(j).getIntersectvalue()) {
								Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
								rectangleMap.put(key, list.get(j));
								intersectMap.put(rectangleMap, "LEFT_RIGHT " + "value :" + value); 
								list.set(i, null);
								list.set(j, null);
								break;
							}
						}
					}
				}
				 
				int k = 0;
				for (Rectangle rectangle : list) {
					if (rectangle != null) {
						k++;
						Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
						rectangleMap.put(rectangle, null);
						intersectMap.put(rectangleMap, "LEFT_RIGHT " + "value :" + rectangle.getIntersectvalue());
					}
				}
				 
				//System.out.println(k);
				break; 
			}
			case Rectangle.UP_DOWN:
			{
 			
				ArrayList<Rectangle> list = directionMap.get(Rectangle.UP_DOWN);
				//System.out.println("UP_DOWN size:" + list.size()); 
				for (int i = 0; i < list.size(); i++) {
					Rectangle key = list.get(i);
					if (key != null) {
						double value = key.getIntersectvalue();
						for (int j = list.size() - 1; j > i; j--) {
							if (list.get(j) != null && value == list.get(j).getIntersectvalue()) {
								Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
								rectangleMap.put(key, list.get(j));
								intersectMap.put(rectangleMap, "UP_DOWN " + "value :" + value); 
								list.set(i, null);
								list.set(j, null);
								break;
							}
						}
					}
				}
				int k = 0;
				for (Rectangle rectangle : list) {
					if (rectangle != null) {
						k++;
						Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
						rectangleMap.put(rectangle, null);
						intersectMap.put(rectangleMap, "UP_DOWN " + "value :" + rectangle.getIntersectvalue());
					}
				} 
				//System.out.println(k);
				break; 
			}
			default:
				break;
			}
		}
		return intersectMap;
	}
	/*
	public void test() {
		//System.out.println("directionMap size :" + directionMap.size());
		for (int it : directionMap.keySet()) {
			switch (it) {
			case Rectangle.FRONT_BOOTOM:
			{
				TreeSet<Rectangle> rlt = directionMap.get(Rectangle.FRONT_BOOTOM);
				//System.out.println("Rectangle.FRONT_BOOTOM :" + rlt.size());
				break; 
			}
			case Rectangle.LEFT_RIGHT:
			{
				TreeSet<Rectangle> rlt = directionMap.get(Rectangle.LEFT_RIGHT);
				//System.out.println("Rectangle.LEFT_RIGHT :" + rlt.size()); 
				break;
			}
			case Rectangle.UP_DOWN:
			{
 			
				TreeSet<Rectangle> rlt = directionMap.get(Rectangle.UP_DOWN);
				//System.out.println("Rectangle.UP_DOWN :" + rlt.size());
				break;
			}
			default:
				break;
			}
		}
	}*/
}
