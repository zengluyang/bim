package com.bim.jyg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class test {

	private static Scanner cin;
	private static BufferedWriter bw = null;
	public static void main(String[] args) throws IOException {
		String inputFileName = "E:\\IFC\\objFile\\YD_S_B04_1F.ifc_final_result_2.txt";
		String outputFileName = "E:\\IFC\\objFile\\newPolygonPointsAndEdge_outPut_2.txt";
		cin = new Scanner(System.in);
		while (cin.hasNext()) {
			double standardLength = cin.nextDouble();	//input standard length
			RectangleDecompositor rectangleDecompositor = new RectangleDecompositor(inputFileName,outputFileName,standardLength);
			rectangleDecompositor.doDecompose();
		}

	}
}

