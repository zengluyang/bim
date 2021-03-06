package com.ifc.jyg;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;

import java.io.*;
import java.util.*;

/**
 * Created by ZLY on 2016/3/28.
 */
public class IfcExtrator {
    private String finalMatlabFile;
    private String finalResultFile;
    private String finalResultFile2;
    private String ifcConvertExeName;
    private String inputIfcFileName;
    private ArrayList<Rectangle> recRlt = new ArrayList<Rectangle>();
    private ArrayList<Polygon> polyRlt = new ArrayList<Polygon>();
    private PrintWriter outMatlab;
    private PrintWriter outFinalResult;
    private PrintWriter outFinalResult2;
    public String successMessage;
    public ArrayList<PloyGpcjResult> ployGpcjResultList;
    public HashMap<String,PloyGpcjResult> ployGpcjResultHashMap = new HashMap<String,PloyGpcjResult>();
    public Map<Integer, Map<String, ArrayList<CoordinateOfPoint>>> rectangleIDAndPointsMap;
    public String getIfcConvertExeName() {
        return ifcConvertExeName;
    }

    public String getInputIfcFileName() {
        return inputIfcFileName;
    }

    public ArrayList<Rectangle> getRecRlt() {
        return recRlt;
    }

    public ArrayList<Polygon> getPolyRlt() {
        return polyRlt;
    }

    public void setInputIfcFileName(String inputIfcFileName) {
        this.inputIfcFileName = inputIfcFileName;
        this.finalResultFile = inputIfcFileName+""+"_final_result.txt";
        this.finalResultFile2 = inputIfcFileName+""+"_final_result_2.txt";
        this.finalMatlabFile = inputIfcFileName+"_final_Matlab_file.m";
    }

    public void setIfcConvertExeName(String ifcConvertExeName) {
        this.ifcConvertExeName = ifcConvertExeName;
        this.finalResultFile = inputIfcFileName+""+"_final_result.txt";
        this.finalResultFile2 = inputIfcFileName+""+"_final_result_2.txt";
        this.finalMatlabFile = inputIfcFileName+"_final_Matlab_file.m";
    }

    private ParseObjFile generateObjFileAndGetPaser(String inputIfcFileName, int type) {
        String ifcType = Cuboid.typeIfcString[type];
        File inputIfcFile = new File(inputIfcFileName);
        String outputObjFileName = inputIfcFile.getAbsolutePath() + "_" + ifcType + ".obj";
        File outputObjFile = new File(outputObjFileName);
        if (outputObjFile.lastModified() < inputIfcFile.lastModified()) {
            System.out.println("Objfile " + outputObjFileName + " out-of-date, converting using " + ifcConvertExeName);
            StringBuilder ifcConverteExePathSb = new StringBuilder();

            ifcConverteExePathSb
                    .append("cmd /c ").append(ifcConvertExeName).append(" ")
                    .append(inputIfcFileName).append(" ")
                    .append(outputObjFileName).append(" ")
                    .append("--use-world-coords ")
                    .append("--weld-vertices ")
                    .append("--include --entities ").append(ifcType);

            try {
                String line;
                Process p = Runtime.getRuntime().exec(ifcConverteExePathSb.toString());
                StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
                errorGobbler.start();
                int exitVal = p.waitFor();
                p.waitFor();
            } catch (Exception err) {
                err.printStackTrace();
            }
        } else {
            System.out.println("Objfile " + inputIfcFileName + " up-to-date.");
        }
        return new ParseObjFile(outputObjFileName, type);

    }

    public IfcExtrator() throws IOException {
        this.ifcConvertExeName = ".\\IfcConvert.exe";
        this.inputIfcFileName = ".\\YD_S_B04_1F.ifc";
        this.finalResultFile = inputIfcFileName+""+"_final_result.txt";
        this.finalResultFile2 = inputIfcFileName+""+"_final_result_2.txt";
        this.finalMatlabFile = inputIfcFileName+"_final_Matlab_file.m";
    }

    public void extract() throws IOException {
        BufferedWriter brMatlab = new BufferedWriter(new FileWriter(finalMatlabFile));
        outMatlab = new PrintWriter (brMatlab);
        BufferedWriter brFinalResult = new BufferedWriter(new FileWriter(finalResultFile));
        outFinalResult = new PrintWriter (brFinalResult);
        BufferedWriter brFinalResult2 = new BufferedWriter(new FileWriter(finalResultFile2));
        outFinalResult2 = new PrintWriter (brFinalResult2);

        ParseObjFile parseBeamObjFile = this.generateObjFileAndGetPaser(inputIfcFileName,Cuboid.BEAM);
        ParseObjFile parseColumnObjFile = this.generateObjFileAndGetPaser(inputIfcFileName,Cuboid.COLUMN);
        ParseObjFile parseSlabObjFile = this.generateObjFileAndGetPaser(inputIfcFileName,Cuboid.POLYSLAB);

        IntersectRectangle ir = new IntersectRectangle();
        IntersectRecangleUsingGpcjClipCompletely irupcc = new IntersectRecangleUsingGpcjClipCompletely();
        ArrayList<Cuboid> listCuboidsBeam = parseBeamObjFile.getCuboid();
        ArrayList<Cuboid> listCuboidsColumn = parseColumnObjFile.getCuboid();
        ArrayList<Cuboid> listCuboidsSlab = parseSlabObjFile.getCuboid();
        ArrayList<Polyhedron> listPolyhedron = parseSlabObjFile.getSlabPolys();
        ArrayList<Cuboid> listCuboids= new ArrayList<Cuboid>();
        listCuboids.addAll(listCuboidsBeam);
        listCuboids.addAll(listCuboidsColumn);
        listCuboids.addAll(listCuboidsSlab);

        for (int j = 0; j < listCuboids.size(); j++) {
            Cuboid cuboid = listCuboids.get(j);
            Set<Rectangle> neededRecs = cuboid.getNeededRectangels();
            if(neededRecs!=null) {
                for(Rectangle r : neededRecs) {
                    ir.addRectangleTogether(r);
                    irupcc.addRectangleTogether(r);
                }
            }
            //}
        }

        ArrayList<Polyhedron> ps = new ArrayList<Polyhedron>();
        ps.addAll(listPolyhedron);
        int slabNeededRecCnt = 0;
        int i=0;
        for (Polyhedron p:ps) {
            i++;

            polyRlt.add(p.getDownPolygon());
            outMatlab.println(String.format("figure(%d);\n",i));
            outMatlab.println(String.format("title('%s %f %s');\n",Rectangle.directionString[p.getDownPolygon().getDirection()],p.height,p.Id));
            outMatlab.println(p.getDownPolygon().toMatlab2D());
            outMatlab.println("%###############\n");
            ArrayList<Rectangle> recs = p.getNeededRectangles();

            for(Rectangle r:recs) {

                ir.addRectangleTogether(r);
                irupcc.addRectangleTogether(r);
                slabNeededRecCnt++;
            }

        }



        ArrayList<ArrayList<TreeSet<Rectangle>>> intersectResult = ir.getPartitionResult();
        this.ployGpcjResultList = irupcc.doClip();
        for(PloyGpcjResult rlt:ployGpcjResultList) {
            this.ployGpcjResultHashMap.put(rlt.getIds()+" "+rlt.direction+" "+rlt.intersectValue,rlt);
        }

        for(Polyhedron p:ps) {
            PloyGpcjResult ployGpcjResult = new PloyGpcjResult();
            ployGpcjResult.polyGpcj = Polygon.convertToGpcjPoly(p.getDownPolygon());
            ployGpcjResult.idList.add(p.Id);
            ployGpcjResult.direction=p.getDownPolygon().getDirection();
            ployGpcjResult.intersectValue=p.getDownPolygon().getIntersectValue();
            this.ployGpcjResultList.add(ployGpcjResult);
            this.ployGpcjResultHashMap.put(ployGpcjResult.getIds()+" "+ployGpcjResult.direction+" "+ployGpcjResult.intersectValue,ployGpcjResult);
        }
        int total_cnt=0;
        for(ArrayList<TreeSet<Rectangle>> recSetList:intersectResult) {
            for(TreeSet<Rectangle> recSet:recSetList) {
                if(recSet.size()==1) {
                    i++;
                    recSet.size();
                    Rectangle rec = recSet.first();
                    recRlt.add(rec);
                    if(rec.Id!=null && rec.Id.equals("BEA_____:200_x350mm:736737")) {
                        rec.toMatlab2d();
                    }
                    outMatlab.println(String.format("figure(%d);\n",i));
                    outMatlab.println(String.format("title('%s %f %s');\n",Rectangle.directionString[rec.getDirection()],rec.getIntersectvalue(),rec.Id));
                    outMatlab.println(rec.toMatlab2d());
                    outMatlab.println("%###############\n");
                } else {
                    Rectangle rec = recSet.first();
                    if(rec.Id!=null && rec.Id.equals("COL_____:600_x_600mm:566092")) {
                        rec.toMatlab2d();
                    }
                    Rectangle big = recSet.pollFirst();
                    if(big!=null&&recSet.size()!=0) {
                        ArrayList<Polygon> polygons = Rectangle.contrunctPolygonsUsingBigRectangleAndSmallRectangles(big, new ArrayList<Rectangle>(recSet));
                        for(Polygon p: polygons) {
                            i++;
                            polyRlt.add(p);
                            outMatlab.println(String.format("figure(%d);\n",i));
                            outMatlab.println(String.format("title('%s %f %s');\n",Rectangle.directionString[big.getDirection()],big.getIntersectvalue(),p.Id));
                            outMatlab.println(p.toMatlab2D());
                        }
                        outMatlab.println("%###############\n");

                    }

                }
            }
            outMatlab.println("%!!!!!!!!!!!!!!!!!\n");
        }
        outMatlab.println("%i "+i);

    }

    public void printResultToFile() {
        outFinalResult.println("############ Generated by IfcSCBModler at "+new Date().toString()+"############");
        outFinalResult.println("############ "+recRlt.size()+" Rectangles");
        outFinalResult.println("############ "+polyRlt.size()+" Polygons");

        for(Rectangle r:recRlt) {
            outFinalResult.println(String.format("REC %s",r.Id));
            outFinalResult.println(String.format("V %f %f %f",r.topLeft.getX(),r.topLeft.getY(),r.topLeft.getZ()));
            outFinalResult.println(String.format("V %f %f %f",r.topRight.getX(),r.topRight.getY(),r.topRight.getZ()));
            outFinalResult.println(String.format("V %f %f %f",r.downRight.getX(),r.downRight.getY(),r.downRight.getZ()));
            outFinalResult.println(String.format("V %f %f %f",r.downLeft.getX(),r.downLeft.getY(),r.downLeft.getZ()));
        }
        for(Polygon p:polyRlt) {
            outFinalResult.println(String.format("POL %s",p.Id));
            for(CoordinateOfPoint cop:p.getPointList()) {
                if(cop!=null)
                    outFinalResult.println(String.format("V %f %f %f",cop.getX(),cop.getY(),cop.getZ()));
            }
            for(Edge e:p.getEdgeList()) {
                outFinalResult.println(String.format("E %f %f %f %f %f %f",
                        e.getFirst().getX(),e.getFirst().getY(),e.getFirst().getZ(),
                        e.getSecond().getX(),e.getSecond().getY(),e.getSecond().getZ()
                ));
            }
        }
        outFinalResult.flush();
        outFinalResult.close();
        outMatlab.flush();
        outMatlab.close();
        successMessage = "Created results at: "+finalMatlabFile+" and "+finalResultFile;
        System.out.println(successMessage);

    }

    public void printResultToFile2() {
        rectangleIDAndPointsMap = new HashMap<Integer, Map<String, ArrayList<CoordinateOfPoint>>>();
        int rectangleNumber=0;
        outFinalResult2.println("############ Generated by IfcSCBModlerV2 at "+new Date().toString()+"############");
        outFinalResult2.println("############ "+ployGpcjResultList.size()+" Polygons");
        for(PloyGpcjResult pgr:ployGpcjResultList) {
            PolyDefault pd = (PolyDefault) pgr.polyGpcj;
            String ids = pgr.getIds();
            ArrayList<Polygon>  pps = Polygon.convertFromGpcjPolyRecursively(pd,pgr.intersectValue,pgr.direction,ids);
            if(pps.size()>1) {
                //System.out.println("pps.size() "+pps.size());
            }
            for(Polygon p:pps) {
                String POL = "POL";
                String REC = "REC";
                boolean isRec = p.getPointList().size()==4;
                outFinalResult2.println(String.format("%s %s %d %f",isRec?REC:POL,p.Id,p.getDirection(),p.getIntersectValue()));
                for(CoordinateOfPoint cop:p.getPointList()) {
                    if(cop!=null)
                        outFinalResult2.println(String.format("V %f %f %f",cop.getX(),cop.getY(),cop.getZ()));
                }
                for(Edge e:p.getEdgeList()) {
                    outFinalResult2.println(String.format("E %f %f %f %f %f %f",
                            e.getFirst().getX(),e.getFirst().getY(),e.getFirst().getZ(),
                            e.getSecond().getX(),e.getSecond().getY(),e.getSecond().getZ()
                    ));
                }
                if(isRec) {
                    rectangleNumber++;
                    if (! rectangleIDAndPointsMap.containsKey(rectangleNumber)) {
                        Map<String, ArrayList<CoordinateOfPoint>> IDAndPointsMap = new HashMap<String, ArrayList<CoordinateOfPoint>>();
                        ArrayList<CoordinateOfPoint> points = new ArrayList<CoordinateOfPoint>();
                        points.addAll(p.getPointList());
                        IDAndPointsMap.put(p.Id+" "+p.getDirection()+" "+p.getIntersectValue(), points);
                        rectangleIDAndPointsMap.put(rectangleNumber, IDAndPointsMap);
                    } else {

                    }
                }
            }
        }
        outFinalResult2.flush();
        outFinalResult2.close();
        successMessage = "Created results at: "+finalResultFile2;
        System.out.println(successMessage);

    }


}

class StreamGobbler extends Thread
{
    InputStream is;
    String type;

    StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }

    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null)
                System.out.println("%"+type + ">" + line);
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}


