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
		addLineFromPloyDeafult(plot,pd,intersectValue,type,id);
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
