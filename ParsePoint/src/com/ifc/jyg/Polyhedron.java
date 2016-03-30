package com.ifc.jyg;

import java.util.ArrayList;

import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by ZLY on 16/3/25.
 */
public class Polyhedron implements Comparable<Object>{
    private Polygon downPolygon;
    public  double height;
    public String Id;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Polyhedron \n downPolygon: ").append(this.downPolygon);
        sb.append("\n height:").append(this.height).append("\n");
        return sb.toString();
    }

    public Polyhedron(Polygon downPolygon, double height,String id) {
        if(downPolygon.getDirection()!=Polygon.UP_DOWN) {
            System.out.println("Polyhedron(Polygon downPolygon, double height) error!");
        }
        this.downPolygon = downPolygon;
        this.height = height;
        this.Id=id;
    }

    public Polygon getDownPolygon() {
        return downPolygon;
    }

//    public static void testgetNeededRectangles() {
//    	ArrayList<Edge> rlt = new ArrayList<Edge>();
//    	Edge edge1 = new Edge(new CoordinateOfPoint(0, 0, 0), new CoordinateOfPoint(1, 0, 0),"X_AXIS"); 
//    	Edge edge2 = new Edge(new CoordinateOfPoint(1, 0, 0), new CoordinateOfPoint(1, 1, 0),"Y_AXIS");  
//    	Edge edge3 = new Edge(new CoordinateOfPoint(0, 1, 0), new CoordinateOfPoint(1, 1, 0),"X_AXIS");
//    	Edge edge4 = new Edge(new CoordinateOfPoint(0, 0, 0), new CoordinateOfPoint(0, 1, 0),"Y_AXIS");
//    	rlt.add(edge1);
//    	rlt.add(edge2);
//    	rlt.add(edge3);
//    	rlt.add(edge4);
//    }
    
    public ArrayList<Rectangle> getNeededRectangles() {
        ArrayList<Rectangle> rlt = new ArrayList<Rectangle>();
        for(Edge e : this.downPolygon.getEdgeList()) {
            CoordinateOfPoint downRight = null;
            CoordinateOfPoint topLeft = null;
            switch (e.getDirection()) {
                case Edge.X_AXIS:
                    downRight = e.getFirst();
                    topLeft = new CoordinateOfPoint(e.getSecond().getX(),e.getSecond().getY(),e.getSecond().getZ()+height);
                    rlt.add(new Rectangle(topLeft,downRight,this.Id));
                    break;
                case Edge.Y_AXIS:
                    downRight = e.getSecond();
                    topLeft = new CoordinateOfPoint(e.getFirst().getX(),e.getFirst().getY(),e.getFirst().getZ()+height);
                    rlt.add(new Rectangle(topLeft,downRight,this.Id));
                    break;
                case Edge.Z_AXIS:
                    System.out.println("Polyhedron getNeededRectangles error!");
                    break;
                case Edge.OTHER:
                    System.out.println("Polyhedron getNeededRectangles error!");
                    break;
            }

        }
        return rlt;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) {
            return 0;
        } else if (o != null && o instanceof Polyhedron) {
            Polyhedron poly = (Polyhedron) o;
            if(this.downPolygon.compareTo(poly.downPolygon)<0) {
                return -1;
            } else if(this.downPolygon.compareTo(poly.downPolygon)==0) {
                if(this.height<poly.height) {
                    return -1;
                } else if(this.height==poly.height) {
                    return 0;
                }
            }
        }

        return 1;
    }


}
