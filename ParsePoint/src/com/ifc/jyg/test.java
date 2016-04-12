package com.ifc.jyg;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import com.seisw.util.geom.PolySimple;
import com.seisw.util.geom.Rectangle2D;
import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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

		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = frame1.getBox().getSelectedIndex();
				PloyGpcjResult ployGpcjResult = ifcExtrator.ployGpcjResultList.get(i);
				PolyDefault pd =  (PolyDefault)ployGpcjResult.polyGpcj;
				double intersectValue = ployGpcjResult.intersectValue;
				int direction  =ployGpcjResult.direction;
				String ids =  ployGpcjResult.getIds();


				Plot2DPanel plot2DPanel = ployPolyDeafult(pd, intersectValue, direction, ids);
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
							frame1.getLabel().setText("Extrated "+ ifcExtrator.ployGpcjResultList.size()+" results.");
							for(Polygon p:ifcExtrator.getPolyRlt()) {
								//frame1.getBox().addItem(p.Id+" "+Polygon.directionString[p.getDirection()]);
							}
							for(PloyGpcjResult polyGpcjRlt:ifcExtrator.ployGpcjResultList) {
								frame1.getBox().addItem(polyGpcjRlt.getIds()+" "+Polygon.directionString[polyGpcjRlt.direction]);
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

	private static Plot2DPanel ployPolyDeafult(PolyDefault pd, double intersectValue, int type, String id) {
		Plot2DPanel plot = new Plot2DPanel();

		addLineFromPloyDeafult(plot,pd,intersectValue,type,id);
		JFrame frame = new JFrame(id+" "+Polygon.directionString[type]);
		frame.setContentPane(plot);
		frame.setVisible(true);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle2D bounds = pd.getBounds();
		double bh = bounds.getHeight();
		double bw = bounds.getWidth();
		int width = 500;
		int height = 500;


		frame.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		frame.setSize(width,height);
		plot.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		plot.setSize(width,height);
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
