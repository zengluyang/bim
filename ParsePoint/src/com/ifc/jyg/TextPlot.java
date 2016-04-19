package com.ifc.jyg;

import org.math.plot.plots.LinePlot;
import org.math.plot.plots.Plot;
import org.math.plot.plots.ScatterPlot;
import org.math.plot.render.AbstractDrawer;

import java.awt.*;

/**
 * Created by ZLY on 2016/4/19.
 */
public class TextPlot extends ScatterPlot {

    public String text;
    public double x;
    public double y;
    public TextPlot(String n, Color c, double[][] XY,String text,double x,double y) {
        super(n,c,XY);
        this.text=text;
        this.x=x;
        this.y=y;
    }

    @Override
    public void plot(AbstractDrawer draw, Color c) {
        double[] pos = {x,y};
        draw.drawText(text,pos);
    }
}
