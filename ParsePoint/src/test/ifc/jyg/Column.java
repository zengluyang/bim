package test.ifc.jyg;

public class Column {
	
	private CoordinateOfPoint[] columnPoints = new CoordinateOfPoint[8]; 	//һ������8����
	
	public Column() {
		
	}
	
	public void setPoints(CoordinateOfPoint[] columnPoints) {
		this.columnPoints = columnPoints;
	}
	
	public CoordinateOfPoint[] getPoints() {
		return columnPoints;
	}

}
