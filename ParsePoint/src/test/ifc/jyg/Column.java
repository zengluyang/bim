package test.ifc.jyg;

public class Column {
	
	private CoordinateOfPoint[] columnPoints = new CoordinateOfPoint[8]; 	//一个柱子8个点
	
	public Column() {
		
	}
	
	public void setPoints(CoordinateOfPoint[] columnPoints) {
		this.columnPoints = columnPoints;
	}
	
	public CoordinateOfPoint[] getPoints() {
		return columnPoints;
	}

}
