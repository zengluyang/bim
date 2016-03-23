package test.ifc.jyg;

public class Beam {

	private CoordinateOfPoint[] beamPoints = new CoordinateOfPoint[8]; 	//一根梁8个点
	
	public Beam() {
		
	}
	
	public void setPoints(CoordinateOfPoint[] beamPoints) {
		this.beamPoints = beamPoints;
	}
	
	public CoordinateOfPoint[] getPoints() {
		return beamPoints;
	}
}
