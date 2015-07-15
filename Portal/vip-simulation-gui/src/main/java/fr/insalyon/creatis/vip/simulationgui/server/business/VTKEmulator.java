/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.server.business;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Tristan Glatard
 */
public class VTKEmulator {
    
    private static Logger logger = Logger.getLogger(VTKEmulator.class);

    // common
    private String fileName;
    private double[] bounds;
    private boolean error = false;
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
    private int[][] linesOfPolys;

    public VTKEmulator(String fileName) {
        
        this.fileName = fileName;
   
        if (fileName.endsWith("vtp") || fileName.endsWith("vtk")) {
            readVTP();
        } else if (fileName.endsWith("mhd")) {
            readMHD();
        }
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
                  System.out.println("ok1");
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
      System.out.println("ok2");
            in.close();
            
        } catch (IOException ex) {
            error = true;
            logger.error(ex);
        }
    }

    private void readVTP() {
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            String element = "";
            int i = 0;
            int j = 0;
            while ((str = in.readLine()) != null) {
                {                    if (str.startsWith("POINTS")) {
                        String[] tokens = str.split(" ");
                        element = tokens[0];
                        numberOfPoint = Integer.valueOf(tokens[1]);
                        point = new float[numberOfPoint * 3];
                        str = in.readLine(); //jump to the next line
                    }
                    if (str.startsWith("POLYGONS")) {
                        String[] tokens = str.split(" ");
                        element = tokens[0];
                        numberOfPolys = Integer.valueOf(tokens[1]);
                        numberOfPointPerPolys = Integer.valueOf(tokens[2]);
                        polys = new int[numberOfPointPerPolys];
                        linesOfPolys = new int[numberOfPolys][1];
                        str = in.readLine(); //jump to the next line
                    }

                    if (element.equals("POINTS")) {
                        String[] tokens = str.split(" ");
                        for (String s : tokens) {
                            if (!s.isEmpty()) {
                                point[j] = Float.valueOf(s);
                                j++;
                            }
                            if (j == numberOfPoint * 3) {
                                element = "";
                            }
                        }
                    }
                    if (element.equals("POLYGONS")) {
                        String[] tokens = str.split(" ");
                        for (String s : tokens) {
                            if (!s.isEmpty()) {
                                polys[i] = Integer.valueOf(s);
                                i++;
                            }
                            if (i == numberOfPointPerPolys) {
                                element = "";
                            }
                        }
                    }

                }
            }
            in.close();
            // bounding box;
            float maxX = 0;
            float maxY = 0;
            float maxZ = 0;
            float minX = 99999;
            float minY = 99999;
            float minZ = 99999;
            for (int k = 0; k < point.length; k = k + 3) {
                if (point[k] > maxX) {
                    maxX = point[k];
                }
                if (point[k] < minX) {
                    minX = point[k];
                }

                if (point[k + 1] > maxY) {
                    maxY = point[k + 1];
                }
                if (point[k] < minY) {
                    minY = point[k + 1];
                }

                if (point[k + 2] > maxZ) {
                    maxZ = point[k + 2];
                }
                if (point[k] < minZ) {
                    minZ = point[k + 2];
                }
            }
            bounds = new double[]{(double) minX, (double) maxX, (double) minY, (double) maxY, (double) minZ, (double) maxZ};
            // Make polys per lines
            int v = 0;
            for (int k = 0; k < linesOfPolys.length; k++) {
                linesOfPolys[k] = new int[polys[v] + 1];
                for (int m = 0; m < linesOfPolys[k].length; m++) {
                    linesOfPolys[k][m] = polys[v + m];
                }
                v += polys[v] + 1;
            }
            
        } catch (IOException ex) {
            logger.error(ex);
            error = true;
        }
    }

    public int getNumberOfPoints() {
        return numberOfPoint;
    }

    public int getNumberOfPolys() {
        return numberOfPolys;
    }

    public int getNumberOfPointPerPolys() {
        return numberOfPointPerPolys;
    }

    public int[] getPolys() {
        return polys;
    }

    public float[] getPoints() {
        return point;
    }

    public int[] getLinesOfPolys(int index) {
        return linesOfPolys[index];
    }

    public boolean getThereIsAnError() {
        return error;
    }
}