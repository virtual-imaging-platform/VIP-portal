  /*
  * To change this template, choose Tools | Templates
  * and open the template in the editor.
  */
package fr.insalyon.creatis.vip.simulationgui.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
  *
  * @author glatard
  */
public class VTKEmulator {

     // common
     private String fileName;
     private double[] bounds;
     private boolean error=false;
     // mhd
     private int nDims;
     private double[] spacing;
     private int[] size;
     private double[] offset;
     // vtp
     private int numberOfPoint;
     private float[] point;
     private int numberOfPolys;
     private int numberOfPointPerPolys;
     private int[] polys;
     private int[][]linesOfPolys;
     public VTKEmulator(String fileName) {
         this.fileName = fileName;
         if(fileName.endsWith("vtp"))readVTP();
         else if(fileName.endsWith("mhd"))readMHD();
     } 

     public double[] getBounds() {
         return bounds;
     }

     public void setBounds(double[] bounds) {
         this.bounds = bounds;
     }

     public double[] getOffset() {
         return offset;
     }

     public void setOffset(double[] offset) {
         this.offset = offset;
     }

     public String getFileName() {
         return fileName;
     }

     public void setFileName(String fileName) {
         this.fileName = fileName;
     }

     public int getnDims() {
         return nDims;
     }

     public void setnDims(int nDims) {
         this.nDims = nDims;
     }

     public int[] getSize() {
         return size;
     }

     public void setSize(int[] size) {
         this.size = size;
     }

     public double[] getSpacing() {
         return spacing;
     }

     public void setSpacing(double[] spacing) {
         this.spacing = spacing;
     }

     private void readMHD() {
         try {
             BufferedReader in = new BufferedReader(new FileReader(fileName));
             String str;
             while ((str = in.readLine()) != null) {
                 String[] tokens = str.split(" ");
                 if (tokens[0].equals("NDims")) {
                     nDims = Integer.parseInt(tokens[2]);
                     spacing = new double[nDims];
                     offset = new double[nDims];
                     size = new int[nDims];
                     bounds = new double[2 * nDims];
                 }
                 if (tokens[0].equals("ElementSpacing")) {
                     for (int i = 0; i < nDims; i++) {
                         spacing[i] = Double.parseDouble(tokens[2 + i]);
                     }

                 }
                 if (tokens[0].equals("DimSize")) {
                     for (int i = 0; i < nDims; i++) {
                         size[i] = Integer.parseInt(tokens[2 + i]);
                     }

                 }
                 if (tokens[0].equals("Offset")) {
                     for (int i = 0; i < nDims; i++) {
                         offset[i] = Double.parseDouble(tokens[2 + i]);
                     }

                 }
             }
             for (int i = 0; i < spacing.length; i++) {
                 bounds[2 * i] = offset[i];
                 bounds[2 * i + 1] = bounds[2 * i] + spacing[i] * size[i];
             }

             in.close();
         } catch (IOException e) {
              error=true;
               Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, e);
         }

     }
     private void readVTP() {
        try {
             BufferedReader in = new BufferedReader(new FileReader(fileName));
             String str;
             String element="";
             int i=0;
             int j=0;
             while ((str = in.readLine()) != null) {
                 {
                    if(str.startsWith("POINTS"))
                    {
                        String[] tokens = str.split(" ");
                        element=tokens[0];
                        numberOfPoint=Integer.valueOf(tokens[1]);
                        point=new float[numberOfPoint*3];
                        str = in.readLine(); //jump to the next line
                    }
                    if(str.startsWith("POLYGONS"))
                    {
                        String[] tokens = str.split(" ");
                        element=tokens[0];
                        numberOfPolys=Integer.valueOf(tokens[1]);
                        numberOfPointPerPolys=Integer.valueOf(tokens[2]);
                        polys=new int[numberOfPointPerPolys];
                        linesOfPolys=new int[numberOfPolys][1];
                        str = in.readLine(); //jump to the next line
                    }
                    
                   
                    if(element.equals("POINTS"))
                    {
                        String[] tokens = str.split(" ");
                        for(String s:tokens)
                        {
                            if(!s.isEmpty())
                            {
                                point[j]=Float.valueOf(s);
                                j++;
                            }
                            if(j==numberOfPoint*3)element="";
                        }
                    }
                    if(element.equals("POLYGONS"))
                    {
                        String[] tokens = str.split(" ");
                        for(String s:tokens)
                        {
                            if(!s.isEmpty())
                            {
                                polys[i]=Integer.valueOf(s);
                                i++;
                            }
                            if(i==numberOfPointPerPolys)element="";
                        }
                    }
                   
                 }
             }
             in.close();
             // bounding box;
             float maxX=0;
             float maxY=0;
             float maxZ=0;
             float minX=99999;
             float minY=99999;
             float minZ=99999;
             for(int k=0;k<point.length;k=k+3)
             {
                 if(point[k]>maxX)maxX=point[k];
                 if(point[k]<minX)minX=point[k];
                 
                 if(point[k+1]>maxY)maxY=point[k+1];
                 if(point[k]<minY)minY=point[k+1];
                 
                 if(point[k+2]>maxZ)maxZ=point[k+2];
                 if(point[k]<minZ)minZ=point[k+2];
             }
             bounds= new double[]{(double)minX,(double)maxX,(double)minY,(double)maxY,(double)minZ,(double)maxZ};
            // Make polys per lines
             int v=0;
             for(int k=0;k<linesOfPolys.length;k++)
             {
                 linesOfPolys[k]=new int[polys[v]+1];
                 for(int m=0;m<linesOfPolys[k].length;m++)
                 {
                     linesOfPolys[k][m]=polys[v+m];
                 }
                 v+=polys[v]+1;
             }
           
        
        
        } catch (IOException e) {
             Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, e);
             error=true;
         }
    }
    public int getNumberOfPoints()
    {
        return numberOfPoint;
    }
    public int getNumberOfPolys()
    {
        return numberOfPolys;
    }
    public int getNumberOfPointPerPolys()
    {
        return numberOfPointPerPolys;
    }
     public int[] getPolys()
    {
        return polys;
    }
    public float[] getPoints()
    {
        return point;
    }
    public int[] getLinesOfPolys(int index)
    {
       return linesOfPolys[index];
    }
    public boolean getThereIsAnError()
    {
        return error;
    }
}