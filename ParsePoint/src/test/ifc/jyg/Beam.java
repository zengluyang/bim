package test.ifc.jyg;

public class Beam {

	private CoordinateOfPoint[] beamPoints = new CoordinateOfPoint[8]; 	//һ����8����
	
	public Beam() {
		
	}
	
	public void setPoints(CoordinateOfPoint[] beamPoints) {
		this.beamPoints = beamPoints;
	}
	
	public CoordinateOfPoint[] getPoints() {
		return beamPoints;
	}
}
