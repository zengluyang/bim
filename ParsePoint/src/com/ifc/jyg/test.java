package com.ifc.jyg;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import org.math.plot.Plot2DPanel;
import org.math.plot.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.Exchanger;

public class test {

	public static void main(String[] args) throws IOException {
		showGUI();
	}


	private static PolyDefault converToGpcjPoly(Polygon p) {
		PolyDefault gpcjPoly = new PolyDefault();
		for(CoordinateOfPoint point:p.getPointList()) {
			gpcjPoly.add(point.getX2d(p.getDirection()),point.getY2d(p.getDirection()));
		}
		return gpcjPoly;
	}

	private static void showGUI () throws IOException{
		NewFrame frame1 = new NewFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//一定要设置关闭
		frame1.setVisible(true);
		JButton button = frame1.getButton();
		IfcExtrator ifcExtrator = new IfcExtrator();


		JButton button3 = new JButton("画图");
		button3.setBounds(103,110,71,27);
		button3.setText("画图");
		button3.setToolTipText("画图");
		frame1.add(button3);

		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = frame1.getBox().getSelectedIndex();
				plotPoly(ifcExtrator.getPolyRlt().get(i));
//				PolyDefault pd = Polygon.convertToGpcjPoly(ifcExtrator.getPolyRlt().get(i));
//				Polygon p = Polygon.convertFromGpcjPoly(pd,ifcExtrator.getPolyRlt().get(i).getIntersectValue(),ifcExtrator.getPolyRlt().get(i).getDirection());
//				p.toMatlab2D();
			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						frame1.getLabel().setText("Extrating!");
						try {
							File exeFile = frame1.exeFileChooser.getSelectedFile();
							File ifcFile = frame1.inputFileChooser.getSelectedFile();

							if(ifcFile!=null) {
								ifcExtrator.setInputIfcFileName(ifcFile.getAbsolutePath());
							}
							if(exeFile!=null) {
								ifcExtrator.setIfcConvertExeName(exeFile.getAbsolutePath());
							}
							ifcExtrator.extract();
							ifcExtrator.printResultToFile();
							frame1.getLabel().setText("Extrated"+ifcExtrator.successMessage);
							for(Polygon p:ifcExtrator.getPolyRlt()) {
								frame1.getBox().addItem(p.Id+" "+Polygon.directionString[p.getDirection()]);
							}
							// create your PlotPanel (you can use it as a JPanel)
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				thread.start();
			}
		});

		button.doClick();

	}
	private static void  plotPoly(Polygon p) {
		Plot2DPanel plot = new Plot2DPanel();

		// add a line plot to the PlotPanel
		double[] xs = new double[p.getEdgeList().size()*2];
		double[] ys = new double[p.getEdgeList().size()*2];
		int i=0;
		for(Edge e:p.getEdgeList()) {
			double[] ixs = {e.getFirst().getX2d(p.getDirection()),e.getFirst().getY2d(p.getDirection())};
			double[] iys = {e.getSecond().getX2d(p.getDirection()),e.getSecond().getY2d(p.getDirection())};
			plot.addLinePlot(p.Id,Color.BLUE,ixs,iys);
		}


		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame(p.Id+" "+Polygon.directionString[p.getDirection()]);
		frame.setContentPane(plot);
		frame.setVisible(true);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 500;
		int height = 500;
		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		frame.setSize(500,500);

	}
}
