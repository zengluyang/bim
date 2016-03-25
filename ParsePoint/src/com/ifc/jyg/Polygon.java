package com.ifc.jyg;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZLY on 2016/3/24.
 */
public class Polygon implements Comparable<Object>{


    public final static int  FRONT_BOOTOM = 0;
    public final static int  LEFT_RIGHT = 1;
    public final static int  UP_DOWN = 2;
    public final static String[] directionString = {"FRONT_BOOTOM","LEFT_RIGHT","UP_DOWN"};

    private ArrayList<CoordinateOfPoint> pointList = new ArrayList<CoordinateOfPoint>();
    private ArrayList<Edge> edgeList = new ArrayList<Edge>();
    private int direction = 0;

    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        } else if (o != null && o instanceof Polygon) {
            Polygon poly = (Polygon) o;
            if(this.pointList.size()<poly.pointList.size()) {
                return -1;
            } else if(this.pointList.size()==poly.pointList.size()) {
                for(int i=0;i<this.pointList.size();i++) {
                    if(this.pointList.get(i).compareTo(poly.pointList.get(i))<0) {
                        return -1;
                    } else if(this.pointList.get(i).compareTo(poly.pointList.get(i))==0) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
        }

        return 1;
    }

    public Polygon(ArrayList<Edge> edges) {
        this.direction = edges.get(0).getDirection();
        this.setEdgeListAndPointListFromUnorderedEdgeList(edges);

    }

    private void setEdgeListAndPointListFromUnorderedEdgeList(ArrayList<Edge> edges) {
        if(edges.size()==0) {
            System.out.println("setEdgeListAndPointListFromUnorderedEdgeList error!");
            return;
        }
        Edge first = edges.get(0);
        edges.remove(0);
        Edge e = null;
        edgeList.add(first);
        while(true) {
            e = findNextConnectedEdge(edges,first);
            if(e==null) {
                break;
            }
            first=e;
        }
        //System.out.println(edgeList);
        for(int i=0;i<edgeList.size()-1;i++) {
            Edge edge =edgeList.get(i);
            if(i==0) {
                pointList.add(edge.getFirst());
                pointList.add(edge.getSecond());
            } else {
                CoordinateOfPoint pLast = pointList.get((i+1)-1);
                CoordinateOfPoint pLastLast = pointList.get((i+1)-2);
//                System.out.println("i "+i+" pointList "+pointList);
//                System.out.println("pLastLast "+pLastLast);
//                System.out.println("pLast "+pLast);
//                System.out.println("edge.getSecond() "+edge.getSecond());
//                boolean test =edge.getSecond()==pLastLast;
//                System.out.println("edge.getSecond()==pLastLast "+ test);
                CoordinateOfPoint pToAdd = null;
                if(pLast==edge.getFirst()) {
                    pToAdd = edge.getSecond();
                } else if(pLast==edge.getSecond()) {
                    pToAdd = edge.getFirst();
                } else if(pLastLast==edge.getFirst()) {
                    pToAdd = edge.getSecond();
                } else if(pLastLast==edge.getSecond()){
                    pToAdd = edge.getFirst();
                } else {
                    System.out.println("Polygon(ArrayList<Edge> edges) error!");
                }
                pointList.add(pToAdd);
            }
        }
    }

    public Polygon(Rectangle a, Rectangle b) {
    	ArrayList<Edge> edges = Polygon.getEdgeListFromTwoRectangles(a, b);
    	this.setEdgeListAndPointListFromUnorderedEdgeList(edges);    	
    }
    
    private static ArrayList<Edge> getEdgeListFromTwoRectangles(Rectangle a, Rectangle b){ 
    	if(a.isAtTopContainsSmaller(b)) {
    		return getEdgeListFromBiggerAndSmallerRectangle(a,b);
    	} else {
    		return getEdgeListFromBiggerAndSmallerRectangle(b,a);
    	}
    }
    
    private static ArrayList<Edge> getEdgeListFromBiggerAndSmallerRectangle(Rectangle a, Rectangle b) {
        ArrayList<Edge> rlt = new ArrayList<Edge>();
        if (a.getDirection() !=  b.getDirection()) {
			return rlt;
		}
        
        if (a.getDirection() == Rectangle.UP_DOWN) {
			return rlt;
		} 
 
        if (a.isAtTopContainedByOrContains(b)) {	//
        	ArrayList<Edge> alistEdges = a.getEdges();
			ArrayList<Edge> blistEdges = b.getEdges(); 
			
			Edge a1 = alistEdges.get(0);
			Edge a2 = alistEdges.get(1);
			Edge a3 = alistEdges.get(2);
			Edge a4 = alistEdges.get(3);
			
			Edge b1 = blistEdges.get(0);
			Edge b2 = blistEdges.get(1);
			Edge b3 = blistEdges.get(2);
			Edge b4 = blistEdges.get(3);
			
			ArrayList<Edge> res1 = a1.getNewEgdesFromThisAndThatEdges(b1);
			if (res1.size() == 0) {
				rlt.add(a1);
				rlt.add(b1);
			} else {
				rlt.addAll(res1);
			}
			
			ArrayList<Edge> res2= a2.getNewEgdesFromThisAndThatEdges(b2);
			if (res2.size() == 0) {
				rlt.add(a2);
				rlt.add(b2);
			} else {
				rlt.addAll(res2);
			}
			
			ArrayList<Edge> res3 = a3.getNewEgdesFromThisAndThatEdges(b3);
			if (res3.size() == 0) {
				rlt.add(a3);
				rlt.add(b3);
			} else {
				rlt.addAll(res3);
			}
			
			ArrayList<Edge> res4 = a4.getNewEgdesFromThisAndThatEdges(b4);
			if (res4.size() == 0) {
				rlt.add(a4);
				rlt.add(b4);
			} else {
				rlt.addAll(res4);
			}
			if(rlt.size()==0 ) {
				System.out.println("getEdgeListFromBiggerAndSmallerRectangle error!");
			}
         
        } 
        return rlt;
    }

    private Edge findNextConnectedEdge(ArrayList<Edge> edges,Edge edge) {
        for(int i=0;i<edges.size();i++) {
            Edge e = edges.get(i);
            //System.out.println("cmp "+e+edge);
            if(e.isConnectedTo(edge)) {
                edges.remove(i);
                edgeList.add(e);
                return e;
            }
        }
        return null;
    }

    public ArrayList<CoordinateOfPoint> getPointList() {
        return pointList;
    }

    public ArrayList<Edge> getEdgeList() {
        return edgeList;
    }

    public int getDirection() {
        return direction;
    }

    public static void testArrayList() {
        ArrayList<Integer> il = new ArrayList<Integer>();
        il.add(100);
        il.add(200);
        il.add(300);
        for (Integer i : il) {
            System.out.println(i);
            //il.remove(i);
//            ArrayList<Integer> iil = (ArrayList<Integer>)il.clone();
//            for (Integer ii : iil) {
//                if (ii.equals(new Integer(300))) {
//                    il.remove(ii);
//                }
//            }
        }
    }

    public static void testPolygon() {
        CoordinateOfPoint p1 = new CoordinateOfPoint(0, 0, 0);
        CoordinateOfPoint p2 = new CoordinateOfPoint(5, 0, 0);
        CoordinateOfPoint p3 = new CoordinateOfPoint(5, 5, 0);
        CoordinateOfPoint p4 = new CoordinateOfPoint(0, 5, 0);
        Edge e12 = new Edge(p1,p2);
        Edge e34 = new Edge(p3,p4);
        Edge e14 = new Edge(p1,p4);
        Edge e23 = new Edge(p2,p3);
        ArrayList<Edge> es = new ArrayList<Edge>();
        es.add(e12);
        es.add(e34);
        es.add(e14);
        es.add(e23);
        Polygon p = new Polygon(es);
        System.out.print(p.getEdgeList()+"\n");
        System.out.print(p.getPointList());
    }
}
