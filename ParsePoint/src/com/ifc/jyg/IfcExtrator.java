package com.ifc.jyg;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ZLY on 2016/3/28.
 */
public class IfcExtrator {
    private String finalMatlabFile;
    private String finalResultFile;
    private String ifcConvertExeName;
    private String inputIfcFileName;
    private ArrayList<Rectangle> recRlt = new ArrayList<Rectangle>();
    private ArrayList<Polygon> polyRlt = new ArrayList<Polygon>();
    private PrintWriter outMatlab;
    private PrintWriter outFinalResult;
    public String successMessage;

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
        this.finalMatlabFile = inputIfcFileName+"_final_Matlab_file.m";
    }

    public void setIfcConvertExeName(String ifcConvertExeName) {
        this.ifcConvertExeName = ifcConvertExeName;
    }

    private ParseObjFile generateObjFileAndGetPaser(String inputIfcFileName, int type) {
        String ifcType = Cuboid.typeIfcString[type];
        File inputIfcFile = new File(inputIfcFileName);
        String outputObjFileName = inputIfcFile.getName() + "_" + ifcType + ".obj";
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
        this.finalMatlabFile = inputIfcFileName+"_final_Matlab_file.m";
        BufferedWriter brMatlab = new BufferedWriter(new FileWriter(finalMatlabFile));
        outMatlab = new PrintWriter (brMatlab);
        BufferedWriter brFinalResult = new BufferedWriter(new FileWriter(finalResultFile));
        outFinalResult = new PrintWriter (brFinalResult);
    }

    public void extract() throws IOException {

        ParseObjFile parseBeamObjFile = this.generateObjFileAndGetPaser(inputIfcFileName,Cuboid.BEAM);
        ParseObjFile parseColumnObjFile = this.generateObjFileAndGetPaser(inputIfcFileName,Cuboid.COLUMN);
        ParseObjFile parseSlabObjFile = this.generateObjFileAndGetPaser(inputIfcFileName,Cuboid.POLYSLAB);

        IntersectRectangle ir = new IntersectRectangle();
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
                slabNeededRecCnt++;
            }

        }



        ArrayList<ArrayList<TreeSet<Rectangle>>> intersectResult = ir.getPartitionResult();
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

        outFinalResult.println("############ Generated by IfcSCBModler at "+new Date().toString()+"############");
        outFinalResult.println("############ "+recRlt.size()+" Rectangles");
        outFinalResult.println("############ "+polyRlt.size()+" Polygons");
    }

    public void printResultToFile() {

        for(Rectangle r:recRlt) {
            outFinalResult.println(String.format("REC %s",r.Id));
            outFinalResult.println(String.format("V %f %f %f",r.topLeft.getX(),r.topLeft.getX(),r.topLeft.getZ()));
            outFinalResult.println(String.format("V %f %f %f",r.topRight.getX(),r.topRight.getX(),r.topRight.getZ()));
            outFinalResult.println(String.format("V %f %f %f",r.downRight.getX(),r.downRight.getX(),r.downRight.getZ()));
            outFinalResult.println(String.format("V %f %f %f",r.downLeft.getX(),r.downLeft.getX(),r.downLeft.getZ()));
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
        successMessage = "Created results at: "+finalMatlabFile+" and "+finalResultFile;
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


