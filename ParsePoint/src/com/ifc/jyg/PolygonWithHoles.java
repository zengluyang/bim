package com.ifc.jyg;

import java.util.ArrayList;

/**
 * Created by ZLY on 2016/3/27.
 */
public class PolygonWithHoles extends Polygon {
    ArrayList<Polygon> holes;
    PolygonWithHoles(ArrayList<Edge> edges,String id, ArrayList<Polygon> holes) {
        super(edges,id);
        this.holes=holes;
    }

    PolygonWithHoles(Polygon outter,String id, ArrayList<Polygon> holes) {
        this.edgeList = outter.edgeList;
        this.pointList = outter.pointList;
        this.holes=holes;
        this.Id=id;
    }

    @Override
    public String toString() {
        return "PolygonWithHoles{" +
                "mainPoly" + super.toString() +
                "holes=" + holes +
                '}';
    }

    @Override
    public String toMatlab2D() {
        StringBuilder sb = new StringBuilder(super.toMatlab2D());
        sb.append("%holes--------------\n");
        for(Polygon p:holes) {
            sb.append(p.toMatlab2D());
        }
        sb.append("%end of holes--------------\n");
        return sb.toString();
    }
}
