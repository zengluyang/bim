package com.ifc.jyg;

public class MarkLocation {

	private int a;
	private int b;
	private int c;
	private String ID;
	
	public MarkLocation(int a, int b, int c, String ID) {
		this.setA(a);
		this.setB(b);
		this.setC(c);
		this.ID = ID;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		this.ID = iD;
	}
}
