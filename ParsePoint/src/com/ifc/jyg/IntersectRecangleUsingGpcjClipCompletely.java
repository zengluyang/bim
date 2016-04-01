package com.ifc.jyg;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

import java.util.*;


public class IntersectRecangleUsingGpcjClipCompletely {
    class PloyGpcjResult {
        public Poly polyGpcj;
        public int direction;
        public double intersectValue;
        public ArrayList<String> idList = new ArrayList<String>();
        public ArrayList<Rectangle> rawRectangleList = new ArrayList<Rectangle>();
        PloyGpcjResult() {

        }

        public String getIds() {
            return String.join(",",this.idList);
        }
    }
    private Map<Integer, Map<Double,  ArrayList<Rectangle>>> directionDoubleMap;
    private Map<Integer, ArrayList<Rectangle>> directionMap = new HashMap<Integer, ArrayList<Rectangle>>();
    private ArrayList<TreeSet<Rectangle>> recSetList = new ArrayList<>();
    private ArrayList<Polygon> resultPolyList = new ArrayList<Polygon>();

    public IntersectRecangleUsingGpcjClipCompletely() {
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


    public ArrayList<PloyGpcjResult> doClip() {
        ArrayList<PloyGpcjResult> rlt= new ArrayList<PloyGpcjResult>();
        ArrayList<ArrayList<Rectangle>> recListList = this.doPart();
        for(ArrayList<Rectangle> recList:recListList) {
            ArrayList<Poly> intersectionRltGpcjList = new ArrayList<Poly>();
            Poly unionRltGpcj = new PolyDefault();
            PloyGpcjResult ployGpcjResult = new PloyGpcjResult();
            ployGpcjResult.direction = recList.get(0).getDirection();
            ployGpcjResult.intersectValue = recList.get(0).getIntersectValue();
            ployGpcjResult.rawRectangleList = recList;
            for(int i=0;i<recList.size();i++) {
                Rectangle ri = recList.get(i);
                ployGpcjResult.idList.add(ri.Id);
                unionRltGpcj=unionRltGpcj.union(Polygon.convertToGpcjPoly(ri));
                for(int j=recList.size()-1;j>i;j--) {
                    Rectangle rj = recList.get(j);
                    intersectionRltGpcjList.add(Polygon.convertToGpcjPoly(ri).intersection(Polygon.convertToGpcjPoly(rj)));
                }
            }
            Poly interUnionRltGpcj = new PolyDefault();
            for(Poly p:intersectionRltGpcjList) {
                interUnionRltGpcj=interUnionRltGpcj.union(p);
            }
            Poly rltLocal = unionRltGpcj.xor(interUnionRltGpcj);
            if(rltLocal instanceof PolyDefault) {
                PolyDefault rltLocalPd = (PolyDefault) rltLocal;
                if(rltLocal.getNumInnerPoly()>=2) {
//                    System.out.println(Polygon.testPolyDefautToMatlab(rltLocalPd));
//                    System.out.print('\n');
                }
            }
            ployGpcjResult.polyGpcj = rltLocal;
            rlt.add(ployGpcjResult);
        }
        return rlt;
    }

    public ArrayList<ArrayList<Rectangle>> doPart() {
        ArrayList<ArrayList<Rectangle>> rlt = new ArrayList<ArrayList<Rectangle>>();
        for(Integer direction:directionDoubleMap.keySet()) {
            Map<Double, ArrayList<Rectangle>> intersetValueMap = directionDoubleMap.get(direction);
            for(Double intersectValue:intersetValueMap.keySet()) {
                ArrayList<Rectangle> recList = intersetValueMap.get(intersectValue);
                ArrayList<ArrayList<Rectangle>> localRecSetList = new ArrayList<>();
                for(Rectangle r:recList) {
                    boolean isInserted = false;
                    for(ArrayList<Rectangle>recSet :localRecSetList) {

                        for(Rectangle rr:recSet) {
                            if(
                                    rr.compareByConataination(r)==1
                                            || rr.compareByConataination(r)== 2
                                            || rr.compareByConataination(r)==-1
                                    ) {
                                recSet.add(r);
                                isInserted = true;
                                break;
                            }
                        }
                        if(isInserted) {
                            break;
                        }
                    }

                    if(!isInserted) {
                        ArrayList<Rectangle> recSet = new ArrayList<>();
                        recSet.add(r);
                        localRecSetList.add(recSet);
                    }
                }
                rlt.addAll(localRecSetList);
            }
        }
        return rlt;
    }

    public ArrayList<Polygon> getIntersectResult() {
        return resultPolyList;
    }

}
