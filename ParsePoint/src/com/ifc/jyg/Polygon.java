package com.ifc.jyg;

import java.util.ArrayList;

/**
 * Created by ZLY on 2016/3/24.
 */
public class Polygon implements Comparable<Object>{

    private ArrayList<CoordinateOfPoint> pointList = new ArrayList<CoordinateOfPoint>();
    private ArrayList<Edge> edgeList = new ArrayList<Edge>();

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public Polygon(ArrayList<Edge> edges) {
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
        for(int i=0;i<edgeList.size();i++) {
            Edge edge =edgeList.get(i);
            int lastStepChoice = 0; //0 indicates picks first, 1 indicates picks second.
            if(i==0) {
                pointList.add(edge.getFirst());
                pointList.add(edge.getSecond());
            } else {
                CoordinateOfPoint pLast = pointList.get((i+1)-1);
                CoordinateOfPoint pLastLast = pointList.get((i+1)-2);
                if(pLast==edge.getFirst()) {
                    pointList.add(edge.getSecond());
                    lastStepChoice = 1;
                } else if(pLast==edge.getSecond()) {
                    pointList.add(edge.getFirst());
                    lastStepChoice = 1;
                } else if(pLastLast==edge.getFirst()) {
                    pointList.add(edge.getSecond());
                    lastStepChoice = 0;
                } else if(pLastLast==edge.getFirst()){
                    pointList.add(edge.getFirst());
                    lastStepChoice = 0;
                } else {
                    System.out.println("Polygon(ArrayList<Edge> edges) error!");
                }
            }
        }

    }

    public Polygon(Rectangle a, Rectangle b) {

    }

    private static getEdgeListFromTwoRectangle(Rectangle a, Rectangle b) {
        ArrayList<Rectangle> rlt = new ArrayList<Rectangle>();
        //TODO
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
