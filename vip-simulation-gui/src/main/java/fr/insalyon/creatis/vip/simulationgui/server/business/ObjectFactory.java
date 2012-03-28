/* Copyright CNRS-CREATIS
 *
 * Rafael Silva
 * rafael.silva@creatis.insa-lyon.fr
 * http://www.rafaelsilva.com
 *
 * This software is a grid-enabled data-driven workflow manager and editor.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
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
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.simulationgui.server.business;

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 *
 * @author Kevin Moulin
 */
public class ObjectFactory {

    public ObjectFactory() {
    }

    public static Data3D build(String path) {

        if (path.endsWith(".vtp") || path.endsWith(".vtk")) {
            return VTPBuilder(path);
        } else if (path.endsWith(".mhd")) {
            return MHDBuilder(path);
        } else {
            return null;
        }
    }

    public static Data3D[][] buildMulti(String path, String[][] objectList, String[] type) {

       boolean[] bbound = new boolean[objectList.length];
      System.out.println("object to create");
        Data3D[][] objectTab = new Data3D[objectList.length][1];
        int i = 0;
        int j = 0;


        for (String[] st : objectList) {
            j = 0;
            bbound[i] = false;
            for (String st1d : st) {
                String tmp = st1d.replace("[", "").replace("]", "");
                if (tmp.endsWith(".zraw") || tmp.endsWith(".raw") || tmp.endsWith(".mhd")) {
                    j--;
                }
                j++;
            }
            objectTab[i] = new Data3D[j + 1];
            i++;
        }
        i = 0;
        for (String[] st : objectList) {
            ArrayList<Data3D> temp = new ArrayList<Data3D>();
            for (String st1d : st) {
                String tmp = st1d.replace("[", "").replace("]", "");
                if (tmp.endsWith(".zraw") || tmp.endsWith(".raw") || tmp.endsWith(".mhd")) {
                    StringTokenizer st1dTokenize = new StringTokenizer(tmp);
                    temp.add(addMHD(path, st1dTokenize.nextToken(","), type[i]));
                    bbound[i] = true;
                }
            }
            
            double[] box = new double[6];
            for(Data3D d : temp)
            {
                double[] bound_temp = d.getBoundingBox();
                if(bound_temp[0]< box[0])
                    box[0] = bound_temp[0];
                if(bound_temp[1]> box[1])
                    box[1] = bound_temp[1];
                if(bound_temp[2]< box[2])
                    box[2] = bound_temp[2];
                if(bound_temp[3]> box[3])
                    box[3] = bound_temp[3];
                if(bound_temp[4]< box[4])
                    box[4] = bound_temp[4];
                if(bound_temp[5]> box[5])
                    box[5] = bound_temp[5];
            }
            objectTab[i][0] = temp.get(0);
            objectTab[i][0].setBoundingBox(box);
            i++;
        }
        i = 0;
        for (String[] st2 : objectList) {
            j = 1;
            
            for (String st2d : st2) {
                String tmp = st2d.replace("[", "").replace("]", "");
                if (tmp.endsWith(".vtp") || tmp.endsWith(".vtk")) {
                    System.out.println("object" + i + " " + j + " created");
                    objectTab[i][j] = addVTP(path, tmp, type[i], objectTab[i][0].getBoundingBox(), bbound[i]);
                    j++;
                }
            }
            
            // If no mhd in this model on this layer
            if(!bbound[i])
            {
                double[] box = new double[6];
                findBounds(objectTab[i], box);
                setBounds(objectTab[i], box);
            }
            i++;
        }
        

        System.out.println("object created");
        return objectTab;
    }

    private static Data3D addMHD(String path, String name, String type) {
        
        Data3D object = new Data3D(name);
        object.setType(type);
        VTKEmulator vtk = new VTKEmulator(path + "/" + name);
        if (!vtk.getThereIsAnError()) {
            make(object, null, null, null, vtk.getBounds(), 0, 1.0f);
        }
        
        return object;
    }

    /**
	 * Find the bounding box of Data3D(meshes) list.
	 * @param list of Data3D object
	 * @param bounding box
	 */
    private static void  findBounds(Data3D[] DATA, double []box)
    {
        int i,j;
        
        for(Data3D data : DATA)
        {
            for(i = 0, j = 1; i <6; i+=2, j+=2)
            {
                if(box[i] < data.getBoundingBox()[i])
                    box[i] = data.getBoundingBox()[i];
                if(box[j] > data.getBoundingBox()[j])
                    box[j] = data.getBoundingBox()[j];
            }
        }
    }
    
    /**
	 * Set the new bounding box dimensions for each Data3D
	 * @param list of Data3D
	 * @param bounding box
     */
    private static void setBounds(Data3D[] DATA, double []box)
    {
        // center of gravity
        float[] gravit = new float[3];
        for(int i = 0; i < 3; i++)
            gravit[i]= (float) (box[i*2+1] + box[i*2]) / 2;
        
        for(Data3D data : DATA)
        {
            // set the associated bounding box of data
            double[] bounds = data.getBoundingBox();
            for(int i = 0; i < 6; i++)
                bounds[i]= bounds[i] - gravit[i/2];
            
            float l = Math.abs((float) (bounds[1] - bounds[0]));
            // set the new position of vertices
            for (int i = 0; i < data.getItemSizeVertex(); i++) {
                data.getVertices()[i*3] = data.getVertices()[i*3] - gravit[0];
                data.getVertices()[i*3 + 1] = data.getVertices()[i*3 + 1] - gravit[1];
                data.getVertices()[i*3 + 2] = data.getVertices()[i*3 + 2] - gravit[2];
            }
            data.setBoundingBox(bounds);
            data.setLenghtInfo(l);
        }
    }
    
    private static Data3D addVTP(String path, String name, String type, double[] bounds, boolean bbound) {
        
        float xgravit = 0;
        float ygravit = 0;
        float zgravit = 0;
         
        Data3D object = new Data3D(name);
        object.setType(type);

        VTKEmulator DATA = new VTKEmulator(path + "/" + name);
        
        if (!DATA.getThereIsAnError()) {
            // if a mhd image is associated, we take the corresponding bounds
            //System.out.println(i + " x:" + vertex[i]+" y:"+ vertex[i + 1]+ " z:"+vertex[i + 2]);
            if(bbound)
            {
                System.out.println(" 0 : " + bounds[0] 
                        +" 1 : " + bounds[1] 
                        +" 2 : " + bounds[2] 
                        +" 3 : " + bounds[3] 
                        +" 4 : " + bounds[4] 
                        +" 5 : " + bounds[5] );
                xgravit = (float) (bounds[1] + bounds[0]) / 2;
                ygravit = (float) (bounds[3] + bounds[2]) / 2;
                zgravit = (float) (bounds[5] + bounds[4]) / 2;
            }
            else
            {
                bounds = new double[6];
            }

            Random r = new Random();
            float R = r.nextFloat();//Returns random float >= 0.0 and < 1.0
            float G = r.nextFloat();
            float B = r.nextFloat();
            int pointOfTriangle = 0;
            for (int c = 0; c < DATA.getNumberOfPolys(); c++) {
                if (DATA.getLinesOfPolys(c)[0] == 3) {
                    pointOfTriangle += 3; // 1 triangles => 3 points  
                } else {
                    pointOfTriangle += 6; // 2 triangles => 6 points
                }
            }

            float[] vertex = new float[DATA.getNumberOfPoints() * 3]; // 3 données par points !
            int[] indices = new int[pointOfTriangle]; // 3 données par lignes
            float[] colors = new float[DATA.getNumberOfPoints() * 4]; // 4 données par lignes : RGB et DETH

            int j = 0; // compteur
            // indices parsor
            for (int c = 0; c < DATA.getNumberOfPolys(); c++) {
                if (DATA.getLinesOfPolys(c)[0] == 3) {
                    indices[j] = DATA.getLinesOfPolys(c)[1];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[2];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[3];
                    j++;
                } else {
                    indices[j] = DATA.getLinesOfPolys(c)[1];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[2];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[3];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[1];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[3];
                    j++;
                    indices[j] = DATA.getLinesOfPolys(c)[4];
                    j++;
                }
            }

            // vertex parsor
            j = 0;
            System.out.println(DATA.getNumberOfPoints());
            for (int i = 0; i < DATA.getNumberOfPoints() * 3; i = i + 3) {
                vertex[i] = DATA.getPoints()[i] - xgravit;
                vertex[i + 1] = DATA.getPoints()[i + 1] - ygravit;
                vertex[i + 2] = DATA.getPoints()[i + 2] - zgravit;
                j++;
            }
            // color maker
            for (int i = 0; i < DATA.getNumberOfPoints() * 4; i = i + 4) // pour chaque ligne de vertex on ajoute une couleur !
            {
                colors[i] = R;
                colors[i + 1] = G;
                colors[i + 2] = B;
                colors[i + 3] = 1.0f;
            }
            bounds = DATA.getBounds();

            // if no mhd image is associated, the bounds are coming from mesh bounds.
            bounds[0] = bounds[0] - xgravit;
            bounds[1] = bounds[1] - xgravit;
            bounds[2] = bounds[2] - ygravit;
            bounds[3] = bounds[3] - ygravit;
            bounds[4] = bounds[4] - zgravit;
            bounds[5] = bounds[5] - zgravit;
            System.out.println(bounds);
            make(object, colors, indices, vertex, bounds, indices.length, 1.0f);
        }
        return object;
    }

    private static Data3D VTPBuilder(String path) {
        
        Data3D object = new Data3D("model");
        VTKEmulator DATA = new VTKEmulator(path);

        double[] bounds = DATA.getBounds(); // boite anglobante ( xmin xmax ymin ymax zmin zmax )
        float xgravit = (float) (bounds[1] + bounds[0]) / 2;
        float ygravit = (float) (bounds[3] + bounds[2]) / 2;
        float zgravit = (float) (bounds[5] + bounds[4]) / 2;

        Random r = new Random();
        float R = r.nextFloat();//Returns random float >= 0.0 and < 1.0
        float G = r.nextFloat();
        float B = r.nextFloat();

        int pointOfTriangle = 0;
        for (int c = 0; c < DATA.getNumberOfPolys(); c++) {
            if (DATA.getLinesOfPolys(c)[0] == 3) {
                pointOfTriangle += 3; // 1 triangles => 3 points  
            } else {
                pointOfTriangle += 6; // 2 triangles => 6 points
            }
        }

        float[] vertex = new float[DATA.getNumberOfPoints() * 3]; // 3 données par points !
        int[] indices = new int[pointOfTriangle]; // 3 données par lignes
        float[] colors = new float[DATA.getNumberOfPoints() * 4]; // 4 données par lignes : RGB et DETH

        int j = 0; // compteur
        // indices parsor
        for (int c = 0; c < DATA.getNumberOfPolys(); c++) {
            if (DATA.getLinesOfPolys(c)[0] == 3) {
                indices[j] = DATA.getLinesOfPolys(c)[1];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[2];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[3];
                j++;
            } else {
                indices[j] = DATA.getLinesOfPolys(c)[1];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[2];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[3];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[1];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[3];
                j++;
                indices[j] = DATA.getLinesOfPolys(c)[4];
                j++;
            }
        }
        // vertex parsor
        for (int i = 0; i < DATA.getNumberOfPoints() * 3; i = i + 3) {
            vertex[i] = DATA.getPoints()[i] - xgravit;
            vertex[i + 1] = DATA.getPoints()[i + 1] - ygravit;
            vertex[i + 2] = DATA.getPoints()[i + 2] - zgravit;
        }

        // color maker
        for (int i = 0; i < DATA.getNumberOfPoints() * 4; i = i + 4) // pour chaque ligne de vertex on ajoute une couleur !
        {
            colors[i] = R;
            colors[i + 1] = G;
            colors[i + 2] = B;
            colors[i + 3] = 1.0f;
        }
        make(object, colors, indices, vertex, bounds, indices.length, 1.0f);
        return object;
    }

    private static Data3D MHDBuilder(String path) {
        Data3D object = new Data3D("box");
        VTKEmulator vtk = new VTKEmulator(path);
        make(object, null, null, null, vtk.getBounds(), 0, 1.0f);
        return object;
    }

    private static void make(Data3D object, float[] c, int[] i, float[] v, double[] b, int n, float alphaInfo) {
        
        object.setColors(c);
        object.setIndices(i);
        object.setVertices(v);
        object.setBoundingBox(b);
        object.setNumItemIndex(n);
        object.setAlphaInfo(alphaInfo);
        float l = Math.abs((float) (b[1] - b[0]));//+Math.abs((float)(b[3]-b[2]))+Math.abs((float)(b[5]-b[4]));
        object.setLenghtInfo(l);
    }
}
