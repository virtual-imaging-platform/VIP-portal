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
package fr.insalyon.creatis.vip.simulationgui.client.gwtgl;

import fr.insalyon.creatis.vip.simulationgui.client.bean.Data3D;
import com.smartgwt.client.util.SC;
/**
 *
 * @author Kevin Moulin
 */
public class Object3D {

    protected int angleX;
    protected int angleY;
    protected int angleZ;
    protected float translateZ;
    protected float translateX;
    protected float translateY;
    protected String id;
    protected Data3D model;
    protected Data3D box;
    protected Data3D axis;
    protected double boundingBox[] = new double[]{-1, 1, -1, 1, -1, 1};

    private void initVertex() {
    }

    private void initColors() {
    }

    private void initIndices() {
    }

    public Object3D() {
    }

    public Object3D(String contents) {
        id = contents;
    }

    public int getAngleX() {
        return angleX;
    }

    public int getAngleY() {
        return angleY;
    }

    public int getAngleZ() {
        return angleZ;
    }

    public float getTranslateX() {
        return translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

    public float getTranslateZ() {
        return translateZ;
    }

    public float[] getVertices() {
        return parsor(box.getVertices(), model.getVertices(), axis.getVertices());
    }

    public float[] getColors() {
        return parsor(box.getColors(), model.getColors(), axis.getColors());
    }

    public int[] getIndices() {
        return mapping(box.getIndices(), model.getIndices(), axis.getIndices());
    }

    public float[] getNormals() {
        return parsor(box.getNormals(), model.getNormals(), axis.getNormals());
    }

    public int getItemSizeColor() {
        return 4;
        //model.GetItemSizeColor();
    }

    public int getItemSizeVertex() {
        return 3;
        //model.GetItemSizeVertex();
    }

    public int getNumItemIndex() {
        return box.getNumItemIndex() + axis.getNumItemIndex() + model.getNumItemIndex();
    }

    public double[] getBoundingBox() {
        return boundingBox;
    }

    public void setAngleX(int x) {
        angleX = x;
    }

    public void setAngleY(int y) {
        angleY = y;
    }

    public void setAngleZ(int z) {
        angleZ = z;
    }

    public void setTranslateX(float x) {
        translateX = x;
    }

    public void setTranslateY(float y) {
        translateY = y;
    }

    public void setTranslateZ(float z) {
        translateZ = z;
    }

    public void setVertices(float[] v, String ID) {
        if ("model".equals(ID)) {
            model.setVertices(v);
        }
        if ("box".equals(ID)) {
            box.setVertices(v);
        }
        if ("axis".equals(ID)) {
            axis.setVertices(v);
        }
    }

    public void setColors(float[] c, String ID) {
        if ("model".equals(ID)) {
            model.setColors(c);
        }
        if ("box".equals(ID)) {
            box.setColors(c);
        }
        if ("axis".equals(ID)) {
            axis.setColors(c);
        }
    }

    public void setIndices(int[] i, String ID) {
        if ("model".equals(ID)) {
            model.setIndices(i);
        }
        if ("box".equals(ID)) {
            box.setIndices(i);
        }
        if ("axis".equals(ID)) {
            axis.setIndices(i);
        }
    }

    public void setItemSizeColor(int c, String ID) {
        if ("model".equals(ID)) {
            model.setItemSizeColor(c);
        }
        if ("box".equals(ID)) {
            box.setItemSizeColor(c);
        }
        if ("axis".equals(ID)) {
            axis.setItemSizeColor(c);
        }

    }

    public void setItemSizeVertex(int v, String ID) {
        if ("model".equals(ID)) {
            model.setItemSizeVertex(v);
        }
        if ("box".equals(ID)) {
            box.setItemSizeVertex(v);
        }
        if ("axis".equals(ID)) {
            axis.setItemSizeVertex(v);
        }
    }

    public void setNumItemIndex(int i, String ID) {
        if ("model".equals(ID)) {
            model.setNumItemIndex(i);
        }
        if ("box".equals(ID)) {
            box.setNumItemIndex(i);
        }
        if ("axis".equals(ID)) {
            axis.setNumItemIndex(i);
        }
    }

    public void buildNormals() {
        model.buildNormals();
        box.buildNormals();
        axis.buildNormals();
    }

    public void reconstructor(Data3D DATA) {
    }

    public void resizeVertex() {
        float weight = weighting();
        float[] vertices = new float[box.getSupVertices().length];
        for (int i = 0; i < box.getSupVertices().length; i++) {
            vertices[i] = box.getSupVertices()[i] * weight;
        }
        box.setVertices(vertices);

        vertices = new float[axis.getSupVertices().length];
        for (int i = 0; i < axis.getSupVertices().length; i++) {
            vertices[i] = axis.getSupVertices()[i] * weight;
        }
        axis.setVertices(vertices);

        vertices = new float[model.getSupVertices().length];
        for (int i = 0; i < model.getSupVertices().length; i++) {
            vertices[i] = model.getSupVertices()[i] * weight;
        }
        model.setVertices(vertices);

    }

    public void setBoundingBox(double[] b) {
        boundingBox = b;
        model.setBoundingBox(b);
        box.setBoundingBox(b);
        axis.setBoundingBox(b);
        initVertex();
    }

    public void enable(String ID) {
        if ("model".equals(ID)) {
            model.enable();
        }
        if ("box".equals(ID)) {
            box.enable();
        }
        if ("axis".equals(ID)) {
            axis.enable();
        }
    }

    public void disable(String ID) {
        if ("model".equals(ID)) {
            model.disable();
        }
        if ("box".equals(ID)) {
            box.disable();
        }
        if ("axis".equals(ID)) {
            axis.disable();
        }
    }

    protected float[] parsor(float[] vert0, float[] vert1, float[] vert2) {
        float[] tmp;
        float[][] V = new float[3][1];
        V[0] = vert0;
        V[1] = vert1;
        V[2] = vert2;
        int taille = 0;
        if (V[0] != null) {
            taille = V[0].length;
        }
        if (V[1] != null) {
            taille = taille + V[1].length;
        }
        if (V[2] != null) {
            taille = taille + V[2].length;
        }
        if (V[0] == null && V[1] == null && V[2] == null) {
            tmp = null;
        } else {
            tmp = new float[taille];
        }

        int k = 0;
        for (int j = 0; j < 3; j++) {
            if (V[j] != null) {
                for (int i = 0; i < V[j].length; i++) {
                    tmp[k] = V[j][i];
                    k++;
                }
            }
        }
        return tmp;
    }

    protected int[] mapping(int indic0[], int indic1[], int indic2[]) {
        int[] tmp;
        int[][] I = new int[3][1];
        I[0] = indic0;
        I[1] = indic1;
        I[2] = indic2;
        int taille = 0;
        if (I[0] != null) {
            taille = I[0].length;
        }
        if (I[1] != null) {
            taille = taille + I[1].length;
        }
        if (I[2] != null) {
            taille = taille + I[2].length;
        }
        if (I[0] == null && I[1] == null && I[2] == null) {
            tmp = null;
        } else {
            tmp = new int[taille];
        }
        int max = 0;
        int k = 0;
        int ad = 0;
        // indicateur !
        for (int j = 0; j < 3; j++) {
            if (I[j] != null) {
                max = 0;
                for (int i = 0; i < (I[j].length); i++) {
                    tmp[k] = I[j][i] + ad;
                    if (tmp[k] > max) {
                        max = tmp[k];
                    }
                    k++;
                }
                ad = max + 1;

            }
        }
        return tmp;
    }

    protected float weighting() {
       
        float maxx = (float) boundingBox[1];
        float minx = (float) boundingBox[0];
        float maxy = (float) boundingBox[3];
        float miny = (float) boundingBox[2];
        float maxz = (float) boundingBox[5];
        float minz = (float) boundingBox[4];
        float tmp = maxx - minx;
        float tmp2 = maxy - miny;
    //    SC.say("maxx : "+ maxx + " minx :" +minx + " maxy:" + maxy + " miny:" + miny + " maxz:" + maxz + " minz:" + minz);
         return (maxx - minx);
     /*   if (tmp >= tmp2) {
            tmp2 = maxz - minz;
            if (tmp >= tmp2) {
                if (tmp != 0) {
                    return tmp;
                } else {
                    return 1;
                }
            } else {
                if (tmp2 != 0) {
                    return tmp2;
                } else {
                    return 1;
                }
            }
        } else {
            tmp = maxz - minz;
            if (tmp >= tmp2) {
                if (tmp != 0) {
                    return tmp;
                } else {
                    return 1;
                }
            } else {
                if (tmp2 != 0) {
                    return tmp2;
                } else {
                    return 1;
                }
            }
        }*/
    }
}
