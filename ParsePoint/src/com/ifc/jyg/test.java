package com.ifc.jyg;

import com.bim.jyg.RectangleDecompositor;
import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import com.seisw.util.geom.PolySimple;
import com.seisw.util.geom.Rectangle2D;
import org.math.plot.Plot2DPanel;
import org.math.plot.canvas.Plot2DCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class test {

	public static void main(String[] args) throws IOException {
//		Rectangle.testcontrunctPolygonsUsingBigRectangleAndSmallRectangles();
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
		final NewFrame frame1 = new NewFrame();
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//涓�瀹氳璁剧疆鍏抽棴
		frame1.setVisible(true);
		JButton button = frame1.getButton();
		final IfcExtrator ifcExtrator = new IfcExtrator();
		JButton button3 = new JButton("draw");
		button3.setBounds(103,110,71,27);
		button3.setText("draw");
		button3.setToolTipText("draw");
		frame1.add(button3);
		final RectangleDecompositor[] rectangleDecompositor = new RectangleDecompositor[1];
		double standardLength = 0.6;
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = frame1.getBox().getSelectedIndex();
				PloyGpcjResult ployGpcjResult;
				if(frame1.getTextField().getText().equals("")) {
					ployGpcjResult = ifcExtrator.ployGpcjResultList.get(i);
				} else {
					ployGpcjResult = ifcExtrator.ployGpcjResultHashMap.get(frame1.getTextField().getText());
				}
				PolyDefault pd =  (PolyDefault)ployGpcjResult.polyGpcj;
				double intersectValue = ployGpcjResult.intersectValue;
				int direction  =ployGpcjResult.direction;
				String ids =  ployGpcjResult.getIds();
				Map<Integer,ArrayList<CoordinateOfPoint>> idRecPoinListMap = rectangleDecompositor[0].getStandardRectangleIDAndPointsMapPolyIdMap().get(ids+" "+ployGpcjResult.direction+" "+ployGpcjResult.intersectValue);

				Plot2DPanel plot2DPanel = ployPolyDeafult(pd, intersectValue, direction, ids+" "+ployGpcjResult.direction+" "+ployGpcjResult.intersectValue,idRecPoinListMap,standardLength);
//				File png = new File("png\\"+ids.replace(':',' ').trim()+".png");
//				try {
//					plot2DPanel;
//				} catch (IOException ee) {
//					ee.printStackTrace();
//				}

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
						frame1.getBox().removeAllItems();
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
							ifcExtrator.printResultToFile2();
							rectangleDecompositor[0] = new RectangleDecompositor(ifcExtrator.rectangleIDAndPointsMap,"output.txt",standardLength);
							rectangleDecompositor[0].doDecompose();
							frame1.getLabel().setText("Extrated "+ ifcExtrator.ployGpcjResultList.size()+" results.");
							for(Polygon p:ifcExtrator.getPolyRlt()) {
								//frame1.getBox().addItem(p.Id+" "+Polygon.directionString[p.getDirection()]);
							}
							for(PloyGpcjResult polyGpcjRlt:ifcExtrator.ployGpcjResultList) {
								frame1.getBox().addItem(polyGpcjRlt.getIds()+" "+" "+polyGpcjRlt.direction+" "+polyGpcjRlt.intersectValue);
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

		//button.doClick();

	}

	private static Plot2DPanel ployPolyDeafult(PolyDefault pd, double intersectValue, int type, String id,Map<Integer,ArrayList<CoordinateOfPoint>> idRecPoinListMap,double standardLength) {
		Plot2DPanel plot = new Plot2DPanel();
		JFrame frame = new JFrame(id+" "+Polygon.directionString[type]);
		frame.setContentPane(plot);
		frame.setVisible(true);

		int width = 500;
		int height = 500;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double bw;
		double bh;
		try {
			Rectangle2D bounds = pd.getBounds();
			bw = bounds.getWidth();
			bh = bounds.getHeight();

		} catch (UnsupportedOperationException e) {
			//e.printStackTrace();
			double[] widthHeight = Polygon.getBoundsFromtFromGpcjPolyDefault(pd);
			bw = widthHeight[0];
			bh = widthHeight[1];
		}

		double scalew = d.width/bw;
		double scaleh = d.height/bh;
		double scale;
		if(scalew>1&&scalew>1) {
			scale=min(scalew,scaleh);
		} else if(scalew<1&&scalew<1) {
			scale=max(scalew,scaleh);
		} else {
			scale = 1.0;
		}

//		if(bh>bw) {
//			scale = d.height/bh;
//		} else {
//			scale = d.width/bw;
//		}

		if(scale>1) {
			scale*=0.8;
			height= (int) (bh*scale);
			width= (int) (bw*scale);
		} else {
			scale*=0.8;
			height= (int) (bh/scale);
			width= (int) (bw/scale);
		}


		if(height<10 || width<10) {
			System.out.println("warning:plot too small,height<10 || width<10!");
			if(height<10) {
				height=100;
			} else if(width<10) {
				width=100;
			}
			if(height==0) {
				height=50;
			} else if(width==0){
				width=50;
			}
		}
		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		frame.setSize(width,height);
		plot.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		plot.setSize(width,height);

		if(id.equals("BEA_____:200_x300mm:742669 0 -313.464")) {
			// 1
			addLineFromPloyDeafult(plot,pd,intersectValue,type,id);

			// 2
//			PolyDefault ppd = new PolyDefault();
//			double x=0;
//			double y=0;
//			ppd.add(x,y);
//			addLineFromPloyDeafult(plot,ppd,intersectValue,type,id);
			// 3
//			Plot2DCanvas plot2DCanvas = (Plot2DCanvas) plot.plotCanvas;
//			double [][] XY = {
//					{rec.downLeft.getX2d(rec.getDirection()),rec.downLeft.getY2d(rec.getDirection())},
//					{rec.topRight.getX2d(rec.getDirection()),rec.topRight.getY2d(rec.getDirection())}
//			};
//			double x = (rec.downLeft.getX2d(rec.getDirection())+rec.topRight.getX2d(rec.getDirection()))/2.0;
//			double y = (rec.downLeft.getY2d(rec.getDirection())+rec.topRight.getY2d(rec.getDirection()))/2.0;
//			String wh = (int)(0.3*500)+"*"+(int)(0.3*1000);
//			plot2DCanvas.addPlot(new TextPlot("",Color.blue,XY,wh,x,y));
			PolyDefault ppd1 = new PolyDefault();
			double x = -44.1945;
			double y = 2.3500;
			ppd1.add(x, y);
			y = 2.0500;
			ppd1.add(x, y);
			x = -43.5945;
			ppd1.add(x, y);
			y = 2.3500;
			ppd1.add(x, y);
			addLineFromPloyDeafult(plot, ppd1, intersectValue, type, id);
			Plot2DCanvas plot2DCanvas = (Plot2DCanvas) plot.plotCanvas;
			double [][] XY = {
					{-44.1945, 2.05}, {-42.5945, 2.35}
			};
			double x_1 = -43.00;
			double y_1 = 2.20;
			String wh = "This is a test";
			plot2DCanvas.addPlot(new TextPlot("", Color.blue, XY, wh, x_1, y_1));


		} else if (id.equals("__:___-_120mm:679224 2 4.63")) {
			addLineFromPloyDeafult(plot, pd, intersectValue, type, id);
			double x;
			double y;
			PolyDefault ppd_1_1 = new PolyDefault();
			x = -296.614;
			y = -125.994;
			ppd_1_1.add(x, y);
			x = -297.51399999999995;
			ppd_1_1.add(x, y);
			y = -124.194;
			ppd_1_1.add(x, y);
			x = -296.614;
			ppd_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1, intersectValue, type, id);
			PolyDefault ppd_1_2 = new PolyDefault();
			x = -296.614;
			y = -124.194;
			ppd_1_2.add(x, y);
			x = -297.51399999999995;
			ppd_1_2.add(x, y);
			y = -122.394;
			ppd_1_2.add(x, y);
			x = -296.614;
			ppd_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_2, intersectValue, type, id);
			PolyDefault ppd_1_3 = new PolyDefault();
			x = -296.614;
			y = -122.394;
			ppd_1_3.add(x, y);
			x = -297.51399999999995;
			ppd_1_3.add(x, y);
			y = -120.594;
			ppd_1_3.add(x, y);
			x = -296.614;
			ppd_1_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_3, intersectValue, type, id);
			PolyDefault ppd_1_4 = new PolyDefault();
			x = -296.614;
			y = -120.594;
			ppd_1_4.add(x, y);
			x = -297.51399999999995;
			ppd_1_4.add(x, y);
			y = -118.794;
			ppd_1_4.add(x, y);
			x = -296.614;
			ppd_1_4.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_4, intersectValue, type, id);
			PolyDefault ppd_2_1 = new PolyDefault();
			x = -297.51399999999995;
			y = -125.994;
			ppd_2_1.add(x, y);
			x = -298.414;
			ppd_2_1.add(x, y);
			y = -124.194;
			ppd_2_1.add(x, y);
			x = -297.51399999999995;
			ppd_2_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_1, intersectValue, type, id);
			PolyDefault ppd_2_2 = new PolyDefault();
			x = -297.51399999999995;
			y = -124.194;
			ppd_2_2.add(x, y);
			x = -298.414;
			ppd_2_2.add(x, y);
			y = -122.394;
			ppd_2_2.add(x, y);
			x = -297.51399999999995;
			ppd_2_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_2, intersectValue, type, id);
			PolyDefault ppd_2_3 = new PolyDefault();
			x = -297.51399999999995;
			y = -122.394;
			ppd_2_3.add(x, y);
			x = -298.414;
			ppd_2_3.add(x, y);
			y = -120.594;
			ppd_2_3.add(x, y);
			x = -297.51399999999995;
			ppd_2_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_3, intersectValue, type, id);
			PolyDefault ppd_2_4 = new PolyDefault();
			x = -297.51399999999995;
			y = -120.594;
			ppd_2_4.add(x, y);
			x = -298.414;
			ppd_2_4.add(x, y);
			y = -118.794;
			ppd_2_4.add(x, y);
			x = -297.51399999999995;
			ppd_2_4.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_4, intersectValue, type, id);
			PolyDefault ppd_3_1 = new PolyDefault();
			x = -298.414;
			y = -125.994;
			ppd_3_1.add(x, y);
			x = -299.31399999999996;
			ppd_3_1.add(x, y);
			y = -124.194;
			ppd_3_1.add(x, y);
			x = -298.414;
			ppd_3_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_1, intersectValue, type, id);
			PolyDefault ppd_3_2 = new PolyDefault();
			x = -298.414;
			y = -124.194;
			ppd_3_2.add(x, y);
			x = -299.31399999999996;
			ppd_3_2.add(x, y);
			y = -122.394;
			ppd_3_2.add(x, y);
			x = -298.414;
			ppd_3_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_2, intersectValue, type, id);
			PolyDefault ppd_3_3 = new PolyDefault();
			x = -298.414;
			y = -122.394;
			ppd_3_3.add(x, y);
			x = -299.31399999999996;
			ppd_3_3.add(x, y);
			y = -120.594;
			ppd_3_3.add(x, y);
			x = -298.414;
			ppd_3_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_3, intersectValue, type, id);
			PolyDefault ppd_3_4 = new PolyDefault();
			x = -298.414;
			y = -120.594;
			ppd_3_4.add(x, y);
			x = -299.31399999999996;
			ppd_3_4.add(x, y);
			y = -118.794;
			ppd_3_4.add(x, y);
			x = -298.414;
			ppd_3_4.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_4, intersectValue, type, id);
			PolyDefault ppd_2_1_1 = new PolyDefault();
			x = -296.614;
			y = -118.794;
			ppd_2_1_1.add(x, y);
			x = -297.51399999999995;
			ppd_2_1_1.add(x, y);
			y = -118.344;
			ppd_2_1_1.add(x, y);
			x = -296.614;
			ppd_2_1_1.add(x, y);
			id = "red";
			addLineFromPloyDeafult(plot, ppd_2_1_1, intersectValue, type, id);
			PolyDefault ppd_2_2_1 = new PolyDefault();
			x = -297.51399999999995;
			y = -118.794;
			ppd_2_2_1.add(x, y);
			x = -298.414;
			ppd_2_2_1.add(x, y);
			y = -118.344;
			ppd_2_2_1.add(x, y);
			x = -297.51399999999995;
			ppd_2_2_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_2_1, intersectValue, type, id);
			PolyDefault ppd_2_3_1 = new PolyDefault();
			x = -298.414;
			y = -118.794;
			ppd_2_3_1.add(x, y);
			x = -299.31399999999996;
			ppd_2_3_1.add(x, y);
			y = -118.344;
			ppd_2_3_1.add(x, y);
			x = -298.414;
			ppd_2_3_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_3_1, intersectValue, type, id);
			PolyDefault ppd_3_1_1 = new PolyDefault();
			id = "green";
			x = -296.614;
			y = -118.344;
			ppd_3_1_1.add(x, y);
			x = -296.914;
			ppd_3_1_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_1_1.add(x, y);
			x = -296.614;
			ppd_3_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_1_1, intersectValue, type, id);
			PolyDefault ppd_3_2_1 = new PolyDefault();
			x = -296.914;
			y = -118.344;
			ppd_3_2_1.add(x, y);
			x = -297.214;
			ppd_3_2_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_2_1.add(x, y);
			x = -296.914;
			ppd_3_2_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_2_1, intersectValue, type, id);
			PolyDefault ppd_3_3_1 = new PolyDefault();
			x = -297.214;
			y = -118.344;
			ppd_3_3_1.add(x, y);
			x = -297.51399999999995;
			ppd_3_3_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_3_1.add(x, y);
			x = -297.214;
			ppd_3_3_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_3_1, intersectValue, type, id);
			PolyDefault ppd_3_4_1 = new PolyDefault();
			x = -297.51399999999995;
			y = -118.344;
			ppd_3_4_1.add(x, y);
			x = -297.81399999999996;
			ppd_3_4_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_4_1.add(x, y);
			x = -297.51399999999995;
			ppd_3_4_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_4_1, intersectValue, type, id);
			PolyDefault ppd_3_5_1 = new PolyDefault();
			x = -297.81399999999996;
			y = -118.344;
			ppd_3_5_1.add(x, y);
			x = -298.114;
			ppd_3_5_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_5_1.add(x, y);
			x = -297.81399999999996;
			ppd_3_5_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_5_1, intersectValue, type, id);
			PolyDefault ppd_3_6_1 = new PolyDefault();
			x = -298.114;
			y = -118.344;
			ppd_3_6_1.add(x, y);
			x = -298.414;
			ppd_3_6_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_6_1.add(x, y);
			x = -298.114;
			ppd_3_6_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_6_1, intersectValue, type, id);
			PolyDefault ppd_3_7_1 = new PolyDefault();
			x = -298.414;
			y = -118.344;
			ppd_3_7_1.add(x, y);
			x = -298.714;
			ppd_3_7_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_7_1.add(x, y);
			x = -298.414;
			ppd_3_7_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_7_1, intersectValue, type, id);
			PolyDefault ppd_3_8_1 = new PolyDefault();
			x = -298.714;
			y = -118.344;
			ppd_3_8_1.add(x, y);
			x = -299.01399999999995;
			ppd_3_8_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_8_1.add(x, y);
			x = -298.714;
			ppd_3_8_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_8_1, intersectValue, type, id);
			PolyDefault ppd_3_9_1 = new PolyDefault();
			x = -299.01399999999995;
			y = -118.344;
			ppd_3_9_1.add(x, y);
			x = -299.31399999999996;
			ppd_3_9_1.add(x, y);
			y = -118.19399999999999;
			ppd_3_9_1.add(x, y);
			x = -299.01399999999995;
			ppd_3_9_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_3_9_1, intersectValue, type, id);
			id = "black";
			PolyDefault ppd_4_1_1 = new PolyDefault();
			x = -296.614;
			y = -118.209;
			ppd_4_1_1.add(x, y);
			x = -297.51399999999995;
			ppd_4_1_1.add(x, y);
			y = -118.084;
			ppd_4_1_1.add(x, y);
			x = -296.614;
			ppd_4_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_4_1_1, intersectValue, type, id);
			PolyDefault ppd_4_2_1 = new PolyDefault();
			x = -297.51399999999995;
			y = -118.209;
			ppd_4_2_1.add(x, y);
			x = -298.414;
			ppd_4_2_1.add(x, y);
			y = -118.084;
			ppd_4_2_1.add(x, y);
			x = -297.51399999999995;
			ppd_4_2_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_4_2_1, intersectValue, type, id);
			PolyDefault ppd_4_3_1 = new PolyDefault();
			x = -298.414;
			y = -118.209;
			ppd_4_3_1.add(x, y);
			x = -299.31399999999996;
			ppd_4_3_1.add(x, y);
			y = -118.084;
			ppd_4_3_1.add(x, y);
			x = -298.414;
			ppd_4_3_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_4_3_1, intersectValue, type, id);
			id = "cyan";
			PolyDefault ppd_5_1_1 = new PolyDefault();
			x = -299.314;
			y = -125.994;
			ppd_5_1_1.add(x, y);
			x = -299.764;
			ppd_5_1_1.add(x, y);
			y = -125.094;
			ppd_5_1_1.add(x, y);
			x = -299.314;
			ppd_5_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_1, intersectValue, type, id);
			PolyDefault ppd_5_1_2 = new PolyDefault();
			x = -299.314;
			y = -125.094;
			ppd_5_1_2.add(x, y);
			x = -299.764;
			ppd_5_1_2.add(x, y);
			y = -124.194;
			ppd_5_1_2.add(x, y);
			x = -299.314;
			ppd_5_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_2, intersectValue, type, id);
			PolyDefault ppd_5_1_3 = new PolyDefault();
			x = -299.314;
			y = -124.194;
			ppd_5_1_3.add(x, y);
			x = -299.764;
			ppd_5_1_3.add(x, y);
			y = -123.294;
			ppd_5_1_3.add(x, y);
			x = -299.314;
			ppd_5_1_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_3, intersectValue, type, id);
			PolyDefault ppd_5_1_4 = new PolyDefault();
			x = -299.314;
			y = -123.294;
			ppd_5_1_4.add(x, y);
			x = -299.764;
			ppd_5_1_4.add(x, y);
			y = -122.394;
			ppd_5_1_4.add(x, y);
			x = -299.314;
			ppd_5_1_4.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_4, intersectValue, type, id);
			PolyDefault ppd_5_1_5 = new PolyDefault();
			x = -299.314;
			y = -122.394;
			ppd_5_1_5.add(x, y);
			x = -299.764;
			ppd_5_1_5.add(x, y);
			y = -121.494;
			ppd_5_1_5.add(x, y);
			x = -299.314;
			ppd_5_1_5.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_5, intersectValue, type, id);
			PolyDefault ppd_5_1_6 = new PolyDefault();
			x = -299.314;
			y = -121.494;
			ppd_5_1_6.add(x, y);
			x = -299.764;
			ppd_5_1_6.add(x, y);
			y = -120.594;
			ppd_5_1_6.add(x, y);
			x = -299.314;
			ppd_5_1_6.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_6, intersectValue, type, id);
			PolyDefault ppd_5_1_7 = new PolyDefault();
			x = -299.314;
			y = -120.594;
			ppd_5_1_7.add(x, y);
			x = -299.764;
			ppd_5_1_7.add(x, y);
			y = -119.694;
			ppd_5_1_7.add(x, y);
			x = -299.314;
			ppd_5_1_7.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_7, intersectValue, type, id);
			PolyDefault ppd_5_1_8 = new PolyDefault();
			x = -299.314;
			y = -119.694;
			ppd_5_1_8.add(x, y);
			x = -299.764;
			ppd_5_1_8.add(x, y);
			y = -118.794;
			ppd_5_1_8.add(x, y);
			x = -299.314;
			ppd_5_1_8.add(x, y);
			addLineFromPloyDeafult(plot, ppd_5_1_8, intersectValue, type, id);
			id = "magenta";
			PolyDefault ppd_6_1_1 = new PolyDefault();
			x = -299.764;
			y = -125.994;
			ppd_6_1_1.add(x, y);
			x = -299.889;
			ppd_6_1_1.add(x, y);
			y = -125.094;
			ppd_6_1_1.add(x, y);
			x = -299.764;
			ppd_6_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_1, intersectValue, type, id);
			PolyDefault ppd_6_1_2 = new PolyDefault();
			x = -299.764;
			y = -125.094;
			ppd_6_1_2.add(x, y);
			x = -299.889;
			ppd_6_1_2.add(x, y);
			y = -124.194;
			ppd_6_1_2.add(x, y);
			x = -299.764;
			ppd_6_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_2, intersectValue, type, id);
			PolyDefault ppd_6_1_3 = new PolyDefault();
			x = -299.764;
			y = -124.194;
			ppd_6_1_3.add(x, y);
			x = -299.889;
			ppd_6_1_3.add(x, y);
			y = -123.294;
			ppd_6_1_3.add(x, y);
			x = -299.764;
			ppd_6_1_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_3, intersectValue, type, id);
			PolyDefault ppd_6_1_4 = new PolyDefault();
			x = -299.764;
			y = -123.294;
			ppd_6_1_4.add(x, y);
			x = -299.889;
			ppd_6_1_4.add(x, y);
			y = -122.394;
			ppd_6_1_4.add(x, y);
			x = -299.764;
			ppd_6_1_4.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_4, intersectValue, type, id);
			PolyDefault ppd_6_1_5 = new PolyDefault();
			x = -299.764;
			y = -122.394;
			ppd_6_1_5.add(x, y);
			x = -299.889;
			ppd_6_1_5.add(x, y);
			y = -121.494;
			ppd_6_1_5.add(x, y);
			x = -299.764;
			ppd_6_1_5.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_5, intersectValue, type, id);
			PolyDefault ppd_6_1_6 = new PolyDefault();
			x = -299.764;
			y = -121.494;
			ppd_6_1_6.add(x, y);
			x = -299.889;
			ppd_6_1_6.add(x, y);
			y = -120.594;
			ppd_6_1_6.add(x, y);
			x = -299.764;
			ppd_6_1_6.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_6, intersectValue, type, id);
			PolyDefault ppd_6_1_7 = new PolyDefault();
			x = -299.764;
			y = -120.594;
			ppd_6_1_7.add(x, y);
			x = -299.889;
			ppd_6_1_7.add(x, y);
			y = -119.694;
			ppd_6_1_7.add(x, y);
			x = -299.764;
			ppd_6_1_7.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_7, intersectValue, type, id);
			PolyDefault ppd_6_1_8 = new PolyDefault();
			x = -299.764;
			y = -119.694;
			ppd_6_1_8.add(x, y);
			x = -299.889;
			ppd_6_1_8.add(x, y);
			y = -118.794;
			ppd_6_1_8.add(x, y);
			x = -299.764;
			ppd_6_1_8.add(x, y);
			addLineFromPloyDeafult(plot, ppd_6_1_8, intersectValue, type, id);
			id = "pink";
			PolyDefault ppd_7_1_1 = new PolyDefault();
			x = -299.314;
			y = -118.794;
			ppd_7_1_1.add(x, y);
			x = -299.464;
			ppd_7_1_1.add(x, y);
			y = -118.494;
			ppd_7_1_1.add(x, y);
			x = -299.314;
			ppd_7_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_7_1_1, intersectValue, type, id);
			PolyDefault ppd_7_1_2 = new PolyDefault();
			x = -299.314;
			y = -118.494;
			ppd_7_1_2.add(x, y);
			x = -299.464;
			ppd_7_1_2.add(x, y);
			y = -118.194;
			ppd_7_1_2.add(x, y);
			x = -299.314;
			ppd_7_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_7_1_2, intersectValue, type, id);
			PolyDefault ppd_7_2_1 = new PolyDefault();
			x = -299.464;
			y = -118.794;
			ppd_7_2_1.add(x, y);
			x = -299.61400000000003;
			ppd_7_2_1.add(x, y);
			y = -118.494;
			ppd_7_2_1.add(x, y);
			x = -299.464;
			ppd_7_2_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_7_2_1, intersectValue, type, id);
			PolyDefault ppd_7_2_2 = new PolyDefault();
			x = -299.464;
			y = -118.494;
			ppd_7_2_2.add(x, y);
			x = -299.61400000000003;
			ppd_7_2_2.add(x, y);
			y = -118.194;
			ppd_7_2_2.add(x, y);
			x = -299.464;
			ppd_7_2_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_7_2_2, intersectValue, type, id);
			PolyDefault ppd_7_3_1 = new PolyDefault();
			x = -299.61400000000003;
			y = -118.794;
			ppd_7_3_1.add(x, y);
			x = -299.764;
			ppd_7_3_1.add(x, y);
			y = -118.494;
			ppd_7_3_1.add(x, y);
			x = -299.61400000000003;
			ppd_7_3_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_7_3_1, intersectValue, type, id);
			PolyDefault ppd_7_3_2 = new PolyDefault();
			x = -299.61400000000003;
			y = -118.494;
			ppd_7_3_2.add(x, y);
			x = -299.764;
			ppd_7_3_2.add(x, y);
			y = -118.194;
			ppd_7_3_2.add(x, y);
			x = -299.61400000000003;
			ppd_7_3_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_7_3_2, intersectValue, type, id);


		} else if (id.equals("BEA_____:350_x_600mm:802339 2 -300.239"))
		{
			addLineFromPloyDeafult(plot,pd,intersectValue,type,id);
			double x, y;
			PolyDefault ppd_1_1_1 = new PolyDefault();
			x = -299.889;
			y = -118.194;
			ppd_1_1_1.add(x, y);
			x = -300.23900000000003;
			ppd_1_1_1.add(x, y);
			y = -119.994;
			ppd_1_1_1.add(x, y);
			x = -299.889;
			ppd_1_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_1, intersectValue, type, id);
			PolyDefault ppd_1_1_2 = new PolyDefault();
			x = -299.889;
			y = -119.994;
			ppd_1_1_2.add(x, y);
			x = -300.23900000000003;
			ppd_1_1_2.add(x, y);
			y = -121.794;
			ppd_1_1_2.add(x, y);
			x = -299.889;
			ppd_1_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_2, intersectValue, type, id);
			PolyDefault ppd_1_1_3 = new PolyDefault();
			x = -299.889;
			y = -121.794;
			ppd_1_1_3.add(x, y);
			x = -300.23900000000003;
			ppd_1_1_3.add(x, y);
			y = -123.59400000000001;
			ppd_1_1_3.add(x, y);
			x = -299.889;
			ppd_1_1_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_3, intersectValue, type, id);
			PolyDefault ppd_1_1_4 = new PolyDefault();
			x = -299.889;
			y = -123.59400000000001;
			ppd_1_1_4.add(x, y);
			x = -300.23900000000003;
			ppd_1_1_4.add(x, y);
			y = -125.394;
			ppd_1_1_4.add(x, y);
			x = -299.889;
			ppd_1_1_4.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_4, intersectValue, type, id);
			PolyDefault ppd = new PolyDefault();
			x = -299.939;
			y = -125.994;
			ppd.add(x, y);
			y = -125.394;
			ppd.add(x, y);
			id = "red";
			addLineFromPloyDeafult(plot, ppd, intersectValue, type, id);
			PolyDefault ppd2 = new PolyDefault();
			x = -299.889;
			y = -125.694;
			ppd2.add(x, y);
			x = -299.939;
			ppd2.add(x, y);
			id = "black";
			addLineFromPloyDeafult(plot, ppd2, intersectValue, type, id);

		}
		else if (id.equals("BEA_____:350_x_600mm:802339,__:___-_120mm:679224 0 -299.889")) {
			addLineFromPloyDeafult(plot,pd,intersectValue,type,id);
			id = "red";
			double x, y;
			PolyDefault ppd_1_1_1 = new PolyDefault();
			x = -118.194;
			y = 4.15;
			ppd_1_1_1.add(x, y);
			x = -119.994;
			ppd_1_1_1.add(x, y);
			y = 4.630000000000001;
			ppd_1_1_1.add(x, y);
			x = -118.194;
			ppd_1_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_1, intersectValue, type, id);
			PolyDefault ppd_1_2_1 = new PolyDefault();
			x = -119.994;
			y = 4.15;
			ppd_1_2_1.add(x, y);
			x = -121.794;
			ppd_1_2_1.add(x, y);
			y = 4.630000000000001;
			ppd_1_2_1.add(x, y);
			x = -119.994;
			ppd_1_2_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_2_1, intersectValue, type, id);
			PolyDefault ppd_1_3_1 = new PolyDefault();
			x = -121.794;
			y = 4.15;
			ppd_1_3_1.add(x, y);
			x = -123.59400000000001;
			ppd_1_3_1.add(x, y);
			y = 4.630000000000001;
			ppd_1_3_1.add(x, y);
			x = -121.794;
			ppd_1_3_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_3_1, intersectValue, type, id);
			PolyDefault ppd_1_4_1 = new PolyDefault();
			x = -123.59400000000001;
			y = 4.15;
			ppd_1_4_1.add(x, y);
			x = -125.394;
			ppd_1_4_1.add(x, y);
			y = 4.630000000000001;
			ppd_1_4_1.add(x, y);
			x = -123.59400000000001;
			ppd_1_4_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_4_1, intersectValue, type, id);
			PolyDefault ppd2 = new PolyDefault();
			x = -125.994;
			y = 4.45;
			ppd2.add(x, y);
			x = -125.394;
			ppd2.add(x, y);
			id = "black";
			addLineFromPloyDeafult(plot, ppd2, intersectValue, type, id);
		}
		else if (id.equals("COL_____:600_x_600mm:595530 0 -300.364")) {
			double x, y;
			addLineFromPloyDeafult(plot,pd,intersectValue,type,id);
			id = "red";
			PolyDefault ppd_1_1_1 = new PolyDefault();
			x = -117.594;
			y = 0.0;
			ppd_1_1_1.add(x, y);
			x = -118.19399999999999;
			ppd_1_1_1.add(x, y);
			y = 1.8;
			ppd_1_1_1.add(x, y);
			x = -117.594;
			ppd_1_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_1, intersectValue, type, id);
			PolyDefault ppd_1_1_2 = new PolyDefault();
			x = -117.594;
			y = 1.8;
			ppd_1_1_2.add(x, y);
			x = -118.19399999999999;
			ppd_1_1_2.add(x, y);
			y = 3.6;
			ppd_1_1_2.add(x, y);
			x = -117.594;
			ppd_1_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_1_1_2, intersectValue, type, id);
			PolyDefault ppd_2_1_1 = new PolyDefault();
			x = -117.594;
			y = 3.6;
			ppd_2_1_1.add(x, y);
			x = -118.19399999999999;
			ppd_2_1_1.add(x, y);
			y = 3.9;
			ppd_2_1_1.add(x, y);
			x = -117.594;
			ppd_2_1_1.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_1_1, intersectValue, type, id);
			PolyDefault ppd_2_1_2 = new PolyDefault();
			x = -117.594;
			y = 3.9;
			ppd_2_1_2.add(x, y);
			x = -118.19399999999999;
			ppd_2_1_2.add(x, y);
			y = 4.2;
			ppd_2_1_2.add(x, y);
			x = -117.594;
			ppd_2_1_2.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_1_2, intersectValue, type, id);
			PolyDefault ppd_2_1_3 = new PolyDefault();
			x = -117.594;
			y = 4.2;
			ppd_2_1_3.add(x, y);
			x = -118.19399999999999;
			ppd_2_1_3.add(x, y);
			y = 4.5;
			ppd_2_1_3.add(x, y);
			x = -117.594;
			ppd_2_1_3.add(x, y);
			addLineFromPloyDeafult(plot, ppd_2_1_3, intersectValue, type, id);

		}
		else  {
			addLineFromPloyDeafult(plot,pd,intersectValue,type,id);
			Plot2DCanvas plot2DCanvas = (Plot2DCanvas) plot.plotCanvas;
			for (Integer i: idRecPoinListMap.keySet()
					) {
				ArrayList<CoordinateOfPoint> points = idRecPoinListMap.get(i);
				Rectangle rec = new Rectangle(points,id);
				PolyDefault polyDefault = converToGpcjPoly(rec);
				addLineFromPloyDeafult(plot,polyDefault,intersectValue,type,id);

				double [][] XY = {
						{rec.downLeft.getX2d(rec.getDirection()),rec.downLeft.getY2d(rec.getDirection())},
						{rec.topRight.getX2d(rec.getDirection()),rec.topRight.getY2d(rec.getDirection())}
				};
				double x = (rec.downLeft.getX2d(rec.getDirection())+rec.topRight.getX2d(rec.getDirection()))/2.0;
				double y = (rec.downLeft.getY2d(rec.getDirection())+rec.topRight.getY2d(rec.getDirection()))/2.0;
				String wh = (int)(standardLength*500)+"*"+(int)(standardLength*1000);
				plot2DCanvas.addPlot(new TextPlot("",Color.blue,XY,wh,x,y));

			}

		}

		return plot;
	}

	private static void addLineFromPloyDeafult(Plot2DPanel plot, PolyDefault pd,double intersectValue, int type, String id) {
		for(int i=0;i<pd.getNumInnerPoly();i++) {
			Poly p = pd.getInnerPoly(i);
			if(p instanceof PolySimple) {
				PolySimple ps = (PolySimple) p;
				addLineFromPloySimple(plot,ps,intersectValue,type,id);
			} else if(p instanceof PolyDefault) {
				PolyDefault pdi = (PolyDefault)p;
				addLineFromPloyDeafult(plot,pdi,intersectValue,type,id);
			}
		}
	}

	private static void addLineFromPloySimple(Plot2DPanel plot,PolySimple ps, double intersectValue, int type, String id) {
		Polygon p = Polygon.convertFromGpcjPolySimple(ps,intersectValue,type,id);
		double[] xs = new double[p.getEdgeList().size()*2];
		double[] ys = new double[p.getEdgeList().size()*2];
		int i=0;
		for(Edge e:p.getEdgeList()) {
			double[] ixs = {e.getFirst().getX2d(p.getDirection()),e.getFirst().getY2d(p.getDirection())};
			double[] iys = {e.getSecond().getX2d(p.getDirection()),e.getSecond().getY2d(p.getDirection())};
			plot.addLinePlot(p.Id,Color.BLUE,ixs,iys);
		}
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
