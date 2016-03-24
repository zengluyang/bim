package com.ifc.jyg;

import java.util.ArrayList;
import java.util.HashMap; 
import java.util.Map; 

public class IntersectRectangle {
	
	private Map<Integer, Map<Double,  ArrayList<Rectangle>>> directionDoubleMap;
	private Map<Integer, ArrayList<Rectangle>> directionMap = new HashMap<Integer, ArrayList<Rectangle>>();
	private Map<Map<Rectangle, Rectangle>, String> intersectMap = new HashMap<>(); 
	public IntersectRectangle() {
		directionDoubleMap = new HashMap<>();
		directionDoubleMap.put(Rectangle.FRONT_BOOTOM, new HashMap());
		directionDoubleMap.put(Rectangle.UP_DOWN, new HashMap());
		directionDoubleMap.put(Rectangle.LEFT_RIGHT, new HashMap());
	}
	
	public void addRectangleTogether(Rectangle rectangle) { 
		//System.out.println("rectangle : " + rectangle.toString());
		ArrayList<Rectangle> rlt = null;
		Map<Double,  ArrayList<Rectangle>> doubleMap = directionDoubleMap.get(rectangle.getDirection());
		double value = rectangle.getIntersectvalue();
		ArrayList<Rectangle> recList = doubleMap.get(value);
		if(recList==null) {
			doubleMap.put(value, new ArrayList<Rectangle>());
		}
		recList = doubleMap.get(value);
		recList.add(rectangle);
	}
	
	public void test() {
		for(Integer dir :directionDoubleMap.keySet()) {
			System.out.println(dir);
			Map<Double,  ArrayList<Rectangle>> doubleMap = directionDoubleMap.get(dir);
			if(doubleMap!=null) {
				for(Double d:doubleMap.keySet()) {
					System.out.println(d);
					ArrayList<Rectangle> recList = doubleMap.get(d);
					if(recList!=null) {
						System.out.println(recList);
					}
				}
			}
		}
	}
	
	
	public Map<Map<Rectangle, Rectangle>, String> getIntersectMap() {
		for(Integer dir :directionDoubleMap.keySet()) {
			//System.out.println(dir);
			Map<Double,  ArrayList<Rectangle>> doubleMap = directionDoubleMap.get(dir);
			if(doubleMap!=null) {
				for(Double d:doubleMap.keySet()) {
					System.out.println(d);
					ArrayList<Rectangle> recList = doubleMap.get(d);
					if(recList!=null) {
						//System.out.println(recList);
						for (int i = 0; i < recList.size(); i++) {
							Rectangle first = recList.get(i); 
							if (first != null) {
								for (int j = recList.size() - 1; j > i; j--) {
									Rectangle second = recList.get(j); 
									if(second!=null) {
										if(first.isAtTopContainedByOrContains(second)) {
											Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
											rectangleMap.put(first, second);
											intersectMap.put(rectangleMap, "CONTAINS" ); 
											recList.set(i, null);
											recList.set(j, null);
											break;
										}
									}
								}
							}
						}
						
						for (Rectangle rectangle : recList) {
							if (rectangle != null) { 
								Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
								rectangleMap.put(rectangle, null);
								intersectMap.put(rectangleMap, "ALONE");
							}
						}
					}
				}
			}
		}
		return intersectMap;
	}
	
	public Map<Map<Rectangle, Rectangle>, String> getIntersectMap2() {
		
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
							 
						}
					}
				}
			 
				
				for (Rectangle rectangle : list) {
					if (rectangle != null) {
		 
						Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
						rectangleMap.put(rectangle, null);
						intersectMap.put(rectangleMap, "FRONT_BOOTOM " + "value :" + rectangle.getIntersectvalue());
					}
				}  
				break; 
			}
			case Rectangle.LEFT_RIGHT:
			{
				ArrayList<Rectangle> list = directionMap.get(Rectangle.LEFT_RIGHT); 
				for (int i = 0; i < list.size(); i++) {
					Rectangle key = list.get(i);
					if (key != null) {
						double value = key.getIntersectvalue();
						for (int j = list.size() - 1; j > i; j--) {
							if (list.get(j) != null && value == list.get(j).getIntersectvalue()) {
								if (key.getArea() > list.get(j).getArea()) { 
									
									if ( key.topLeft.getX() <= list.get(j).topLeft.getX() && 
											key.downRight.getZ() <= list.get(j).topLeft.getZ() ) {
										Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
										rectangleMap.put(key, list.get(j));
										intersectMap.put(rectangleMap, "LEFT_RIGHT " + "value :" + value); 
										list.set(i, null);
										list.set(j, null);
										break;
									}
								} else if(key.getArea() <= list.get(j).getArea()) {
									if ( key.topLeft.getX() >= list.get(j).topLeft.getX() && 
											key.downRight.getZ() <= list.get(j).topLeft.getZ()) {
										Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
										rectangleMap.put(list.get(j), key);
										intersectMap.put(rectangleMap, "LEFT_RIGHT " + "value :" + value); 
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
						intersectMap.put(rectangleMap, "LEFT_RIGHT " + "value :" + rectangle.getIntersectvalue());
					}
				}
				  
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
								if (key.getArea() > list.get(j).getArea()) { 
									
									if ( key.topLeft.getX() == list.get(j).topLeft.getX() && 
											key.downRight.getY() >= list.get(j).topLeft.getY() ) {
										Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
										rectangleMap.put(key, list.get(j));
										intersectMap.put(rectangleMap, "UP_DOWN " + "value :" + value); 
										list.set(i, null);
										list.set(j, null);
										break;
									}
								} else if(key.getArea() <= list.get(j).getArea()) {
									if ( key.topLeft.getX() == list.get(j).topLeft.getX() && 
											key.downRight.getY() <= list.get(j).topLeft.getY()) {
										Map<Rectangle, Rectangle> rectangleMap = new HashMap<>();
										rectangleMap.put(list.get(j), key);
										intersectMap.put(rectangleMap, "UP_DOWN " + "value :" + value); 
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
						intersectMap.put(rectangleMap, "UP_DOWN " + "value :" + rectangle.getIntersectvalue());
					}
				}  
				break; 
			}
			default:
				break;
			}
		}
		return intersectMap;
	}
	 
}
