package test.ifc.jyg;

 

public class RectangularArea {

	private double length;
	private double width;
	private double area;
	
	public RectangularArea(double length, double width) {
		if (length > width) {
			this.width = width;
			this.length = length;
		} else if (length < width) { 
			this.width = length;
			this.length = width;
		} 
	}
	
	public void setLength(double length) {
		this.length = length;
	}
	
	public double getLength() {
		return length;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setArea(double width, double length) {
		 
		this.area = width * length;
	}
	
	public String getArea() {
		return  "length : " + length + " width : " + width  + " area : "+ area;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub		 
		if(this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (obj != null && obj.getClass() == RectangularArea.class) {
			RectangularArea rec = (RectangularArea) obj;
			if (this.getLength() == rec.getLength() && this.getWidth() == rec.getWidth()) {
				return true;
			}  
		}
		
		return false;
	}
	/*
	public boolean equals(RectangularArea rectangle) { 
		
		if (length == rectangle.getLength() && width == rectangle.getWidth()) {
			return true;
		} else {
			return false;
		} 
	}*/ 
}
