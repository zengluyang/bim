package com.ifc.jyg;

import java.util.*;

public class IntersectRectangle {
	
	private Map<Integer, Map<Double,  ArrayList<Rectangle>>> directionDoubleMap;
	private Map<Integer, ArrayList<Rectangle>> directionMap = new HashMap<Integer, ArrayList<Rectangle>>();
	private Map<Map<Rectangle, Rectangle>, String> intersectMap = new HashMap<>();
	private ArrayList<TreeSet<Rectangle>> recSetList = new ArrayList<>();
	private ArrayList<Polygon> resultPolyList = new ArrayList<Polygon>();

	public IntersectRectangle() {
		directionDoubleMap = new TreeMap<>();
		directionDoubleMap.put(Rectangle.FRONT_BOOTOM, new TreeMap());
		directionDoubleMap.put(Rectangle.UP_DOWN, new TreeMap());
		directionDoubleMap.put(Rectangle.LEFT_RIGHT, new TreeMap());
	}
	
	public void addRectangleTogether(Rectangle rectangle) { 
		Map<Double,  ArrayList<Rectangle>> doubleMap = directionDoubleMap.get(rectangle.getDirection());
		if(doubleMap==null) {
			System.out.println("IntersectRectangle.addRectangleTogether error!");
			return;
		}
		double value = rectangle.getIntersectvalue();
		ArrayList<Rectangle> recList = doubleMap.get(value);
		if(recList==null) {
			doubleMap.put(value, new ArrayList<Rectangle>());
		}
		if(rectangle==null) {
			System.out.println("IntersectRectangle.addRectangleTogether error!");
			return;
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
	
	public ArrayList<ArrayList<TreeSet<Rectangle>>> getPartitionResult() {
		//System.out.println("directionDoubleMap "+directionDoubleMap);
		ArrayList<ArrayList<TreeSet<Rectangle>>> rlt = new ArrayList<ArrayList<TreeSet<Rectangle>>>();
		for(Integer direction:directionDoubleMap.keySet()) {
			Map<Double, ArrayList<Rectangle>> intersetValueMap = directionDoubleMap.get(direction);
			for(Double intersectValue:intersetValueMap.keySet()) {
				ArrayList<Rectangle> recList = intersetValueMap.get(intersectValue);
				ArrayList<TreeSet<Rectangle>> localRecSetList = new ArrayList<>();
				for(Rectangle r:recList) {
					boolean isInserted = false;

					for(TreeSet<Rectangle>recSet :localRecSetList) {
						Rectangle biggestContainationRec = recSet.first();
						if(biggestContainationRec!=null) {
							if(r==null) {
								break;
							}
							if(
									biggestContainationRec.compareByConataination(r)==1 ||
											//biggestContainationRec.compareByConataination(r)==0 ||
											biggestContainationRec.compareByConataination(r)==-1
									) {
								recSet.add(r);
								isInserted = true;
							}
						}
					}

					if(!isInserted) {
						TreeSet<Rectangle> recSet = new TreeSet<>(new Comparator<Rectangle>() {
							@Override
							public int compare(Rectangle r1, Rectangle r2) {
								return r2.compareByConataination(r1);
							}
						});
						recSet.add(r);
						localRecSetList.add(recSet);
					}
				}
				rlt.add(localRecSetList);
			}
		}
		return rlt;
	}

	public ArrayList<Polygon> getIntersectResult() {
		return resultPolyList;
	}

	public Map<Map<Rectangle, Rectangle>, String> getIntersectMap() {
		return intersectMap;
	}
	
	public Map<Map<Rectangle, Rectangle>, String> getIntersectMap2() {
		return intersectMap;
	}
	 
}
