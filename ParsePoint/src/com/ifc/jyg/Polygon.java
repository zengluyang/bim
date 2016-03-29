package com.ifc.jyg;

import com.seisw.util.geom.PolyDefault;

import java.util.ArrayList;

/**
 * Created by ZLY on 2016/3/24.
 */
public class Polygon implements Comparable<Object>{


    public final static int  FRONT_BOOTOM = 0;
    public final static int  LEFT_RIGHT = 1;
    public final static int  UP_DOWN = 2;
    public final static String[] directionString = {"FRONT_BOOTOM","LEFT_RIGHT","UP_DOWN"};

    public String Id;
    protected ArrayList<CoordinateOfPoint> pointList;
    protected ArrayList<Edge> edgeList;
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

    protected Polygon() {

    }

    public Polygon(ArrayList<Edge> edges,String Id) {
        this(edges);
        this.Id = Id;
    }

    public Polygon(ArrayList<Edge> edges) {
        this.setEdgeListAndPointListFromUnorderedEdgeList(edges);
        boolean isUpDown = true;
        boolean isLeftRight = true;
        boolean isFrontBottom = true;

        for(Edge e:this.edgeList) {
            if(e.getDirection()==Edge.Z_AXIS) {
                isUpDown = false;
            } else if(e.getDirection()==Edge.Y_AXIS) {
                isLeftRight = false;
            } else if(e.getDirection()==Edge.X_AXIS) {
                isFrontBottom = false;
            }
        }

        if(isUpDown) {
            this.direction=UP_DOWN;
        } else if(isLeftRight) {
            this.direction=LEFT_RIGHT;
        } else if(isFrontBottom) {
            this.direction=FRONT_BOOTOM;
        } else {
            System.out.println(" Polygon(ArrayList<Edge> edges) direction error!");
        }

        this.Id = this.edgeList.get(0).Id;
    }

    private void setEdgeListAndPointListFromUnorderedEdgeList(ArrayList<Edge> edges) {
        pointList = new ArrayList<CoordinateOfPoint>();
        edgeList = new ArrayList<Edge>();
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
            Edge nextEdge = edgeList.get(i+1);
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
                if(pLast.compareTo(edge.getFirst())==0) {
                    pToAdd = edge.getSecond();
                } else if(pLast.compareTo(edge.getSecond())==0) {
                    pToAdd = edge.getFirst();
                } else if(pLastLast.compareTo(edge.getFirst())==0) {
                    pointList.set((i+1)-1,pLastLast);
                    pointList.set((i+1)-2,pLast);
                    pToAdd = edge.getSecond();
                } else if(pLastLast.compareTo(edge.getSecond())==0){
                    pointList.set((i+1)-1,pLastLast);
                    pointList.set((i+1)-2,pLast);
                    pToAdd = edge.getFirst();
                } else {
                    System.out.println("Polygon(ArrayList<Edge> edges) error!");
                }
                pointList.add(pToAdd);
            }
        }
    }

    private static boolean isPointOnEdgeEnd(CoordinateOfPoint point, Edge edge) {
        return point.compareTo(edge.getFirst())==0
                || point.compareTo(edge.getSecond())==0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Polygon :\n Points: \n");
        for(CoordinateOfPoint p:pointList) {
            sb.append("\t").append(p).append("\n");
        }
        sb.append("Polygon :\n Edges: \n");
        for(Edge e: edgeList) {
            sb.append("\t").append(e);
        }
        sb.append("\n");
        return sb.toString();
    }
//
//    static {
//        testtoMatlab2D();
//    }
    public static void testtoMatlab2D() {
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
        System.out.print(p.toMatlab2D()+"\n");
    }

    public String toMatlab2D() {
        StringBuilder sb = new StringBuilder();
        for(Edge e : this.edgeList) {
            sb.append(e.toMatlab2D(this.direction));
        }
        sb.append("\n");
        return sb.toString();
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
    
//    static {
//    	CoordinateOfPoint aTopLeft = new CoordinateOfPoint(10, 0, 10);
//    	CoordinateOfPoint aDownRight = new CoordinateOfPoint(0, 0, 0);
//    	
//    	CoordinateOfPoint bTopLeft = new CoordinateOfPoint(8, 0, 10);
//    	CoordinateOfPoint bDownRight = new CoordinateOfPoint(5, 0, 5);
//    	
//    	CoordinateOfPoint cTopLeft = new CoordinateOfPoint(10, 0, 10);
//    	CoordinateOfPoint cDownRight = new CoordinateOfPoint(8, 0, 7);
//    	Rectangle a = new Rectangle(aTopLeft, aDownRight);
//    	Rectangle b = new Rectangle(bTopLeft, bDownRight);
//    	Rectangle c = new Rectangle(cTopLeft, cDownRight);
//    	ArrayList<Edge> re = getEdgeListFromTwoRectangles(a, b, c);
//    	for (int i = 0; i < re.size(); i++) {
//    		System.out.print("test getEdgeListFromTwoRectangles" + re.get(i));
//		}
//    	System.out.println();
//    	CoordinateOfPoint a1TopLeft = new CoordinateOfPoint(0, 0, 10);
//    	CoordinateOfPoint a1DownRight = new CoordinateOfPoint(0, 10, 0);
//    	
//    	CoordinateOfPoint b1TopLeft = new CoordinateOfPoint(0, 5, 10);
//    	CoordinateOfPoint b1DownRight = new CoordinateOfPoint(0, 8, 5);
//    	
//    	CoordinateOfPoint c1TopLeft = new CoordinateOfPoint(0, 8, 10);
//    	CoordinateOfPoint c1DownRight = new CoordinateOfPoint(0, 10, 6);
//    	
//    	Rectangle a1 = new Rectangle(a1TopLeft, a1DownRight);
//    	Rectangle b1 = new Rectangle(b1TopLeft, b1DownRight);
//    	Rectangle c1 = new Rectangle(c1TopLeft, c1DownRight);
//    	ArrayList<Edge> re1 = getEdgeListFromTwoRectangles(a1, b1, c1);
//    	for (int i = 0; i < re1.size(); i++) {
//    		System.out.print("test1 getEdgeListFromTwoRectangles" + re1.get(i));
//		}
//    	System.out.println();
//    	
//    	CoordinateOfPoint a2TopLeft = new CoordinateOfPoint(0, 0, 10);
//    	CoordinateOfPoint a2DownRight = new CoordinateOfPoint(0, 10, 0);
//    	
//    	CoordinateOfPoint b2TopLeft = new CoordinateOfPoint(0, 5, 10);
//    	CoordinateOfPoint b2DownRight = new CoordinateOfPoint(0, 8, 5);
//    	
//    	CoordinateOfPoint d2TopLeft = new CoordinateOfPoint(0, 8, 10);
//    	CoordinateOfPoint d2DownRight = new CoordinateOfPoint(0, 10, 6);
//    	
//    	CoordinateOfPoint c2TopLeft = new CoordinateOfPoint(0, 0, 10);
//    	CoordinateOfPoint c2DownRight = new CoordinateOfPoint(0, 5, 6);
//    	
//    	Rectangle a2 = new Rectangle(a2TopLeft, a2DownRight);
//    	Rectangle b2 = new Rectangle(b2TopLeft, b2DownRight);
//    	Rectangle c2 = new Rectangle(c2TopLeft, c2DownRight);
//    	Rectangle d2 = new Rectangle(d2TopLeft, d2DownRight);
//    	ArrayList<Edge> re2 = getEdgeListFromTwoRectangles(a2, b2, c2, d2);
//    	for (int i = 0; i < re2.size(); i++) {
//    		System.out.print("test2 getEdgeListFromTwoRectangles" + re2.get(i));
//		}
//    	System.out.println();
//    	
//    	CoordinateOfPoint a3TopLeft = new CoordinateOfPoint(10, 0, 10);
//    	CoordinateOfPoint a3DownRight = new CoordinateOfPoint(0, 0, 0);
//    	
//    	CoordinateOfPoint b3TopLeft = new CoordinateOfPoint(8, 0, 10);
//    	CoordinateOfPoint b3DownRight = new CoordinateOfPoint(5, 0, 5);
//    	
//    	CoordinateOfPoint c3TopLeft = new CoordinateOfPoint(10, 0, 10);
//    	CoordinateOfPoint c3DownRight = new CoordinateOfPoint(8, 0, 7);
//    	
//    	CoordinateOfPoint d3TopLeft = new CoordinateOfPoint(5, 0, 7);
//    	CoordinateOfPoint d3DownRight = new CoordinateOfPoint(0, 0, 7);
//    	Rectangle a3 = new Rectangle(a3TopLeft, a3DownRight);
//    	Rectangle b3 = new Rectangle(b3TopLeft, b3DownRight);
//    	Rectangle c3 = new Rectangle(c3TopLeft, c3DownRight);
//    	Rectangle d3 = new Rectangle(d3TopLeft, d3DownRight);
//    	ArrayList<Edge> re3 = getEdgeListFromTwoRectangles(a3, b3, c3, d3);
//    	for (int i = 0; i < re3.size(); i++) {
//    		System.out.print("test3 getEdgeListFromTwoRectangles" + re3.get(i));
//		}
//    	System.out.println();
//    }
//    
    private static ArrayList<Edge> getEdgeListFromTwoRectangles(Rectangle a, Rectangle b, Rectangle c) {
    	ArrayList<Edge> rlt = new ArrayList<Edge>();
    	
    	CoordinateOfPoint aDownLeft = a.getPoint().get(0);
    	CoordinateOfPoint aTopRight = a.getPoint().get(1);
    	
    	CoordinateOfPoint bDownLeft = b.getPoint().get(0);
    	CoordinateOfPoint bTopRight = b.getPoint().get(1);
    	
    	CoordinateOfPoint cDownLeft = c.getPoint().get(0); 
    	 
    	Edge[] edges = new Edge[8];
    	if (a.topLeft.compareTo(c.topLeft) == 0) {
			
    		edges[0] = new Edge(cDownLeft, c.downRight); 
			edges[1] = new Edge(c.downRight, bDownLeft);
			edges[2] = new Edge(bDownLeft, b.downRight);
			edges[3] = new Edge(b.downRight, bTopRight);
			edges[4] = new Edge(bTopRight, aTopRight);
			edges[5] = new Edge(aTopRight, a.downRight);
			edges[6] = new Edge(a.downRight, aDownLeft);
			edges[7] = new Edge(aDownLeft, cDownLeft);
    		
		} else if (a.topLeft.getZ() == c.topLeft.getZ() &&
				a.downRight.getX() == c.downRight.getX() &&
				a.downRight.getY() == c.downRight.getY()) { //������ FRONT_BOOTOM����LEFT_RIGHT������
			edges[0] = new Edge(a.topLeft, b.topLeft); 
			edges[1] = new Edge(b.topLeft, bDownLeft);
			edges[2] = new Edge(bDownLeft, b.downRight);
			edges[3] = new Edge(b.downRight, cDownLeft);
			edges[4] = new Edge(cDownLeft, c.downRight);
			edges[5] = new Edge(c.downRight, a.downRight);
			edges[6] = new Edge(a.downRight, aDownLeft);
			edges[7] = new Edge(aDownLeft, a.topLeft);
		} 
    	
    	for (int i = 0; i < edges.length; i++) {
    		rlt.add(edges[i]);
		} 
    	return rlt;
    }

    
    private static ArrayList<Edge> getEdgeListFromTwoRectangles(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
    	ArrayList<Edge> rlt = new ArrayList<Edge>();
    	CoordinateOfPoint aDownLeft = a.getPoint().get(0);  
    	CoordinateOfPoint bDownLeft = b.getPoint().get(0);  
    	CoordinateOfPoint cDownLeft = c.getPoint().get(0); 
    	CoordinateOfPoint dDownLeft = d.getPoint().get(0); 
    	
    	Edge edge1 = new Edge(cDownLeft, c.downRight);
    	Edge edge2 = new Edge(c.downRight, bDownLeft);
    	Edge edge3 = new Edge(bDownLeft, b.downRight);
    	Edge edge4 = new Edge(b.downRight, dDownLeft);
    	Edge edge5 = new Edge(dDownLeft, d.downRight);
    	Edge edge6 = new Edge(d.downRight, a.downRight);
    	Edge edge7 = new Edge(a.downRight, aDownLeft);
    	Edge edge8 = new Edge(aDownLeft, cDownLeft);
    	
    	rlt.add(edge1);
    	rlt.add(edge2);
    	rlt.add(edge3);
    	rlt.add(edge4);
    	rlt.add(edge5);
    	rlt.add(edge6);
    	rlt.add(edge7);
    	rlt.add(edge8);
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




    public static PolyDefault convertToGpcjPoly(Polygon p) {
        PolyDefault gpcjPoly = new PolyDefault();
        for(CoordinateOfPoint point:p.getPointList()) {
            gpcjPoly.add(point.getX2d(p.getDirection()),point.getY2d(p.getDirection()));
        }
        return gpcjPoly;
    }

    public static Polygon convertFromGpcjPoly(PolyDefault poly,double intersectValue,int type) {
        Polygon p = new Polygon();
        int n = poly.getNumPoints();
        p.edgeList = new ArrayList<Edge>(n);
        p.pointList = new ArrayList<CoordinateOfPoint>(n);
        for(int i=0;i<n;i++) {
            double x;
            double y;
            double z;
            switch (type) {
                case Polygon.FRONT_BOOTOM:
                    x=intersectValue;
                    y=poly.getX(i);
                    z=poly.getY(i);
                    break;
                case Polygon.LEFT_RIGHT:
                    x=poly.getX(i);
                    y=intersectValue;
                    z=poly.getY(i);
                    break;
                case Polygon.UP_DOWN:
                    x=poly.getX(i);
                    y=poly.getY(i);
                    z=intersectValue;
                    break;
                default:
                    x=0;
                    y=0;
                    z=0;
                    break;
            }
            p.pointList.add(new CoordinateOfPoint(x,y,z));
            p.direction=type;
        }
        for(int i=0;i<n-1;i++) {
            p.edgeList.add(new Edge(p.pointList.get(i),p.pointList.get(i+1)));
        }
        p.edgeList.add(new Edge(p.pointList.get(n-1),p.pointList.get(0)));
        return p;
    }

    public static void testArrayList() {
        ArrayList<Integer> il = new ArrayList<Integer>();
        il.add(100);
        il.add(200);
        il.add(300);
        for (Integer i : il) {
//            System.out.println(i);
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
//        System.out.print(p.getEdgeList()+"\n");
//        System.out.print(p.getPointList());
    }

    public double getIntersectValue() {
        switch (direction) {
            case Polygon.FRONT_BOOTOM:
                return this.pointList.get(0).getX();
            case Polygon.LEFT_RIGHT:
                return this.pointList.get(0).getY();
            case Polygon.UP_DOWN:
                return this.pointList.get(0).getZ();
        }
        return 0.0;
    }
}
